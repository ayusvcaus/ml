package com.ayu.ml.viterbi;

import java.util.List;

/**
 * Model represents a Hidden Markov Model which contains {@link State}s that correspond to a hidden variable and
 * {@link Observation}s that correspond to observable variables.
 */
public interface Model {

    /**
     * Gets the model states.
     * @return the states
     */
	public List<State> getStates();

    /**
     * Sets the model states.
     * @param states the states
     */
	public void setStates(List<State> states);

    /**
     * Adds one or more states to the model.
     * @param states the states
     */
	public void addStates(State... states);

    /**
     * Gets all states that have transitions in from other states. In many cases this will exclude the initial state.
     * @return a list of states with transitions in
     */
	public List<State> getTransitionStates();

    /**
     * Gets the model observations.
     * @return the observations
     */
	public List<Observation> getObservations();

    /**
     * Sets the model observations.
     * @param observations the observations
     */
	public void setObservations(List<Observation> observations);

    /**
     * Adds one or more observations to the model.
     * @param observations the observations
     */
	public void addObservations(Observation... observations);

    /**
     * Gets the initial state; a special cased state where the model always begins.
     * @return the initial state
     */
	public State getInitialState();

    /**
     * Sets the initial state; a special cased state where the model always begins.
     * @param initialState the initial state
     */
	public void setInitialState(State initialState);

    /**
     * Gets a Viterbi Path through a sequence of observations, given the current parameters.
     * @param observationSequence a sequence of observations
     * @return the optimal path
     */
	public Path getPath(List<Observation> observationSequence);

    /**
     * Trains a model based on the given path. Can be used repeatedly with a new path found each iteration to
     * perform Viterbi Training.
     * @param path the path to learn from
     */
	public void train(Path path);
}
