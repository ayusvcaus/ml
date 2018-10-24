package com.ayu.ml.adaboost3;

import com.ayu.ml.adaboost3.Instance;

public interface Classifier {
      
	public Double getErrorRate();
	
	public Integer getErrorNumber();
	
    public int classify(Instance instance);
    
    public void train(Instance[] instances, double[] weights, int dimNum);
}
