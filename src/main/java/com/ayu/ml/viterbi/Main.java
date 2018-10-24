package com.ayu.ml.viterbi;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Main is the main execution entry point for a lightweight application that demonstrates one use of the Viterbi
 * framwork for finding a Viterbi Path when given a Hidden Markov Model and a set of real observations, as well as
 * the ability to run Viberbi Training on the data.
 */
public class Main {

    /**
     * Main execution entry point. Gets the genetic sequence information for Methanocaldococcus Jannaschii and
     * identifies segments of the genome with high GC content (which generally map to non-coding RNA's) by passing
     * in some initial parameter estimates and running Viterbi Training.
     * @param args the list of arguments, which aren't used
     */
    public static void main(String[] args) {

        try {

            //viterbi.Model model = getDiceModel();
            //List<viterbi.Observation> observationSequence = getDiceObservationSequence();

            Model model = getGeneSequenceModel();
            List<Observation> observationSequence = getGeneObservationSequence();

            for (int i = 1; i <= 1; i++) {
                System.out.println("======================================================");
                System.out.println("Iteration " + i + "");

                System.out.println();
                System.out.println("Model:");
                System.out.println();
                System.out.println(model);

                Path path = model.getPath(observationSequence);
                Path filteredPath = path.filter("GCPatch");

                System.out.println("Path:");
                System.out.println();
                System.out.println(filteredPath);
                System.out.println();

                model.train(path);
            }
        }

        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static List<Observation> getGeneObservationSequence() throws IOException {
        ObservationFactory factory = new FastaObservationFactory();
        //URL url = new URL("ftp://ftp.ncbi.nih.gov/genomes/Bacteria/Methanocaldococcus_jannaschii_DSM_2661_uid57713/NC_000909.fna");
        //URL url = new URL("http://radiop.org.tn/RadioP1/IRRB/Methanocaldococcus_jannaschii_DSM_2661_uid57713/NC_000909.fna");
        //List<Observation> observationSequence = factory.getObservationSequence(url);
        //List<Observation> observationSequence = factory.getObservationSequence("data/viterbi/NC_000909.fna");
        List<Observation> observationSequence = factory.getObservationSequence("data/viterbi/NC_test.fna");
        return observationSequence;
    }

    private static Model getGeneSequenceModel() throws IOException {
    	
    	/*
states = ('Start', 'Background', 'GCPatch')
observations = ('a', 'c', 'g', 't')
start_probability = {'Start': 0.6, 'GCPatch': 0.4}
transition_probability = {
    'Start' : {'Background': .9999d, 'GCPatch': .0001d},
    'Background' : {'Background': .9999d, 'GCPatch': .0001d},
    'GCPatch' : {'Background': .01d, 'GCPatch': .99d},
    } 
emission_probability = {
    'Start' : {'a': .25d, 'c': .25d, 'g': .25d, 't': .25d},
    'Background' : {'a': .2d, 'c': .3d, 'g': .3d, 't': .2d},
}
    	 * 
    	 */

        Observation oa = new BasicObservation("a");
        Observation oc = new BasicObservation("c");
        Observation og = new BasicObservation("g");
        Observation ot = new BasicObservation("t");

        State s0 = new BasicState("Start");
        State s1 = new BasicState("Background");
        State s2 = new BasicState("GCPatch");

        s0.addTransitionProbability(s1, .9999d);
        s0.addTransitionProbability(s2, .0001d);
        s1.addTransitionProbability(s1, .9999d);
        s1.addTransitionProbability(s2, .0001d);
        s2.addTransitionProbability(s1, .01d);
        s2.addTransitionProbability(s2, .99d);

        s1.addEmissionProbability(oa, .25d);
        s1.addEmissionProbability(oc, .25d);
        s1.addEmissionProbability(og, .25d);
        s1.addEmissionProbability(ot, .25d);

        s2.addEmissionProbability(oa, .2d);
        s2.addEmissionProbability(oc, .3d);
        s2.addEmissionProbability(og, .3d);
        s2.addEmissionProbability(ot, .2d);

        Model model = new BasicModel();
        model.addStates(s0, s1, s2);
        model.setInitialState(s0);
        model.addObservations(oa, oc, og, ot);

        return model;
    }
/*
    private static Model getDiceModel() {

        Observation o1 = new BasicObservation(1);
        Observation o2 = new BasicObservation(2);
        Observation o3 = new BasicObservation(3);
        Observation o4 = new BasicObservation(4);
        Observation o5 = new BasicObservation(5);
        Observation o6 = new BasicObservation(6);

        State s0 = new BasicState("Start");
        State sf = new BasicState("Fair");
        State sl = new BasicState("Loaded");

        s0.addTransitionProbability(sf, .48d);
        s0.addTransitionProbability(sl, .52d);
        sf.addTransitionProbability(sf, .83d);
        sf.addTransitionProbability(sl, .17d);
        sl.addTransitionProbability(sf, .4d);
        sl.addTransitionProbability(sl, .6d);

        sf.addEmissionProbability(o1, (double)1/6);
        sf.addEmissionProbability(o2, (double)1/6);
        sf.addEmissionProbability(o3, (double)1/6);
        sf.addEmissionProbability(o4, (double)1/6);
        sf.addEmissionProbability(o5, (double)1/6);
        sf.addEmissionProbability(o6, (double)1/6);

        sl.addEmissionProbability(o1, .1d);
        sl.addEmissionProbability(o2, .1d);
        sl.addEmissionProbability(o3, .1d);
        sl.addEmissionProbability(o4, .1d);
        sl.addEmissionProbability(o5, .1d);
        sl.addEmissionProbability(o6, .5d);

        Model model = new BasicModel();
        model.addStates(s0, sf, sl);
        model.setInitialState(s0);
        model.addObservations(o1, o2, o3, o4, o5, o6);

        return model;
    }

    private static List<Observation> getDiceObservationSequence() {

        List<Observation> observationSequence = new ArrayList<Observation>();

        observationSequence.add(new BasicObservation(3, 0));
        observationSequence.add(new BasicObservation(1, 1));
        observationSequence.add(new BasicObservation(6, 2));
        observationSequence.add(new BasicObservation(6, 3));
        observationSequence.add(new BasicObservation(6, 4));
        observationSequence.add(new BasicObservation(4, 5));

        return observationSequence;
    }
    */
}
