package com.ayu.ml.viterbi;

import java.util.*;

/**
 * Path represents a way to navigate through a sequence of actual {@link Observation}s by changing {@State}s at
 * particular intervals. Each group of observations where the path remains in a state is represented by a
 * {@link Segment}. Note that the optimal path is generally the most interesting, but a path doesn't necessarily
 * have to be optimal.
 */
public class Path {

    /**
     * The list of segments that make up the path. The number of segments is equal to the number of state changes.
     */
    private List<Segment> segments;

    /**
     * The sequence of actual observations that was used to generate the path.
     */
    private List<Observation> observationSequence;

    /**
     * The log likelihood of the path, with respect to the sequence of actual observations.
     */
    private double logLikelihood;

    /**
     * Creates an empty Path.
     */
    public Path() {
        this.observationSequence = new ArrayList<Observation>();
        this.segments = new ArrayList<Segment>();
        this.logLikelihood = Double.MIN_VALUE;
    }

    /**
     * Creates a Path with state.
     * @param observationSequence the observation sequence
     * @param logLikelihood the log likelihood
     */
    public Path(List<Observation> observationSequence, double logLikelihood) {
        this.observationSequence = observationSequence;
        this.segments = new ArrayList<>();
        this.logLikelihood = logLikelihood;
    }

    /**
     * Creates a new path by filtering all segments from the current path where the state identifier matches a string.
     * Convenient for seeing only particular states in a path.
     * @param stateIdentifier the string to match against the state identifier
     * @return the new path
     */
    public Path filter(String stateIdentifier) {
        Path path = new Path(observationSequence, logLikelihood);
        for (Segment segment : getSegments()) {
            if (segment.getState().getIdentifier() == stateIdentifier) {
                path.addSegment(segment);
            }
        }

        return path;
    }

    /**
     * Gets the path segments.
     * @return the segments
     */
    public List<Segment> getSegments() {
        return segments;
    }

    /**
     * Sets the path segments.
     * @param segments the path segments
     */
    public void setSegments(List<Segment> segments) {
        this.segments = segments;
        Collections.sort(segments);
    }

    /**
     * Adds an individual segment to the path.
     * @param segment the segment
     */
    public void addSegment(Segment segment) {
        segments.add(segment);
        Collections.sort(segments);
    }

    /**
     * Gets the transition probabilities based on the path. Useful for running Viterbi Training.
     * @return the transition probabilities, where each key is a list of to and from states
     */
    public Map<List<Object>, Double> getTransitionProbabilities() {

        // Iterate over each segment.
        Map<List<Object>, Integer> transitionCounts = new HashMap<List<Object>, Integer>();
        Segment lastSegment = null;
        Map<State, Integer> totalCounts = new HashMap<State, Integer>();
        for (Segment segment : getSegments()) {
            State state = segment.getState();

            // If this isn't the first segment, store the state transition.
            if (lastSegment != null) {
                State lastState = lastSegment != null ? lastSegment.getState() : null;
                List<Object> key = Arrays.asList((Object)lastState, state);

                int totalCount = transitionCounts.containsKey(key) ? transitionCounts.get(key) + 1 : 1;
                transitionCounts.put(key, totalCount);
            }

            // Store the transitions within a single state.
            int count = segment.getLastObservation().getIndex() - segment.getFirstObservation().getIndex();
            if (count > 0) {
                List<Object> key = Arrays.asList((Object)state, state);

                int totalCount = transitionCounts.containsKey(key) ? transitionCounts.get(key) + count : count;
                transitionCounts.put(key, totalCount);
            }

            lastSegment = segment;

            // Keep track of the total count for finding relative probabilities.
            int totalCount = totalCounts.containsKey(state) ? totalCounts.get(state) + count + 1 : count + 1;
            totalCounts.put(state, totalCount);
        }

        // Calculate probabilities based on counts.
        Map<List<Object>, Double> transitionProbabilities = new HashMap<List<Object>, Double>();
        for (List<Object> key : transitionCounts.keySet()) {
            State state = (State)key.get(0);
            double probability = (double)transitionCounts.get(key) / (double)totalCounts.get(state);
            transitionProbabilities.put(key, probability);
        }

        return transitionProbabilities;
    }

