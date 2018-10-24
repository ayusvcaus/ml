package com.ayu.ml.markov;
import com.ayu.ml.markov.Validator;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import javafx.util.Pair;

import java.util.*;

public class HMM_For_Testing {
    private String name;
    private int numberOfStates;
    private int numberOfObservations;
    private List<String> states;
    private List<String> observations;
    private Map<String, Double> initialProbabilities;
    private Map<Pair<String, String>, Double> transitionMatrix;
    private Map<Pair<String, String>, Double> emissionMatrix;

    /**
     * A constructor that initialize the class attributes
     * @param states A List that is the states of the model
     * @param observations  A List that is the observations of the model
     * @param initialProbabilities A Map that is the initial probability List of the states
     * @param transitionMatrix A Map the transition matrix between the states
     * @param emissionMatrix A Map that is the emission matrix between the states and the observations
     */

    public HMM_For_Testing(String name, List<String> states, List<String> observations, Map<String, Double> initialProbabilities, Map<Pair<String, String>, Double> transitionMatrix, Map<Pair<String, String>, Double> emissionMatrix) throws Exception {
        this.name = name;
        this.states = states;
        this.numberOfStates = states.size();
        this.observations = observations;
        this.numberOfObservations = observations.size();

        this.initialProbabilities = initialProbabilities;
        if (!this.validateInitialProbability(initialProbabilities))
            throw new Exception("Initial Probabilities sum must be equal 1.0");
        if (!this.validateInitialProbabilitiesAndStates(states, initialProbabilities))
            throw new Exception("States size and Initial Probabilities size must be equal");

        this.transitionMatrix = transitionMatrix;
        if (!this.validateTransitionMatrix(transitionMatrix, states))
            throw new Exception("Check the transition matrix elements");

        this.emissionMatrix = emissionMatrix;
        if (!this.validateEmissionMatrix(emissionMatrix, states, observations))
            throw new Exception("Check the emission matrix elements");
    }

    public HMM_For_Testing(String filepath) {

    }

    /**
     *
     * @param initialProbabilities A Map that is the initial probability List of the states
     * @return [True/False] which specifies if the List elements are logically right or not
     */

    private boolean validateInitialProbability(Map<String, Double> initialProbabilities) {
        return Validator.summationIsOne(initialProbabilities);
    }

    /**
     *
     * @param states A List<String> that is the states of the model
     * @param initialProbabilities A Map that is the initial probability List of the states
     * @return [True/False] which specifies if the sizes are matched or not
     */

    private boolean validateInitialProbabilitiesAndStates(List<String> states, Map<String, Double> initialProbabilities) {
        return Validator.isValidInitialProbabilities(states, initialProbabilities);
    }

    /**
     *
     * @param transitionMatrix A Map that is the transition matrix between the states
     * @param states A List that is the states of the model
     * @return [True/False] which specifies if the matrix elements are logically right or not
     */

    private boolean validateTransitionMatrix(Map<Pair<String, String>, Double> transitionMatrix, List<String> states) {
        return Validator.isValidTransitionMatrix(transitionMatrix, states);
    }

    /**
     *
     * @param emissionMatrix A Map that is the emission matrix between the states and the observations
     * @param states A List that is the states of the model
     * @param observations A List that is the model observations
     * @return [True/False] True/False which specifies if the matrix elements are logically right or not
     */

    private boolean validateEmissionMatrix(Map<Pair<String, String>, Double> emissionMatrix, List<String> states, List<String> observations) {
        return Validator.isValidEmissionMatrix(emissionMatrix, states, observations);
    }

    /**
     * Get the number of states in the model
     * @return An integer that specifies the number of states in the model
     */

    public int getNumberOfStates() {
        return this.numberOfStates;
    }

    /**
     * Get the model states
     * @return A List which is the states of the model
     */

    public List<String> getStates() {
        return states;
    }

    /**
     * Set the number of states in the model
     * @param numberOfStates integer
     */

    public void setNumberOfStates(int numberOfStates) {
        this.numberOfStates = numberOfStates;
    }

    /**
     * Get the number of observations in the model
     * @return An integer that specifies the number of observations in the model
     */

    public int getNumberOfObservations() {
        return numberOfObservations;
    }

