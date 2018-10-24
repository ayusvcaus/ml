package com.ayu.ml.markov;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import javafx.util.Pair;

import com.ayu.ml.markov.StatisticalOperations;
import com.ayu.ml.markov.Validator;

public class HiddenMarkovModel {
    private String name;
    private int numberOfStates;
    private int numberOfObservations;
    private List<String> states;
    private List<String> observations;
    private Map<String, Double> initialProbabilities;
    private Map<Pair<String, String>, Double> transitionMatrix;
    private Map<Pair<String, String>, Double> emissionMatrix;
    private List<Map<String, Double>> alpha;
    private List<Map<String, Double>> beta;

    /**
     * A constructor that initialize the class attributes
     * @param states A List that is the states of the model
     * @param observations  A List that is the observations of the model
     * @param initialProbabilities A Map that is the initial probability List of the states
     * @param transitionMatrix A Map the transition matrix between the states
     * @param emissionMatrix A Map that is the emission matrix between the states and the observations
     */

    public HiddenMarkovModel(String name, List<String> states, List<String> observations, Map<String, Double> initialProbabilities, Map<Pair<String, String>, Double> transitionMatrix, Map<Pair<String, String>, Double> emissionMatrix) throws Exception {
        this.name = name;
        this.states = states;
        this.numberOfStates = states.size();
        this.observations = observations;
        this.numberOfObservations = observations.size();

        this.initialProbabilities = initialProbabilities;
        if (!Validator.summationIsOne(initialProbabilities))
            throw new Exception("Initial Probabilities sum must be equal 1.0");
        
        if (!Validator.isValidInitialProbabilities(states, initialProbabilities))
            throw new Exception("States size and Initial Probabilities size must be equal");

        this.transitionMatrix = transitionMatrix;
        if (!Validator.isValidTransitionMatrix(transitionMatrix, states))
            throw new Exception("Check the transition matrix elements");

        this.emissionMatrix = emissionMatrix;
        if (!Validator.isValidEmissionMatrix(emissionMatrix, states, observations))
            throw new Exception("Check the emission matrix elements");

        alpha = new ArrayList<>();
        beta = new ArrayList<>();
    }

    /**
     * Get the model name
     * @return A String that is the model
     */

    public String getName() {
        return name;
    }

    /**
     * Get the number of states in the model
     * @return An integer that specifies the number of states in the model
     */

    public int getNumberOfStates() {
        return numberOfStates;
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
        return transitionMatrix.get(new Pair<>(firstState, secondState));
    }

    /**
     *
     * @param state A string that is a state in the model
     * @param observation A string that is an observation in the model
     * @return A Double that is the value of the emission between the state and the observation
     */

    public Double getEmissionValue(String state, String observation) {
        return emissionMatrix.get(new Pair<>(state, observation));
    }

    /**
     *
     * @param state A string that is a state in the model
     * @return A Double that is the initial probability value of the state
     */

    public Double getInitialProbability(String state) {
        return initialProbabilities.get(state);
    }

    /**
     * Get the Alpha values which is obtained from the forward function
     * @return A Map which represents the Alpha values
     */

    public List<Map<String, Double>> getAlpha() {
        return alpha;
    }

    /**
     * Get the Beta values which is obtained from the backward function
     * @return A Map which represents the Beta values
     */

    public List<Map<String, Double>> getBeta() {
        return beta;
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
            probability = getInitialProbability(states.get(i));
            previousState = "";
            for (int j = 0; j < observations.size(); j++) {
                //double emissionValue = getEmissionValue(states.get(j), observations.get(j));
            	//double emissionValue = emissionMatrix.get(new Pair<>(states.get(j), observations.get(j)));
                //double transitionValue = 0.0;
                if (j != 0) {
                	//transitionValue += transitionValue += getTransitionValue(previousState, states.get(j));
                	//double transitionValue = transitionMatrix.get(new Pair<>(previousState, states.get(j)));
                    //probability *= transitionValue * emissionValue;
                	probability *= transitionMatrix.get(new Pair<>(previousState, states.get(j))) * emissionMatrix.get(new Pair<>(states.get(j), observations.get(j)));
                }
                previousState = states.get(j);
            }
            result += probability;
        }

