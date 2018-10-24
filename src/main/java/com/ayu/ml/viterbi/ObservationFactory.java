package com.ayu.ml.viterbi;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * ObservationFactory represents a way to get a sequence of actual {@link Observation}s.
 */
public interface ObservationFactory {

    /**
     * Gets a sequence of actual observations by parsing a URL where the data can be fetched. The current interface
     * is fairly tightly coupled to the FASTA use case of parsing genetic nucleotide sequences or any other case
     * where the observations are addressed by a URL that is reachable, and a more generic way to pass in context
     * about the observation source would be desirable.
     * @param url the url
     * @return the sequence of observations
     * @throws IOException
     */
    public  List<Observation> getObservationSequence(URL url) throws IOException;
    public  List<Observation> getObservationSequence(String file) throws IOException;

}
