package com.ayu.ml.adaboost3;

public class Instance {  
	  
    public Double[] dim;    //各个维度值  
    public Integer label;       //类别标号  
      
    public Instance(Double[] dim, Integer label) {  
        this.dim = dim;  
        this.label = label;  
    }  
}  