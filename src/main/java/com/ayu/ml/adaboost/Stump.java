package com.ayu.ml.adaboost;

import java.util.List;

/**
 * Class: SampleNode
 * Description: sample class 
 * Company: HUST
 * @author Sonly
 * Date: 2017Äê5ÔÂ24ÈÕ
 */
public class Stump {
	public int dim;
    public double thresh;
    public String condition;
    public double error;
    public List<Integer> labelList;
    double factor;
    
    public String toString() {
        return "dim is "+dim+"\nthresh is "+thresh+"\ncondition is "+condition+"\nerror is "+error+"\nfactor is "+factor+"\nlabel is "+labelList;
    }
}
