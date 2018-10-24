package com.ayu.ml.markov;

import javafx.util.Pair;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ahmed Hani Ibrahim on 12/19/2015.
 */
public class Validator {

    public static boolean summationIsOne(Map<String, Double> list) {
        double sum = 0.0;

        for (double item : list.values()) {
            sum += item;
        }

        return sum == 1.0;
    }

    public static boolean isValidInitialProbabilities(List<String> states, Map<String, Double> initialProbabilities) {
        if (states.size() != initialProbabilities.size())
            return false;

        for (int i = 0; i < states.size(); i++) {
            boolean found = false;
            for (String state : initialProbabilities.keySet()) {
                if (state.equals(states.get(i))) {
                    found = true;
                    break;
                }
            }

            if (!found)
                return false;
        }

        return true;
    }

    public static boolean isValidTransitionMatrix(Map<Pair<String, String>, Double> transitionMatrix, List<String> states) {
        if (transitionMatrix.size() != states.size() * states.size())
            return false;

        Map<Pair<String, String>, Boolean> frequency = new HashMap<>();

        for (Pair<String, String> item : transitionMatrix.keySet()) {
            if (frequency.containsKey(item)) {
                return false;
            }
            frequency.put(item, true);
        }

        Map<Pair<String, String>, Boolean> visited = new HashMap<>();

        for (Pair<String, String> first : transitionMatrix.keySet()) {
            double sum = 0.0;
            int entered = 0;
            String state = first.getKey();
            for (Pair<String, String> second: transitionMatrix.keySet()) {
                if (state.equals(second.getKey()) && !visited.containsKey(second)) {
                    sum += transitionMatrix.get(second);
                    entered++;
                    visited.put(second, true);
                }
            }

            if (sum != 1.0 && entered > 0) {
                return false;
            }
        }

        return true;
    }

    public static boolean isValidEmissionMatrix(Map<Pair<String, String>, Double> emissionMatrix, List<String> states, List<String> observations) {
        if (emissionMatrix.size() != observations.size() * states.size()) {
            return false;
        }

        for (Pair<String, String> item : emissionMatrix.keySet()) {
            boolean found = false;
            double sum = 0.0;
            int count = 0;
            for (int i = 0; i < states.size(); i++) {
                for (int j = 0; j < observations.size(); j++) {
                    if (item.getKey().equals(states.get(i)) && item.getValue().equals(observations.get(j))) {
                        found = true;
                        break;
                    }
                }

                if (found)
                    break;
            }

            if (!found)
                return false;

            for (Pair<String, String> item2 : emissionMatrix.keySet()) {
                if (item.getKey().equals(item2.getKey())) {
                    sum += emissionMatrix.get(item2);
                    count++;
                }
            }

            if (sum != 1.0 && count > 0)
                return false;
        }

        return true;
    }
}