    /**
     * Get the model observations
     * @return A List which is the observations of the model
     */
    public List<String> getObservations() { return observations; }

    /**
     * Set the number of observations in the model
     * @param numberOfObservations An integer that specifies the number of observations in the model
     */

    public void setNumberOfObservations(int numberOfObservations) {
        this.numberOfObservations = numberOfObservations;
    }

    /**
     * Get the initial probability List of the states
     * @return Map that is the initial probability List of the states
     */

    public Map<String, Double> getInitialProbabilities() {
        return initialProbabilities;
    }

    /**
     * Set the initial probability List of the states
     * @param initialProbabilities Map that is the initial probability List of the states
     */

    public void setInitialProbabilities(Map<String, Double> initialProbabilities) {
        this.initialProbabilities = initialProbabilities;
    }

    /**
     * Get the transition matrix between the states
     * @return Map that is the transition matrix between the states
     */

    public Map<Pair<String, String>, Double> getTransitionMatrix() {
        return transitionMatrix;
    }

    /**
     * Set the transition matrix between the states
     * @param transitionMatrix Map that is the transition matrix between the states
     */

    public void setTransitionMatrix(Map<Pair<String, String>, Double> transitionMatrix) {
        this.transitionMatrix = transitionMatrix;
    }

    /**
     * Get the emission matrix between the states and the observations
     * @return Map that is the emission matrix between the states and the observations
     */

    public Map<Pair<String, String>, Double> getEmissionMatrix() {
        return emissionMatrix;
    }

    /**
     * Set the emission matrix between the states and the observations
     * @param emissionMatrix Map that is the emission matrix between the states and the observations
     */

    public void setEmissionMatrix(Map<Pair<String, String>, Double> emissionMatrix) {
        this.emissionMatrix = emissionMatrix;
    }

    /**
     *
     * @param firstState A string that is a state in the model
     * @param secondState A string that is a state in the model
     * @return A Double that is the transition value between the 2 states
     */

    public Double getTransitionValue(String firstState, String secondState) {
        return this.transitionMatrix.get(new Pair<String, String>(firstState, secondState));
    }

    /**
     *
     * @param state A string that is a state in the model
     * @param observation A string that is an observation in the model
     * @return A Double that is the value of the emission between the state and the observation
     */

    public Double getEmissionValue(String state, String observation) {
        return this.emissionMatrix.get(new Pair<String, String>(state, observation));
    }

    /**
     *
     * @param state A string that is a state in the model
     * @return A Double that is the initial probability value of the state
     */

    public Double getInitialProbability(String state) {
        return this.initialProbabilities.get(state);
    }

    /**
     * Calculate the probability to obtain this sequence of states and observations which is the Evaluation of the model
     * @param states A List which is the sequence of model states
     * @param observations A List which is the sequence of the model observations
     * @return A Double The probability to get this sequence of states and observations
     * @throws Exception The sizes of states and observations sequences must be the same.
     */

    public double evaluateUsingBruteForce(List<String> states, List<String> observations) throws Exception {
        if (states.size() != observations.size())
            throw new Exception("States and Observations must be at a same size!");

        String previousState = "";
        double probability = 0.0;
        double result = 0.0;

        for (int i = 0; i < states.size(); i++) {
            probability = this.getInitialProbability(states.get(i));
            previousState = "";
            for (int j = 0; j < observations.size(); j++) {
                double emissionValue = this.getEmissionValue(states.get(j), observations.get(j));
                double transitionValue = 0.0;
                if (j != 0) {
                    transitionValue += this.getTransitionValue(previousState, states.get(j));
                    probability *= transitionValue * emissionValue;
                }
                previousState = states.get(j);
            }
            result += probability;
        }

        return result;
    }

    /**
     * Calculate the probability to obtain this sequence of states and observations which is the Evaluation of the model
     * @param states A List which is the sequence of model states
     * @param observations A List which is the sequence of the model observations
     * @return A Double The probability to get this sequence of states and observations
     * @throws Exception The sizes of states and observations sequences must be the same.
     */