        return result;
    }

    public double evaluateUsingForwardAlgorithm(List<String> states, List<String> observations) {
        calculateForwardProbabilities(states, observations);
        double res = 0;
        for (int i=0; i<alpha.get(observations.size()-1).size(); i++) {
            res += alpha.get(observations.size() - 1).get(states.get(i));
        }
        return res;
    }

    /**
     * Calculate the probability to obtain this sequence of states and observations which is the Evaluation of the model
     * @param states A List which is the sequence of model states
     * @param observations A List which is the sequence of the model observations
     * @return A Double The probability to get this sequence of states and observations
     * @throws Exception The sizes of states and observations sequences must be the same.
     */

    public List<Double> evaluateUsingForward_Backward(List<String> states, List<String> observations) throws Exception {
        List<Double> resultsList = new ArrayList<>();

        calculateForwardProbabilities(states, observations);
        //alpha = StatisticalOperations.getInstance().normalize(alpha, states); // Normalization
        calculateBackwardProbabilities(states, observations);
       // beta = StatisticalOperations.getInstance().normalize(beta, states); // Normalization

        for (int t=0; t<states.size(); t++) {
            double result = 1.0;
            for (int i=0; i<alpha.size(); i++) {
                result += (alpha.get(t).get(states.get(i)) * beta.get(t).get(states.get(i)));
            }
            resultsList.add(result);
        }
        resultsList = StatisticalOperations.getInstance().normalize(resultsList);
        return resultsList;
    }

    /**
     * Calculate the forward probabilities Alpha as a part of Forward-Backward algorithm https://en.wikipedia.org/wiki/Forward%E2%80%93backward_algorithm
     * @param states A List that is the model states
     * @param observations A List that represents the observations sequence
     * @return A List which contains the alpha values
     */

    public void calculateForwardProbabilities(List<String> states, List<String> observations) {
    	if (alpha.size()<1) {
            alpha.add(new HashMap<>());
    	}
        for (int i=0; i<states.size(); i++) {
           //alpha.get(0).put(states.get(i), getInitialProbability(states.get(i)) * getEmissionValue(states.get(i), observations.get(0)));
           alpha.get(0).put(states.get(i), initialProbabilities.get(states.get(i)) * emissionMatrix.get(new Pair<>(states.get(i), observations.get(0))));
        }
        for (int t=1; t<observations.size(); t++) {
        	if (alpha.size()<=t) {
        		alpha.add(new HashMap<>());
        	}
            for (int i=0; i<states.size(); i++) {
                double probability = 0.0;
                for (int j=0; j<states.size(); j++) {
                    //probability += alpha.get(t - 1).get(states.get(j)) * getTransitionValue(states.get(j), states.get(i));
                	probability += alpha.get(t - 1).get(states.get(j)) * transitionMatrix.get(new Pair<>(states.get(j), states.get(i)));
                }
                //alpha.get(t).put(states.get(i), probability * getEmissionValue(states.get(i), observations.get(t)));
                alpha.get(t).put(states.get(i), probability * emissionMatrix.get(new Pair<>(states.get(i), observations.get(t))));
            }
        }
    }

    /**
     * Calculate the backward probabilities Beta as a part of Forward-Backward algorithm https://en.wikipedia.org/wiki/Forward%E2%80%93backward_algorithm
     * @param states A List that is the model states
     * @param observations A List that represents the observations sequence
     * @return A List which contains the Beta values
     */

    private void calculateBackwardProbabilities(List<String> states, List<String> observations) {
    	if (beta.size()==0) {
            beta.add(new HashMap<>());
    	}
        for (int i=0; i<states.size(); i++) {
            beta.get(0).put(states.get(i), 1.0);
        }

        for (int t=observations.size()-2; t>=0; t--) {
            beta.add(0, new HashMap<>());
            for (int i=0; i<states.size(); i++) {
                double probability = 0.0;
                for (int j=0; j<states.size(); j++) {
                    probability += beta.get(1).get(states.get(j)) * transitionMatrix.get(new Pair<>(states.get(j), states.get(i))) * emissionMatrix.get(new Pair<>(states.get(j), observations.get(t)));
                }
                beta.get(0).put(states.get(i), probability);
            }
        }
    }

    /**
     * Get the most optimal path for states to emit the given observations
     * @param states A List which is the model states
     * @param observations A List which represents the observations
     * @return A String which holds the optimal path and the total cost
     */

    public String getOptimalStateSequenceUsingViterbiAlgorithm(List<String> states, List<String> observations) {
        String path = "";
        List<Map<String, Double>> dpTable = new ArrayList<>();
        Map<String, Double> statesProbabilities = new HashMap<>();
        Map<String, Double> priorProbabilities = new HashMap<>();

        for (int i = 0; i < observations.size(); i++) {
            if (i == 0) {
                for (String state : states) {
                    double initialProbability = getInitialProbability(state);
                    double emissionProbability = getEmissionValue(state, observations.get(i));
                    statesProbabilities.put(state, Math.log(initialProbability) + Math.log(emissionProbability));
                }
            } else {
                for (String state : states) {
                    double emissionProbability = getEmissionValue(state, observations.get(i));
                    double bestProbability = -100000;

                    for (String prevState : priorProbabilities.keySet()) {
                        double transitionProbability = getTransitionValue(prevState, state);
                        double accumulate = priorProbabilities.get(prevState) + Math.log(emissionProbability) + Math.log(transitionProbability);

                        if (accumulate > bestProbability)
                            bestProbability = accumulate;
                    }
                    statesProbabilities.put(state, bestProbability);
                }
            }
            //HashMap<String, Double> obj = (HashMap<String, Double>)statesProbabilities.clone();
            Map<String, Double> obj = new HashMap<>();
            obj.putAll(statesProbabilities);
            dpTable.add(obj);
            priorProbabilities = obj;
        }

        Map<String, Double> lastColumn = dpTable.get(dpTable.size() - 1);
        double totalCost = -1000000;

        for (String item : lastColumn.keySet()) {
            if (lastColumn.get(item) > totalCost) {
                totalCost = lastColumn.get(item);
            }
        }

        for (Map<String, Double> column : dpTable) {
            double costPerColumn = -1000000;
            String targetState = "";
            for (String state : column.keySet()) {
                if (column.get(state) > costPerColumn) {
                    costPerColumn = column.get(state);
                    targetState = state;
                }
            }
            path += targetState + " -> ";
        }

        path += "END with total cost = " + totalCost;

        return path;
    }

    /**
     * Estimate the parameters of HMM which known as the learning approach for HMM
     * @param states A List which is the model states
     * @param observations A List which is the sequence of observations
     * @param additiveSmoothing A boolean which indicates if the function will use the smoothing value or not to avoid zero values.
     */

    public void estimateParametersUsingBaumWelchAlgorithm(List<String> states, List<String> observations, boolean additiveSmoothing) {
        double smoothing = additiveSmoothing ? 1.0 : 0.0;
        calculateForwardProbabilities(states, observations);
        calculateBackwardProbabilities(states, observations);
        List<Map<String, Double>> gamma = new ArrayList<>();

        for (int i = 0; i < observations.size(); i++) {
            gamma.add(new HashMap<>());
            double probabilitySum = 0.0;
            for (String state : states) {
                double product = alpha.get(i).get(state) * beta.get(i).get(state);
                gamma.get(i).put(state, product);
                probabilitySum += product;
            }
            if (probabilitySum == 0)
                continue;

            for (String state : states) {
                gamma.get(i).put(state, gamma.get(i).get(state) / probabilitySum);
            }
        }

        List<Map<String, Map<String, Double>>> eps = new ArrayList<>();

        for (int i = 0; i < observations.size() - 1; i++) {
            double probabilitySum = 0.0;
            eps.add(new HashMap<>());
            for (String fromState : states) {
                eps.get(i).put(fromState, new HashMap<>());
                for (String toState : states) {
                    double tempProbability = alpha.get(i).get(fromState)
                            * beta.get(i + 1).get(toState)
                            * getTransitionValue(fromState, toState)
                            * getEmissionValue(toState, observations.get(i + 1));

                     eps.get(i).get(fromState).put(toState, tempProbability);
                    probabilitySum += tempProbability;
                 }
            }

            if (probabilitySum == 0)
                continue;

            for (String from : states) {
                for (String to : states) {
                    eps.get(i).get(from).put(to, eps.get(i).get(from).get(to) / probabilitySum);
                }
            }
        }

        for (String state : states) {
            double updated = (gamma.get(0).get(state) + smoothing) / (1 + (states.size() * smoothing));
            initialProbabilities.put(state, updated);

            double gammaProbabilitySum = 0.0;
            for (int i = 0; i < observations.size() - 1; i++) {
                gammaProbabilitySum += gamma.get(i).get(state);
            }

            if (gammaProbabilitySum > 0) {
                double denominator = gammaProbabilitySum + smoothing * states.size();
                for (String to : states) {
                    double epsSum = 0.0;
                    for (int i = 0; i < observations.size() - 1; i++) {
                        epsSum += eps.get(i).get(state).get(to);
                        transitionMatrix.put(new Pair<>(state, to), (smoothing + epsSum) / denominator);
                    }
                }
            } else {
                for (String to : states) {
                    transitionMatrix.put(new Pair<String, String>(state, to), 0.0);
                }
            }

            gammaProbabilitySum = 0.0;

            for (int i = 0; i < observations.size(); i++) {
                gammaProbabilitySum += gamma.get(i).get(state);
            }

            Map<String, Double> emissionProbabilitySums = new HashMap<String, Double>();

            for (String observation : observations) {
                emissionProbabilitySums.put(observation, 0.0);
            }

            for (int i = 0; i < observations.size(); i++) {
                emissionProbabilitySums.put(observations.get(i), emissionProbabilitySums.get(observations.get(i)) + gamma.get(i).get(state));
            }

            if (gammaProbabilitySum > 0) {
                double denominator = gammaProbabilitySum + smoothing * observations.size();
                for (String observation : observations) {
                    emissionMatrix.put(new Pair<>(state, observation), (smoothing + emissionProbabilitySums.get(observation)) / denominator);
                }
            } else {
                for (String observation : observations) {
                    emissionMatrix.put(new Pair<>(state, observation), 0.0);
                }
            }
        }
    }
}