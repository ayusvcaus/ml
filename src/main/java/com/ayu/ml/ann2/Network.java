package com.ayu.ml.ann2;

import java.util.List;
import java.util.ArrayList;

public class Network {

    private List<Neuron[]> layers;

    public Network(){
        layers = new ArrayList<>();
    }

    public void append(Neuron[] layer){
        layers.add(layer);
    }

    public List<Neuron[]> getLayers() {
        return layers;
    }
    
    public Network(List<Neuron[]> layers) {
    	//Deep copy
    	this.layers = new ArrayList<>();
    	for (Neuron[] na: layers) {
    		Neuron[] nt = new Neuron[na.length];
    		for (int i=0; i<nt.length; i++) {
    			nt[i] = new Neuron(na[i]);
    		}
    		this.layers.add(nt);
    	}
    }

    @Override
    public String toString(){

        StringBuilder sb = new StringBuilder();
        int layerNum = 0;

        for(Neuron[] layer : layers){
            sb.append("Layer :[")
            .append(layerNum+1)
            .append("]\n");
            int neuronNum = 0;

            for(Neuron neuron : layer){
                sb.append("Neuron :[")
                .append(neuronNum+1)
                .append("]  ")
                .append(neuron.toString());
                if(neuronNum == layer.length-1)
                    sb.append("\n");
                neuronNum++;
            }
            layerNum++;
        }
        return sb.toString();
    }
}
