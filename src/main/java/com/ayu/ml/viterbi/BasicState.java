package com.ayu.ml.viterbi;

import java.util.HashMap;
import java.util.Map;

/**
 * BasicState provides a straight forward, in memory representation of a {@link State}.
 */
public class BasicState implements State {

    /**
     * Value that is used to uniquely identify a state.
     */
    private String identifier;

    /**
     * The probability of transitioning to any other state.
     */
    private Map<State, Double> transitionProbabilities;

    /**
     * The probability of emitting any observation.
     */
    private Map<Observation, Double> emissionProbabilities;

    /**
     * Creates an empty BasicState.
     */
    public BasicState() {
        clear();
        identifier = null;
    }

    /**
     * Creates a BasicState with an identifier.
     * @param identifier
     */
    public BasicState(String identifier) {
        clear();
        this.identifier = identifier;
    }

    /**
     * Creates a BasicState with state.
     * @param identifier the identifier
     * @param transitionProbabilities the transition probabilities
     * @param emissionProbabilities the emissino probabilities
     */
    public BasicState(String identifier, Map<State, Double> transitionProbabilities, Map<Observation, Double> emissionProbabilities) {

        this.identifier = identifier;
        this.transitionProbabilities = transitionProbabilities;
        this.emissionProbabilities = emissionProbabilities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<State, Double> getTransitionProbabilities() {
        return transitionProbabilities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTransitionProbabilities(Map<State, Double> transitionProbabilities) {
        this.transitionProbabilities = transitionProbabilities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getTransitionProbability(State state) {
        for (State transitionState : getTransitionProbabilities().keySet()) {
            if (transitionState.equals(state)) {
                return getTransitionProbabilities().get(transitionState);
            }
        }

        return Double.MIN_VALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTransitionProbability(State state, Double probability) {
        this.transitionProbabilities.put(state, probability);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Observation, Double> getEmissionProbabilities() {
        return emissionProbabilities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEmissionProbabilities(Map<Observation, Double> emissionProbabilities) {
        this.emissionProbabilities = emissionProbabilities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getEmissionProbability(Observation observation) {
        for (Observation emissionObservation : getEmissionProbabilities().keySet()) {
            if (emissionObservation.equals(observation)) {
                return getEmissionProbabilities().get(emissionObservation);
            }
        }

        return Double.MIN_VALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEmissionProbability(Observation observation, Double probability) {
        emissionProbabilities.put(observation, probability);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        transitionProbabilities = new HashMap<>();
        emissionProbabilities = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return identifier;
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BasicState)) {
			return false;
		}
		BasicState other = (BasicState) obj;
		if (identifier==null) {
			if (other.getIdentifier() != null) {
				return false;
			}
		} else if (!identifier.equals(other.getIdentifier())) {
			return false;
		}		
		return true;
	} 
}
