package com.ayu.ml.rf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ayu.ml.rf.Entries;

public class Utils {
	
	
    /**
     * A utility function to read a CSV as a List of String Arrays, each element is a row.
     * @param filePath The CSV filepath.
     * @return The rows raw data.
     * @throws IOException In case of IOException.
     */
    public static List<String[]> readCSV(String filePath, boolean header, String delimiter, Object obj) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
        if (header) {
        	List<String> lst = Arrays.asList(fileReader.readLine().split(delimiter));
        	if (obj instanceof RandomForest) {
        		RandomForest forest = (RandomForest)obj;
                forest.setAttributesName(lst);
                // Usually specify the random subspace as the sqrt of the attributes number.;
                forest.setAttrSubspaceNum((int) Math.sqrt(lst.size()));
        	} else if (obj instanceof DecisionTree) {
        		DecisionTree tree = (DecisionTree)obj;
        		tree.setAttributesName(lst);
        		tree.setAttrSubspaceNum((int) Math.sqrt(lst.size()));
        	}
        }
        String line = null;
        List<String[]> entries = new ArrayList<>();
        while ((line = fileReader.readLine()) != null) {     	
            String[] tokens = line.split(delimiter);
            if (tokens.length > 0) {
                entries.add(tokens);
            }
        }
        return entries;
    }
    
    public static void saperatData(List<String[]> entries, String filePath, Object obj, List<String[]> trainEntries, List<String[]> testEntries, double rate) {
        try {
            if (obj instanceof RandomForest) {
        		RandomForest forest = (RandomForest)obj;
                entries = Utils.readCSV(filePath, true, ";", forest);
        	} else if (obj instanceof DecisionTree) {
        		DecisionTree tree = (DecisionTree)obj;
        		entries = Utils.readCSV(filePath, true, ";", tree);
        	}
        } catch (Exception e) {}

        int trainSize = (int) (entries.size() * rate);

        Set<Integer> testIndex = new HashSet<>();
        
        for (int j=0; j <entries.size()-trainSize; j++) {
            Integer index = (int)(Math.random()*(entries.size()));
            while (testIndex.contains(index)) {
                index = (int) (Math.random()*(entries.size()));
            }
            testIndex.add(index);
            testEntries.add(entries.get(index));
        }
        for (int i = 0; i < entries.size(); i ++) {
            if (testIndex.contains(i)) {
                continue;
            }
            trainEntries.add(entries.get(i));
        }
    }
	
    /**
     * A utility function for loadData function, to add data to DecisionTree properties.
     * @param training Indicate if the data is training data.
     * @param entries The entries needs to be filled in.
     */
    public static Entries loadData(List<String[]> entries, List<Boolean> typeSpecification) {
    	Entries data = new Entries();
        for (String[] s: entries) {
            Entry newEntry = new Entry();
            for (int i=0; i<s.length-1; i++) {
                newEntry.attributes.add(new CellData(s[i], typeSpecification.get(i)));
            }
            newEntry.label = s[s.length-1];
            data.entries.add(newEntry);
        }
    	return data;
    }


    public static void count(Map<String, Integer> map, String key) {
        try {
        	map.put(key, map.get(key) + 1);
        } catch (Exception e) {
        	map.put(key, 1);
        }
    }
}
