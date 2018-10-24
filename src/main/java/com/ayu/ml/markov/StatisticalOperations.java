package com.ayu.ml.markov;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
/**
 * Created by Ahmed Hani Ibrahim on 12/24/2015.
 */
public class StatisticalOperations {
    private static StatisticalOperations ourInstance = new StatisticalOperations();

    public static StatisticalOperations getInstance() {
        return ourInstance;
    }

    private StatisticalOperations() {
    }

    /**
     * Probabilities Normalization
     * @param probabilities A Map which contains the probability values
     * @param states A List which is the model states
     * @return Normalized probabilities as a Map
     */

    public List<Map<String, Double>> normalize(List<Map<String, Double>> probabilities, List<String> states) {
        double sum = 0.0;
        if (states.size() == 1) return probabilities;

        for (int t = 0; t < states.size(); t++) {
            for (int i = 0; i < probabilities.size(); i++) {
                sum += (probabilities.get(t).get(states.get(i)));
            }
        }

        for (int t = 0; t < states.size(); t++) {
            for (int i = 0; i < probabilities.size(); i++) {
                double current = (probabilities.get(t).get(states.get(i)));
                probabilities.get(t).put(states.get(i), current / sum);
            }
        }

        return probabilities;
    }

    public List<Double> normalize(List<Double> data) {
        List<Double> res = new ArrayList<>();
        double sum = 0.0;

        for (int i = 0; i < data.size(); i++) {
            sum += data.get(i);
        }

        for (int i = 0; i < data.size(); i++) {
             res.add(data.get(i) / sum);
        }

        return res;
    }
}