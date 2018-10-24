package com.ayu.ml.viterbi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * FastaObservationFactory is a factory that gets an actual sequence of nucleotides from a genetic sequence that
 * is represented in the FASTA format, and represents the observations as strings with one of the 4 characters
 * representing each possible base.
 */
public class FastaObservationFactory implements ObservationFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Observation> getObservationSequence(URL url) throws IOException {
        String content = getContent(url);
        List<Observation> observations = new ArrayList<Observation>(content.length());
        int i = 0;
        for (char c : content.toCharArray()) {
            if ((c == 'a') || (c == 'c') || (c == 'g') || (c == 't')) {
                observations.add(new BasicObservation(String.valueOf(c), i));
            }

            else {
                observations.add(new BasicObservation(String.valueOf('t'), i));
            }

            i++;
        }

        return observations;
    }

    /**
     * Gets the content of a remote document that is addressed by URL by making an HTTP request.
     * @param url the url
     * @return the content as a string
     * @throws IOException
     */
    private String getContent(URL url) throws IOException {
        InputStreamReader isr = new InputStreamReader(url.openStream());
        BufferedReader br = new BufferedReader(isr);

        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            if ((line.length() > 0) && (line.charAt(0) != '>')) {
                line = line
                        .toLowerCase()
                        .replaceAll("\\s", "");

                sb.append(line);
            }
        }

        return sb.toString();
    }
    
    @Override
    public List<Observation> getObservationSequence(String file) throws IOException {
        String content = getContent(file);
        List<Observation> observations = new ArrayList<Observation>(content.length());
        int i = 0;
        for (char c : content.toCharArray()) {
            if ((c == 'a') || (c == 'c') || (c == 'g') || (c == 't')) {
                observations.add(new BasicObservation(String.valueOf(c), i));
            }

            else {
                observations.add(new BasicObservation(String.valueOf('t'), i));
            }

            i++;
        }

        return observations;
    }
    
    private String getContent(String file) throws IOException {
        ///InputStreamReader isr = new InputStreamReader(file);
        BufferedReader br = new BufferedReader(new FileReader(file));
        //BufferedReader br = new BufferedReader(isr);

        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            if ((line.length() > 0) && (line.charAt(0) != '>')) {
                line = line
                        .toLowerCase()
                        .replaceAll("\\s", "");

                sb.append(line);
            }
        }

        return sb.toString();
    }
}
