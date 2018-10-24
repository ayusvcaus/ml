package com.ayu.ml.markov;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import javafx.util.Pair;

/**
 * Created by Ahmed Hani Ibrahim on 12/20/2015.
 */
public class DataDecoding {
    private static final String OUTER_SPLITTER = ", ";
    private static final String INNER_SPLITTER = "->";

    public static List<String> getStates(String statesInJson) {
        if (null != statesInJson) {
            return Arrays.asList(statesInJson.split(OUTER_SPLITTER));
        }
        return null;
    }

    /**
     * Get the initial probabilities of the states
     * @param initialProbabilitiesInJson A string that hold the json expression of the model initial probabilities
     * @return A Map that is the initial probabilities of the model states
     */

    public static Map<String, Double> getInitialProbabilities(String initialProbabilitiesInJson) {
        Map<String, Double> initialProbabilities = new HashMap<>();

        String[] initialProb = initialProbabilitiesInJson.split(OUTER_SPLITTER);

        for (String expression : initialProb) {
            String[] tempExpression = expression.split(INNER_SPLITTER);
            initialProbabilities.put(tempExpression[0], Double.parseDouble(tempExpression[1]));
        }

        return initialProbabilities;
    }

    /**
     * Get the observations of the model
     * @param observationsInJson A string that hold the json expression of the model observations
     * @return A Map that is the observations of the model
     */

    public static List<String> getObservations(String observationsInJson) {       
        if (null != observationsInJson) {
            return Arrays.asList(observationsInJson.split(OUTER_SPLITTER));
        }
        return null;
    }

    /**
     * Get the transition matrix of the model
     * @param transitionMatrixInJson A string that hold the json expression of the model transition matrix
     * @return A Hasshtable that is the transition matrix of the model
     */

    public static Map<Pair<String, String>, Double> getTransitionMatrix(String transitionMatrixInJson) {
        Map<Pair<String, String>, Double> transitionMatrix = new HashMap<>();
        String[] tempExpressionArray = transitionMatrixInJson.split(OUTER_SPLITTER);

        for (String expression : tempExpressionArray) {
            String[] transitionExpression = expression.split(INNER_SPLITTER);
            transitionMatrix.put(new Pair<String, String>(transitionExpression[0], transitionExpression[1]),
                    Double.parseDouble(transitionExpression[2]));
        }

        return transitionMatrix;
    }

    /**
     * Get the emission matrix
     * @param emissionMatrixInJson A string that hold the json expression of the model emission matrix
     * @return A Hasshtable that is the emission matrix of the model
     */

    public static Map<Pair<String, String>, Double> getEmissionMatrix(String emissionMatrixInJson) {
        Map<Pair<String, String>, Double> emissionMatrix = new HashMap<>();
        String[] tempExpressionArray = emissionMatrixInJson.split(OUTER_SPLITTER);
        for (String expression : tempExpressionArray) {
            String[] emissionExpression = expression.split(INNER_SPLITTER);
            emissionMatrix.put(new Pair<String, String>(emissionExpression[0], emissionExpression[1]),
                    Double.parseDouble(emissionExpression[2]));
        }

        return emissionMatrix;
    }
}
