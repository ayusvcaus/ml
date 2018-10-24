package com.ayu.ml.viterbi;

public class Segment implements Comparable {

    /**
     * The state of the segment.
     */
    private State state;

    /**
     * The first observation of the segment.
     */
    private Observation firstObservation;

    /**
     * The last observation of the segment.
     */
    private Observation lastObservation;

    /**
     * Creates an empty Segment.
     */
    public Segment() {
    }

    /**
     * Creates a Segment with state.
     * @param state the state
     * @param firstObservation the first observation
     * @param lastObservation the last observation
     */
    public Segment (State state, Observation firstObservation, Observation lastObservation) {
        this.state = state;
        this.firstObservation = firstObservation;
        this.lastObservation = lastObservation;
    }

    /**
     * Gets the first observation.
     * @return the first observation
     */
    public Observation getFirstObservation() {
        return firstObservation;
    }

    /**
     * Sets the first observation.
     * @param startingObservation the first observation
     */
    public void setFirstObservation(Observation startingObservation) {
        this.firstObservation = startingObservation;
    }

    /**
     * Gets the last observation.
     * @return the last observation
     */
    public Observation getLastObservation() {
        return lastObservation;
    }

    /**
     * Sets the last observation.
     * @param endingObservation the last observation
     */
    public void setLastObservation(Observation endingObservation) {
        this.lastObservation = endingObservation;
    }

    /**
     * Gets a character representation of the segment, generally to be used when printing the observation and segment
     * out in shorthand form as part of a long list.
     * @return a character representation of the segment
     */
    public char toChar() {
        State state = getState();
        if (state == null) {
            return ' ';
        }

        return state.getIdentifier().toLowerCase().charAt(0);
    }

    /**
     * Gets the segment state.
     * @return the state
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the segment state.
     * @param state the state
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Compares the segment to another segment. Used for ordering lists of segments, since part of the Viterbi Path
     * finding algorithm involves identifying segments in reverse order.
     * @param o the other segment
     * @return an integer value that corresponds to segment order
     */
    @Override
    public int compareTo(Object o) {

        Segment s = (Segment)o;

        float a = (getFirstObservation().getIndex() + getLastObservation().getIndex() / 2);
        float b = (s.getFirstObservation().getIndex() + s.getLastObservation().getIndex() / 2);

        if (a>b) {
        	return 1;
        } else if (b>a) {
        	return -1;
        }      
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getState().getIdentifier() + " " + firstObservation.getIndex() + ":" + lastObservation.getIndex() +
                " (" + (lastObservation.getIndex() - firstObservation.getIndex() + 1) + ") ");
        return sb.toString();
    }
}