    /**
     * Gets the transition probabilities based on the path. Useful for running Viterbi Training.
     * @return the transition probabilities, where each key is a list of to and from states
     */

    /**
     * Gets the emission probabilities based on the path.  Userful for running Viterbi Training.
     * @return the emission probabilites, where each key is a list of state followed by observation
     */
    public Map<List<Object>, Double> getEmissionProbabilities() {

        // Iterate over each segment.
        Map<List<Object>, Integer> emissionCounts = new HashMap<List<Object>, Integer>();
        Map<State, Integer> totalCounts = new HashMap<State, Integer>();
        for (Segment segment : getSegments()) {
            State state = segment.getState();

            // Iterate over each observation in the segment and add it to the count.
            for (int i = segment.getFirstObservation().getIndex(); i < segment.getLastObservation().getIndex(); i++) {
                Observation observation = observationSequence.get(i);
                List<Object> key = Arrays.asList((Object)state, observation);

                int emissionCount = emissionCounts.containsKey(key) ?  emissionCounts.get(key) + 1 : 1;
                emissionCounts.put(key, emissionCount);

                // Keep track of the total count for finding relative probabilities.
                int totalCount = totalCounts.containsKey(state) ? totalCounts.get(state) + 1 : 1;
                totalCounts.put(state, totalCount);
            }
        }

        // Calculate probabilities based on counts.
        Map<List<Object>, Double> emissionProbabilities = new HashMap<List<Object>, Double>();
        for (List<Object> key : emissionCounts.keySet()) {
            State state = (State)key.get(0);
            double probability = (double)emissionCounts.get(key) / (double)totalCounts.get(state);
            emissionProbabilities.put(key, probability);
        }

        return emissionProbabilities;
    }

    /**
     * Gets the sequence of actual observations.
     * @return the sequence of actual observations
     */
    public List<Observation> getObservationSequence() {
        return observationSequence;
    }

    /**
     * Sets the sequence of actual observations.
     * @param observationSequence the sequence of actual observations
     */
    public void setObservationSequence(List<Observation> observationSequence) {
        this.observationSequence = observationSequence;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Segment segment : getSegments()) {
            sb.append(i + ". " + segment.toString() + System.lineSeparator());
            i++;
        }

        sb.append(System.lineSeparator());
        sb.append("Log Likelihood: " + getLogLikelihood());
        return sb.toString();
    }

    /**
     * Gets the log likelihood ratio.
     * @return the log likelihood ratio
     */
    public double getLogLikelihood() {
        return logLikelihood;
    }

    /**
     * Sets the log likelihood ratio.
     * @param logLikelihood the log likelihood ratio
     */
    public void setLogLikelihood(double logLikelihood) {
        this.logLikelihood = logLikelihood;
    }

    /**
     * Gets the path as a verbose string including each observation and state. Useful for debugging.
     * @return the verbose string
     */
    public String toVerboseString() {

        StringBuilder sb = new StringBuilder();
        StringBuilder osb = new StringBuilder();
        StringBuilder ssb = new StringBuilder();

        int j = 0;
        for (Segment segment : getSegments()) {
            for (int i = segment.getFirstObservation().getIndex(); i <= segment.getLastObservation().getIndex(); i++) {
                Observation observationFromSequence = getObservationSequence().get(i);

                osb.append(observationFromSequence.toChar());
                ssb.append(segment.toChar());

                if (j >= 50) {
                    sb.append(osb.toString() + System.lineSeparator());
                    sb.append(ssb.toString() + System.lineSeparator());

                    osb = new StringBuilder();
                    ssb = new StringBuilder();

                    sb.append(System.lineSeparator());
                    j = 0;
                }

                j++;
            }
        }

        sb.append(osb.toString() + System.lineSeparator());
        sb.append(ssb.toString() + System.lineSeparator());

        return sb.toString();
    }
}
