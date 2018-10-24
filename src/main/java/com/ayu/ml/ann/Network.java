package com.ayu.ml.ann;

import java.util.List;
import java.util.ArrayList;

public class Network {
	
    private List<Layer> layers = new ArrayList<>();

    public void addLayer(Layer l) {
        layers.add(l);
    }

    public long size() {
        return layers.size();
    }

    public Layer get(int index) {
        return layers.get(index);
    }

    public Layer first() {
        return layers.get(0);
    }

    public Layer last() {
        return layers.get(layers.size()-1);
    }

    public void linkAll() {
        for (int i=0; i<size()-1; i++) {
            get(i).linkToAnother(get(i+1));
        }
    }

    public void forwardPass() {
        for (int i=1; i<size(); i++) {
            get(i).pass();
        }
    }
}