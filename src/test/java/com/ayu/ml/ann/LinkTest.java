package com.ayu.ml.ann;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

class LinkTest {

    @Test
    public void createLinkBetweenTwoNeurons() {
        assertNotNull(new Link(new Neuron(), new Neuron()));
    }

    @Test
    public void checkLinkDestinations() {
        Link link = new Link(new Neuron(), new Neuron());
        assertNotNull(link.getFrom());
        assertNotNull(link.getTo());
    }

    @Test
    public void getLinkWeight() {
        Link.setWeightRange(-0.3, 0.3);
        Link link = new Link(new Neuron(), new Neuron());
        double weight = link.getWeight();

        assertTrue(weight >= -0.3);
        assertTrue(weight <= 0.3);
    }

    @Test
    public void getLinkWeightWithCustomWeightRange() {
        Link.setWeightRange(1.0, 2.0);
        Link link = new Link(new Neuron(), new Neuron());
        double weight = link.getWeight();

        assertTrue(weight >= 1.0);
        assertTrue(weight <= 2.0);
    }

    @Test
    public void getAndSetLinkWeight() {
        Link link = new Link(new Neuron(), new Neuron());
        link.setWeight(2);
        assertEquals(link.getWeight(), 2);
    }

    @Test
    public void calculateWeightedValue() {
        Link link = new Link(new Neuron(1.5), new Neuron());
        link.setWeight(2);

        assertEquals(link.calculateWeightedValue(), 2 * 1.5);
    }

    @Test
    public void calculateWeightedError() {
        Neuron fromNeuron = new Neuron(1.5);
        fromNeuron.setError(3);
        Link link = new Link(fromNeuron, new Neuron());
        link.setWeight(2);

        assertEquals(link.calculateWeightedError(), 2 * 3);
    }

    @Test
    public void adjustWeight() {
        Link link = new Link(new Neuron(1.5), new Neuron());
        link.setWeight(2);

        link.adjustWeight(0.2, 0.5);

        assertTrue(link.getWeight() == 1.9);
    }
}
