package com.ayu.ml.ann;

import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.junit.Test;


public class LayerTest {

    @Test
    public void createLayer() {
        assertNotNull(new Layer());
    }

    @Test
    public void emptyLayer() {
        Layer layer = new Layer();
        assertEquals(layer.size(), 0);
    }

    @Test
    public void createLayerAddNeuron() {
        Layer layer = new Layer();

        layer.addNeuron(new Neuron());

        assertEquals(layer.size(), 1);
    }

    @Test
    public void createLayerAddNeurons() {
        Layer layer = new Layer();

        layer.addNeuron(new Neuron());
        layer.addNeuron(new Neuron());

        assertEquals(layer.size(), 2);
    }

    @Test
    public void createLayerWithNeurons() {
        Layer layer = new Layer();
        layer.generateNeurons(10);

        assertEquals(layer.size(), 10);
    }

    @Test
    public void createLayerWithNeuronsConstructor() {
        assertEquals(new Layer(10).size(), 10);
    }

    @Test
    public void createLayerWithNeuronssSameLayer() {
        Layer layer = new Layer();
        assertEquals(layer, layer.generateNeurons(10));
    }

    @Test
    public void createLayerAddNeuronsSameLayer() {
        Layer layer = new Layer();
        assertEquals(layer, layer.addNeuron(new Neuron()));
    }

    @Test
    public void getNeuronAtFirstIndex() {
        Layer layer = new Layer();
        layer.generateNeurons(3);

        assertNotNull(layer.get(0));
    }

    @Test
    public void getNeuronFirst() {
        Layer layer = new Layer();
        layer.generateNeurons(3);

        assertNotNull(layer.first());
    }

    @Test
    public void getNeuronFirstOnEmptyLayerFails() {
        Layer layer = new Layer();
        /*Throwable exception = assertThrows(NoSuchElementException.class, () -> {
            layer.first();
        });*/
        try {
        	layer.first();
            fail();
        } catch (NoSuchElementException e) {
            assertThat(e.getMessage(), containsString(""));
        }
    }

    @Test
    public void getNeuronLastOnEmptyLayerFails() {
        Layer layer = new Layer();
        /*Throwable exception = assertThrows(NoSuchElementException.class, () -> {
            layer.last();
        });*/
        try {
        	layer.last();
            fail();
        } catch (NoSuchElementException e) {
            assertThat(e.getMessage(), containsString(""));
        }
    }

    @Test
    public void getNeuronLast() {
        Layer layer = new Layer();
        layer.generateNeurons(3);

        assertNotNull(layer.last());
    }

    @Test
    public void getNeuronFistSameFirstByIndex() {
        Layer layer = new Layer();
        layer.generateNeurons(3);

        assertEquals(layer.first(), layer.get(0));
    }

    @Test
    public void getNeuronFistNotSameAsLast() {
        Layer layer = new Layer();
        layer.generateNeurons(3);

        assertTrue(layer.first()!=layer.last());
    }

    @Test
    public void getNeuronAtSecondIndex() {
        Layer layer = new Layer();
        layer.generateNeurons(3);

        assertNotNull(layer.get(1));
    }

    @Test
    public void getNeuronOutsideRange() {
        Layer layer = new Layer();
        layer.generateNeurons(3);

        /*Throwable exception = assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            layer.get(10);
        });*/
        
        try {
        	layer.get(10);
            fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            assertThat(e.getMessage(), containsString("Array index out of range: 10"));
        }
        
        //assertEquals(exception.getMessage(), "Array index out of range: 10");
    }

    @Test
    public void getNeuronsSame() {
        Layer layer = new Layer();
        layer.generateNeurons(3);

        assertEquals(layer.get(0), layer.get(0));
    }

    @Test
    public void getNeuronsNotSame() {
        Layer layer = new Layer();
        layer.generateNeurons(3);

        assertTrue(layer.get(0)!=layer.get(1));
    }

    @Test
    public void generateNeuronsGetSigmoid() {
        Layer layer = new Layer();
        layer.generateNeurons(3);

        assertTrue(layer.first().getSigmoid() instanceof DefaultSigmoid);
    }

    @Test
    public void addNeuronWithLogSigmoid() {
        Layer layer = new Layer();
        layer.addNeuron(new Neuron(new LogSigmoid(0, 1)));

        assertFalse(layer.first().getSigmoid() instanceof DefaultSigmoid);
        assertTrue(layer.first().getSigmoid() instanceof LogSigmoid);
    }

    @Test
    public void createLayerWithSigmoidNeurons() {
        Layer layer = new Layer();
        layer.generateNeurons(10, new LogSigmoid(0, 1));

        assertEquals(layer.size(), 10);
        assertTrue(layer.first().getSigmoid() instanceof LogSigmoid);
    }

