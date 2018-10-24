package com.ayu.ml.nb2;

public class Model {
	
    private float[] p0Vect;
    private float[] p1Vect;
    private float pAbusive;
    
    public float[] getP0Vect() {
        return p0Vect;
    }
    
    public float[] getP1Vect() {
        return p1Vect;
    }
    
    public void setP1Vect(float[] p1Vect) {
        this.p1Vect = p1Vect;
    }
    
    public void setP0Vect(float[] p0Vect) {
        this.p0Vect = p0Vect;
    }
    
    public float getPAbusive() {
        return pAbusive;
    }
    
    public void setPAbusive(float pAbusive) {
        this.pAbusive = pAbusive;
    }
    
    
    public Model(float[] p0Vect,float[] p1Vect,float pAbusive) {
        this.p0Vect = p0Vect;
        this.p1Vect = p1Vect;
        this.pAbusive = pAbusive;
    }

}