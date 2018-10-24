package com.ayu.ml.markov;

import com.ayu.ml.markov.DataDecoding;
import com.ayu.ml.markov.JsonParser;
import javafx.util.Pair;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;



public class Main {
    public static void main(String[] args) throws Exception {
        JsonParser jp = new JsonParser("data/markov/test_HMM.json");
        String name = jp.getName();
        List<String> states = DataDecoding.getStates(jp.getStates());
        List<String> observations = DataDecoding.getObservations(jp.getObservations());
        Map<String, Double> initialProbabilities = DataDecoding.getInitialProbabilities(jp.getInitialProbabilities());
        Map<Pair<String, String>, Double> transitionMatrix = DataDecoding.getTransitionMatrix(jp.getTransitionMatrix());
        Map<Pair<String, String>, Double> emissionMatrix = DataDecoding.getEmissionMatrix(jp.getEmissionMatrix());

        HiddenMarkovModel hmm = new HiddenMarkovModel(name, states, observations, initialProbabilities, transitionMatrix, emissionMatrix);
        List<String> sampleStates = new ArrayList<String>() {{
            add("R");
            add("S");
            add("R");
            add("R");
            add("S");
            add("R");
            add("R");
            add("S");
            add("R");
            add("R");
            add("S");
            add("R");
        }};
        
        List<String> sampleO = new ArrayList<String>() {{
            add("U");
            add("D");
            add("U");
            add("U");
            add("D");
            add("U");
            add("U");
            add("D");
            add("U");
            add("U");
            add("D");
            add("U");
        }};

        System.out.println(hmm.evaluateUsingBruteForce(sampleStates, sampleO));
        System.out.println(hmm.evaluateUsingForwardAlgorithm(sampleStates, sampleO));
        System.out.println(hmm.evaluateUsingForward_Backward(sampleStates, sampleO));
        System.out.println(hmm.getOptimalStateSequenceUsingViterbiAlgorithm(states, sampleO));
        hmm.estimateParametersUsingBaumWelchAlgorithm(states, sampleO, false);
        System.out.println(hmm.getInitialProbabilities());
        System.out.println(hmm.getTransitionMatrix());
        System.out.println(hmm.getEmissionMatrix());
        System.out.println(hmm.evaluateUsingBruteForce(sampleStates, sampleO));
        System.out.println(hmm.evaluateUsingForwardAlgorithm(sampleStates, sampleO));

    }

}
