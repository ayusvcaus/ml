package com.ayu.ml.ann;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NeuronTest {

    @Test
    public void createNewNeuron() {
        Neuron neuron = new Neuron();
        assertNotNull(neuron);
    }

    @Test
    public void NeuronValue() {
        double value = new Neuron().getValue();
        assertEquals(value, 0.0);
    }

    @Test
    public void NeuronValueConstructor() {
        double value = new Neuron(5).getValue();
        assertEquals(value, 5.0);
    }

    @Test
    public void NeuronValueConstructorWithSigmoid() {
        Neuron neuron = new Neuron(5, new LogSigmoid(0, 1));
        assertEquals(neuron.getValue(), 5.0);
        assertTrue(neuron.getSigmoid() instanceof LogSigmoid);
    }

    @Test
    public void neuronWeight() {
        Neuron neuron = new Neuron();
        neuron.setError(5.0);

        assertEquals(5.0, neuron.getError());
    }

    @Test
    public void neuronLinkTo() {
        Neuron neuron1 = new Neuron();
        Neuron neuron2 = new Neuron();

        neuron1.linkTo(neuron2);

        assertEquals(neuron1.linkCount(), 0);
        assertEquals(neuron2.linkCount(), 1);
    }

    @Test
    public void neuronGetLinkFrom() {
        Neuron neuron1 = new Neuron();
        Neuron neuron2 = new Neuron();
        Neuron neuron3 = new Neuron();

        neuron1.linkTo(neuron2);
        neuron3.linkTo(neuron2);

        assertEquals(neuron2.linkCount(), 2);
        assertNotNull(neuron2.getLinkFrom(neuron3));
        assertNull(neuron2.getLinkFrom(neuron2));
    }

    @Test
    public void neuronLinkFrom() {
        Neuron neuron1 = new Neuron();
        Neuron neuron2 = new Neuron();

        neuron1.linkFrom(neuron2);

        assertEquals(neuron1.linkCount(), 1);
        assertEquals(neuron2.linkCount(), 0);
    }

    @Test
    public void neuronPass() {
        Neuron neuron1 = new Neuron();
        Neuron neuron2 = new Neuron();
        Neuron target = new Neuron();

        neuron1.setValue(5);
        neuron2.setValue(9);

        neuron1.linkTo(target);
        neuron2.linkTo(target);

        assertTrue(target.getValue() == 0);

        target.pass();

        assertFalse(target.getValue() == 0);
    }

}