    public double evaluateUsingForward_Backward(List<String> states, List<String> observations) throws Exception {
        if (observations.size() != states.size()) {
            throw new Exception("States and Observations must be at a same size");
        }

        double result = 0.0;

        List<Map<String, Double>> alpha = this.calculateForwardProbabilities(states, observations);
        // alpha = StatisticalOperations.normalize(alpha, states);
        List<Map<String, Double>> beta = this.calculateBackwardProbabilities(states, observations);
        //beta = StatisticalOperations.normalize(beta, states);

        for (int t = 0; t < states.size(); t++) {
            for (int i = 0; i < alpha.size(); i++) {
                result += (alpha.get(t).get(states.get(i)) * beta.get(t).get(states.get(i)));
            }
        }

        return result;
    }

    /**
     * Calculate the forward probabilities Alpha as a part of Forward-Backward algorithm https://en.wikipedia.org/wiki/Forward%E2%80%93backward_algorithm
     * @param states A List that is the model states
     * @param observations A List that is the model observations
     * @return A List which contains the alpha values
     */

    public List<Map<String, Double>> calculateForwardProbabilities(List<String> states, List<String> observations) {
        List<Map<String, Double>> alpha = new ArrayList<Map<String, Double>>();
        alpha.add(new HashMap<>());
        double sum1 = 0.0;
        for(int i = 0; i < states.size(); i++) {
            alpha.get(0).put(states.get(i), this.getInitialProbability(states.get(i)) * this.getEmissionValue(states.get(i), observations.get(0)));
            sum1 += this.getInitialProbability(states.get(i)) * this.getEmissionValue(states.get(i), observations.get(0));
        }

        for(int i = 0; i < states.size(); i++) {
            alpha.get(0).put(states.get(i), alpha.get(0).get(states.get(i)) * (1 / sum1));
        }

        sum1 = 0.0;
        for (int t = 1; t < states.size(); t++) {
            alpha.add(new HashMap<>());
            for (int i = 0; i < states.size(); i++) {
                double probability = 0.0;
                for (int j = 0; j < states.size(); j++) {
                    probability += alpha.get(t - 1).get(states.get(j)) * this.getTransitionValue(states.get(j), states.get(i));
                }
                alpha.get(t).put(states.get(i), probability * this.getEmissionValue(states.get(i), observations.get(t)));
                sum1 += probability * this.getEmissionValue(states.get(i), observations.get(t));
            }
        }

        for (int t = 1; t < states.size(); t++) {
            for (int i = 0; i < states.size(); i++) {
                alpha.get(t).put(states.get(i), alpha.get(t).get(states.get(i)) * (1 / sum1));
            }
        }

        return alpha;
    }

    /**
     * Calculate the backward probabilities Beta as a part of Forward-Backward algorithm https://en.wikipedia.org/wiki/Forward%E2%80%93backward_algorithm
     * @param states A List that is the model states
     * @param observations A List that is the model observations
     * @return A List which contains the Beta values
     */

    public List<Map<String, Double>> calculateBackwardProbabilities(List<String> states, List<String> observations) {
        List<Map<String, Double>> beta = new ArrayList<>();
        beta.add(new HashMap<>());
        double sum1 = 0.0;

        for (int i = 0; i < states.size(); i++) {
            beta.get(0).put(states.get(i), 1.0);
            sum1 += 1.0;
        }

        for (int i = 0; i < states.size(); i++) {
            beta.get(0).put(states.get(i), beta.get(0).get(states.get(i)) * (1 / sum1));
        }

        sum1 = 0.0;

        for (int t = states.size() - 2; t >= 0; t--) {
            beta.add(0, new HashMap<>());
            for (int i = 0; i < states.size(); i++) {
                double probability = 0.0;
                for (int j = 0; j < states.size(); j++) {
                    probability += beta.get(1).get(states.get(j)) * this.getEmissionValue(states.get(j),
                            observations.get(t)) * this.getTransitionValue(states.get(i), states.get(j));
                }
                beta.get(0).put(states.get(i), probability);
                sum1 += probability;
            }
        }

        for (int t = states.size() - 2; t >= 0; t--) {
            for (int i = 0; i < states.size(); i++) {
                beta.get(0).put(states.get(i), beta.get(0).get(states.get(i)) * (1 / sum1));
            }
        }

        return beta;
    }
}