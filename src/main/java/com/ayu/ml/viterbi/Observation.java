package com.ayu.ml.viterbi;

/**
 * Observation represents a single possible or actual observation, with respect to a {@link Model}.  For example in
 * a dishonest casino that occasionally uses loaded dice, a the possible observations could be the numbers 1-6
 * being rolled and a sequence of actual observations could be a real list of numbers that were rolled.
 */
public interface Observation {

    /**
     * Gets the observation payload.
     * @return the payload
     */
	public Object getPayload();

    /**
     * Sets the observation payload.
      * @param o the payload
     */
	public void setPayload(Object o);

    /**
     * Gets a character representation of the observation, generally to be used when printing the observation out in
     * shorthand form as part of a long list.
     * @return a character representation of the observation
     */
	public char toChar();

    /**
     * The index of the observation in a sequence of observations. Note that the index will only be set for an
     * observation if it is an actual observation in a sequence, not a possible observation.
     * @return the index
     */
	public int getIndex();

    /**
     * Sets the index of an observation in a sequence of observations.
     * @param index the index
     */
	public void setIndex(int index);
}
