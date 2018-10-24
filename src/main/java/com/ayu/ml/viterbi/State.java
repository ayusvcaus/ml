package com.ayu.ml.viterbi;

import java.util.Map;

/**
 * State represents a hidden variable with respect to a {@link Model}. For example in a dishonest casino that
 * occasionally uses loaded dice, the state could be whether a fair or loaded die was being used for a particular
 * set of roles.
 */
public interface State {

    String getIdentifier();

    public void setIdentifier(String identifier);

    /**
     * Gets the list of the probabilities of transitioning to any other state.
     * @return the probabilities, as a map that correlates each state to the probability of transition
     */
    public Map<State, Double> getTransitionProbabilities();

    /**
     * Sets the list of probabilities of transitioning to any other {@link State}.
     * @param transitionProbabilities the probabilities
     */
    public void setTransitionProbabilities(Map<State, Double> transitionProbabilities);

    /**
     * Gets the probability of transitioning to a specific state. Provided in addition to raw access to the list of
     * all transition probabilities to add special cased logic, for example it's often desirable to return some
     * small non-zero probability even if the transition to the state isn't in the list of transitions.
     * @param state the state
     * @return the probability
     */
    public Double getTransitionProbability(State state);

    /**
     * Adds an individual probability of transitioning to a state.
     * @param state the state
     * @param probability the probability
     */
    public void addTransitionProbability(State state, Double probability);

    /**
     * Gets the list of the probabilities of emitting each possible {@link Observation} from the state.
     * @return the probabilities, as a map that correlates each observation to the probability of emission
     */
    public Map<Observation, Double> getEmissionProbabilities();

    /**
     * Sets the list of probabilities of emitting any possible {@link Observation}.
     * @param emissionProbabilities the probabilities
     */
    public void setEmissionProbabilities(Map<Observation, Double> emissionProbabilities);

    /**
     * Gets the probability of emitting a specific observation. Provided in addition to raw access to the list of
     * all emission probabilities to add special cased logic, for example it's often desirable to return some
     * small non-zero probability even if the observation isn't in the list of emissions.
     * @param observation the observation
     * @return the probability
     */
    public Double getEmissionProbability(Observation observation);

    /**
     * Adds the probability of emitting an observation.
     * @param observation the observation
     * @param probability the probability
     */
    public void addEmissionProbability(Observation observation, Double probability);

    /**
     * Clears the transition and emission probabilities from the state, for example so that they can be updated
     * as part of a Viberbi Training iteration.
     */
    public void clear();
}
