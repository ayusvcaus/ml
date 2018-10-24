package com.ayu.ml.viterbi;

/**
 * BasicObservation provides a straight forward, in memory representation of an {@link Observation}.
 */
public class BasicObservation implements Observation {

    /**
     * The payload which is effectively wrapped by the observation. For the basic implementation the payload is only
     * used by getting the string representation and performing string comparison.
     */
    private Object payload;

    /**
     * The index of the observation, with respect to a sequence of actual observations.
     */
    private int index;

    /**
     * Creates an empty BasicObservation.
     */
    public BasicObservation() {
        payload = null;
    }

    /**
     * Creates a possible BasicObservation with a payload.
     * @param payload the payload
     */
    public BasicObservation(Object payload) {
        this.payload = payload;
    }

    /**
     * Creates an actual BasicObservation with a payload and index.
     * @param payload the payload
     * @param index the index
     */
    public BasicObservation(Object payload, int index) {
        this.payload = payload;
        this.index = index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getPayload() {
        return payload;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPayload(Object payload) {
        this.payload = payload;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        return hashCode() == o.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getPayload().toString().toLowerCase().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndex() {
        return index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char toChar() {
        Object payload = getPayload();
        if (payload == null) {
            return ' ';
        }

        return getPayload().toString().toLowerCase().charAt(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getPayload().toString();
    }
}
