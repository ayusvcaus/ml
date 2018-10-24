package com.ayu.ml.viterbi2;

import java.io.*;
import java.util.*;

public class Viterbi {

    private static Map<String,Map<String, Double>> transition; // previous tag -> (current tag -> P(current|previous))
    private static Map<String,Map<String, Double>> emission; // tag -> (word -> P(tag|word))
    private static final double unseen = -100; // unseen word penalty of -100
    private static final String start = "#"; // default start

    /**
     * Train
     *
     * Method to train the POS tagger using the Hidden Markov model
     *
     * @param trainfile the file with the the train sentences
     * @param tagfile the file with the corresponding tags
     */
    private static void train(String trainfile, String tagfile){

        BufferedReader test = null, tag = null;
        try{
            test = new BufferedReader(new FileReader(trainfile)); // Open train file
            tag = new BufferedReader(new FileReader(tagfile)); // Open tag file

            String word, tagword;

            while((word = test.readLine()) != null && (tagword = tag.readLine()) != null){ // read the file
                String[] wordarray = word.split(" "); // split by spaces
                String[] tagarray = tagword.split(" ");

                // Fill in the emission map with number of emissions
                for (int i=0; i<wordarray.length; i++){
                    if(!emission.containsKey(tagarray[i])) { // if the tag appears for the first time
                        Map<String,Double> word2value = new HashMap<>();
                        word2value.put(wordarray[i],1.0);
                        emission.put(tagarray[i],word2value);
                    } else {
                        if (!emission.get(tagarray[i]).containsKey(wordarray[i])) { // if the word appeared with the tag for the first time
                            emission.get(tagarray[i]).put(wordarray[i], 1.0);
                        } else { // if the word has already appeared with the tag
                            double newvalue = emission.get(tagarray[i]).get(wordarray[i]) + 1;
                            emission.get(tagarray[i]).put(wordarray[i], newvalue);
                        }
                    }
                    // Fill in the transition map with number of transitions
                    if (i==0){ // the first transition is # -> (first tag -> P(first tag|#)
                        if (!transition.containsKey(start)) {
                            Map<String, Double> first2value = new HashMap<>();
                            first2value.put(tagarray[i], 1.0);
                            transition.put(start, first2value);
                        } else if (!transition.get(start).containsKey(tagarray[i])){
                            transition.get(start).put(tagarray[i], 1.0);
                        } else {
                            double newvalue = transition.get(start).get(tagarray[i]) + 1.0;
                            transition.get(start).put(tagarray[i],newvalue);
                        }
                    } else if (!transition.containsKey(tagarray[i-1])){ // if transition doesn't contain the previous tag
                        Map<String,Double> curr2value = new HashMap<>();
                        curr2value.put(tagarray[i],1.0);
                        transition.put(tagarray[i-1],curr2value);
                    } else { // if transition contains the previous tag
                        if (!transition.get(tagarray[i-1]).containsKey(tagarray[i])){ // but not to the current tag
                            transition.get(tagarray[i-1]).put(tagarray[i],1.0);
                        } else { // and to the current tag
                            double newvalue = transition.get(tagarray[i-1]).get(tagarray[i]) + 1;
                            transition.get(tagarray[i-1]).put(tagarray[i],newvalue);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close file if file exist. If not, catch the exception
            try {
            	if (test != null) {
                    test.close();
            	}
            	if (tag!=null) {
            		tag.close();
            	}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Normalize
     *
     * Method to normalize the probablity of the emission and transition maps and convert it to log10
     *
     */
    private static void normalize(){

        for (String a : emission.keySet()){
            double count = emission.get(a).size();
            for (String b : emission.get(a).keySet()) { // normalize the probability by dividing by count and taking log10
                double logprob = Math.log10(emission.get(a).get(b) / count);
                emission.get(a).put(b,logprob);
            }
        }

        for (String a : transition.keySet()){
            double count = transition.get(a).size();
            for (String b : transition.get(a).keySet()){ // normalize the probability by dividing by count and taking log10
                double logprob = Math.log10(transition.get(a).get(b) / count);
                transition.get(a).put(b,logprob);
            }
        }
    }

    /**
     * Viterbi
     *
     * Method to perform Viterbi tagging and backtracing
     *
     * @param input the sentence to be tagged
     * @return result - the tags associated with the sentence
     */
    public static String viterbi(String input){
        String[] words = input.split(" "); // the array of words

        Set<String> prevstates = new HashSet<>(); // the previous states
        prevstates.add(start); // start with "#"

        Map<String, Double> prevscores = new HashMap<>(); // the previous scores
        prevscores.put(start,0.0); // probability of starting is 100%
        
        List<Map<String, String>> backtrace = new ArrayList<>(); // List of current tag -> previous tag

        for (int i=0; i<words.length; i++) {
            Set<String> nextstates = new HashSet<>();
            Map<String, Double> nextscores = new HashMap<>();
            Map<String, String> backpoint = new HashMap<>();
            double score;

            for (String state: prevstates) { // for all previous tags
                if (transition.containsKey(state) && !transition.get(state).isEmpty()) {
                    for (String transit : transition.get(state).keySet()) { // for all transition of previous -> current tag
                        nextstates.add(transit);
                        if (emission.containsKey(transit) && emission.get(transit).containsKey(words[i])) { // if word is in emission
                            score = prevscores.get(state) + transition.get(state).get(transit) + emission.get(transit).get(words[i]);
                        } else { // if word is unseen, use an unseen score
                            score = prevscores.get(state) + transition.get(state).get(transit) + unseen;
                        }
                        if (!nextscores.containsKey(transit) || score>nextscores.get(transit)) {
                            nextscores.put(transit, score); // keep track of the highest score
                            backpoint.put(transit, state); // for backtracing later on
                            if (backtrace.size()>i) {
                            	backtrace.remove(i); // remove the last element if required to update the last element
                            }
                            backtrace.add(backpoint);
                        }
                    }
                }
            }
            prevscores = nextscores;
            prevstates = nextstates;
        }
        
        //Find the last tag for backtracing
        String lasttag = null;
        double highestscore = Double.NEGATIVE_INFINITY; // highest score in the Viterbi tagging. Set to negative infinity since score < 0
        for (String state: prevscores.keySet()) {
            if (prevscores.get(state)>highestscore) {
                highestscore = prevscores.get(state);
                lasttag = state;
            }
        }

        // Perform Backtrace
        Stack<String> toprint = new Stack<>(); // the stack for printing to result
        toprint.push(lasttag);
        for (int i=words.length-1; i>0; i--) {
            toprint.push(backtrace.get(i).get(toprint.peek()));
        }

        //Print to result
        String result = "";
        while (!toprint.isEmpty()) {
            result += toprint.pop()+" ";
        }
        return result;
    }

    /**
     * ViterbiConsole
     *
     * Method to perform Viterbi tagging on a user input from the console. Results will be printed on the console
     */
    public static void viterbiConsole(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a sentence for tagging");
        String sentence = scan.nextLine();
        String result = viterbi(sentence);
        System.out.println(result);
    }

    /**
     * ViterbiFile
     *
     * Method to perform Viterbi tagging on a file. Will return the tagged file
     * @param filename input file to be tagged
     * @return filename of the tagged file
     */
    public static List<String> viterbiFile(String filename) {
        BufferedReader input = null;
        List<String> output = new ArrayList<>();
        try {
            input = new BufferedReader(new FileReader(filename));
            String line = null, tagline = null;
            while ((line=input.readLine()) != null){ // perform viterbi taging line by line and write to output file
                tagline = viterbi(line);
                output.add(tagline);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            // Close file if file exist. If not, catch the exception
            try {
            	if (input != null) {
                    input.close();
            	}
            } catch (Exception e) {
                e.printStackTrace();
            }
            return output;
        }
    }

    /**
     * PrintStat
     *
     * Method to compare 2 tag files and print out the number of correct and incorrect tags
     * @param file1
     * @param file2
     */
    public static void printStat(String file1, List<String> trainedData){
        BufferedReader input1 = null;
        try{
            input1 = new BufferedReader(new FileReader(file1));
            String string1 = null;
            int totalcount = 0, totalmistake = 0, totalcorrect = 0;
            int j=0;
            while ((string1 = input1.readLine())!=null) {
            	String[] stringarray1 = string1.split(" ");
            	String[] stringarray2 = trainedData.get(j++).split(" ");
                for (int i=0; i<stringarray1.length; i++) { // count the number of mistakes
                    if (!stringarray1[i].equals(stringarray2[i])) {
                        totalmistake++;
                    }
                }
                totalcount += stringarray1.length; // count the number of total tags
            }
            totalcorrect = totalcount - totalmistake;

            // print the statistics
            System.out.println("Testing with tagging "+file1);
            System.out.println("There are "+totalcount+" tags in total.");
            System.out.println(totalcorrect+ " tags are correct and "+totalmistake+" tags are wrong.");
            System.out.println("The accuracy = "+ 100 * totalcorrect/totalcount + "%");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input1.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // the train files
        String trainfile = "data/viterbi2/brown-train-sentences.txt", traintag = "data/viterbi2/brown-train-tags.txt";
        // the test files
        String testfile = "data/viterbi2/brown-test-sentences.txt", testtag = "data/viterbi2/brown-test-tags.txt";

        transition = new HashMap<>();
        
        emission = new HashMap<>();

        long t1 = System.currentTimeMillis();
        train(trainfile, traintag); // train the algorithm
        long t2 = System.currentTimeMillis();
        System.out.println("t2-t1="+(t2-t1));
        normalize(); // normalize the probability
        long t3 = System.currentTimeMillis();
        System.out.println("t3-t2="+(t3-t2));

        List<String> newtags = viterbiFile(testfile); 
        long t4 = System.currentTimeMillis();
        System.out.println("t4-t3="+(t4-t3));
        printStat(testtag, newtags);
        viterbiConsole(); // tag console input
    }
}