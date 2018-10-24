package com.ayu.ml.svm;

import java.util.List;
import java.util.ArrayList;

public class Data {
	private List<Double> y;
	List<List<Double>> x;
	
	public Data() {
	};
	
	public void setY(List<Double> y) {
		this.y=y;
	}
	
	public List<Double> getY() {
		return y;
	}
	
	public void setX(List<List<Double>> x) {
		this.x=x;
	}
	
	public List<List<Double>> getX() {
		return x;
	}
}