    @Test
    public void createLayerWithNeuronsConstructorAndDefaultSigmoid() {
        Layer layer = new Layer(10);
        assertEquals(layer.size(), 10);
        assertTrue(layer.first().getSigmoid() instanceof DefaultSigmoid);
    }

    @Test
    public void createLayerWithNeuronsConstructorAndLogSigmoid() {
        Layer layer = new Layer(10, new LogSigmoid(0, 1));
        assertEquals(layer.size(), 10);
        assertTrue(layer.first().getSigmoid() instanceof LogSigmoid);
    }

    @Test
    public void setValuesToLayer() {
        double[] input = {1.0, 2, 30, 42, 11};
        Layer layer = generateExampleLayerWithValues(input);

        assertEquals(layer.get(0).getValue(), 1.0);
        assertEquals(layer.get(1).getValue(), 2);
        assertEquals(layer.get(2).getValue(), 30);
        assertEquals(layer.get(3).getValue(), 42);
        assertEquals(layer.get(4).getValue(), 11);
    }

    @Test
    public void getValuesFromLayer() {
        double[] input = {1.0, 2, 30, 42, 11};
        Layer layer = generateExampleLayerWithValues(input);

        List<Double> values = layer.getValues();
        assertEquals(input.length, values.size());

        assertTrue(values.get(0) == 1.0);
        assertTrue(values.get(1) == 2);
        assertTrue(values.get(2) == 30);
        assertTrue(values.get(3) == 42);
        assertTrue(values.get(4) == 11);
    }

    private Layer generateExampleLayerWithValues(double[] input) {
        Layer layer = new Layer(input.length);
        try {
            layer.setValues(input);
        }
        catch (NeuralNetworkError e) {
            fail("Exception thrown when setting values to layer");
        }
        return layer;
    }

    @Test
    public void setValuesToLayerWrongAmount() {
        Layer layer = new Layer(5);
        double[] input = {1.0, 2, 30, 42};
        /*Throwable exception = assertThrows(NeuralNetworkError.class, () -> {
            layer.setValues(input);
        });*/
        
        try {
        	layer.setValues(input);
            fail();
        } catch (NeuralNetworkError e) {
            assertThat(e.getMessage(), containsString(""));
        }
    }

    @Test
    public void getWeightsFromAllNeurons() {
        Layer layer1 = new Layer(3);
        Layer layer2 = new Layer(2);
        layer1.linkToAnother(layer2);

        List<Double> weights = layer2.getWeights();
        assertEquals(2 * 3, weights.size());
    }

    @Test
    public void adjustWeights() {
        Layer layer1 = new Layer(3);
        Layer layer2 = new Layer(2);
        layer1.linkToAnother(layer2);

        List<Double> outputError = new ArrayList<Double>();
        outputError.add(1.0d);
        outputError.add(0.3d);
        List<Double> weights1 = layer2.getWeights();

        try {
            layer2.adjustWeights(outputError, 0.5);
        }
        catch (NeuralNetworkError e) {
            fail("Exception thrown when adjusting weights");
        }

        List<Double> weights2 = layer2.getWeights();
        assertTrue(weights1.get(0)!=weights2.get(0));
        assertTrue(weights1.get(0) - 1.0 * 0.5 == weights2.get(0));
        assertTrue(weights1.get(1) - 1.0 * 0.5 == weights2.get(1));
        assertTrue(weights1.get(2) - 1.0 * 0.5 == weights2.get(2));
        assertTrue(weights1.get(3) - 0.3 * 0.5 == weights2.get(3));
        assertTrue(weights1.get(4) - 0.3 * 0.5 == weights2.get(4));
        assertTrue(weights1.get(5) - 0.3 * 0.5 == weights2.get(5));
    }

    @Test
    public void adjustInvalidAmountOfWeights() {
        Layer layer1 = new Layer(3);
        Layer layer2 = new Layer(2);
        layer1.linkToAnother(layer2);

        List<Double> outputError = new ArrayList<>();
        outputError.add(1.0d);

        /*Throwable exception = assertThrows(NeuralNetworkError.class, () -> {
            layer2.adjustWeights(outputError, 0.5);
        });*/
        
        try {
        	layer2.adjustWeights(outputError, 0.5);
            fail();
        } catch (NeuralNetworkError e) {
            assertThat(e.getMessage(), containsString(""));
        }
    }

    @Test
    public void layerPass() {
        Layer layer1 = new Layer(2);
        Layer layer2 = new Layer(1);
        double[] input = {2, 42};
        try {
            layer1.setValues(input);
        }
        catch (NeuralNetworkError e) {
            fail("Exception thrown when setting values to layer");
        }
        layer1.linkToAnother(layer2);

        layer2.pass();

        assertTrue(layer2.first().getValue() != 0);
    }
}
