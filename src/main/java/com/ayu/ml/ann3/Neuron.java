package com.ayu.ml.ann3;

import java.util.List;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class Neuron {

    @Getter @Setter 
    private double value;
    @Getter 
    private Sigmoid sigmoid;
    @Getter @Setter 
    private double error;
    @Getter 
    private List<Link> links;

    public Neuron() {
        this.sigmoid = new DefaultSigmoid();
        links = new ArrayList<>();
    }

    public Neuron(double value) {
        this.sigmoid = new DefaultSigmoid();
        this.value = value;
        links = new ArrayList<>();
    }

    public Neuron(Sigmoid sigmoid) {
        this.sigmoid = sigmoid;
        links = new ArrayList<>();
    }

    public Neuron(double value, Sigmoid sigmoid) {
        this.sigmoid = sigmoid;
        this.value = value;
        links = new ArrayList<>();
    }

    public void linkTo(Neuron to) {
        to.addLink(new Link(this, to));
    }

    public void linkFrom(Neuron from) {
        addLink(new Link(from, this));
    }

    public void addLink(Link link) {
        links.add(link);
    }

    public Link getLinkFrom(Neuron from) {
        for (Link l : links) {
            if (l.getFrom()==from) return l;
        }
        return null;
    }

    public long linkCount() {
        return links.size();
    }

    public double calculateWeightedSum() {
        double sum = 0;
        for (int i=0; i<links.size(); i++) {
            sum += links.get(i).calculateWeightedValue();
        }
        return sum;
    }

    public void pass() {
        double sum = calculateWeightedSum();
        value = sigmoid.transfer(sum);
    }
}
