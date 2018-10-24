package com.ayu.ml.viterbi;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class ViterbiTest {

	@Test
	public void test() {

        try {
            Model model = getGeneSequenceModel();
            List<Observation> observationSequence = getGeneObservationSequence();

            for (int i=1; i<=10; i++) {
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	private static List<Observation> getGeneObservationSequence() throws IOException {
        ObservationFactory factory = new FastaObservationFactory();
        List<Observation> observationSequence = factory.getObservationSequence("data/viterbi/NC_000909.fna");
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
}
