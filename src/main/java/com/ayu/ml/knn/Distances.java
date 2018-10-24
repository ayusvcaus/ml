package com.ayu.ml.knn;

import java.util.Comparator;

/**
 * 
 * @author haolidong
 * @Description:  [该类主要用于保存KNN的距离信息以及index]  
 */
public class Distances implements Comparable<Distances> {
	
	private double distances;
	private  int sortedDistIndicies;
	
	
	public Distances(double distances, int sortedDistIndicies) {
		this.distances = distances;
		this.sortedDistIndicies = sortedDistIndicies;
	}
	
	public double getDistances() {
		return distances;
	}
	
	public void setDistances(double distances) {
		this.distances = distances;
	}
	
	public int getSortedDistIndicies() {
		return sortedDistIndicies;
	}
	
	public void setSortedDistIndicies(int sortedDistIndicies) {
		this.sortedDistIndicies = sortedDistIndicies;
	}	
	
	@Override
	public int compareTo(Distances arg1) {
		// TODO Auto-generated method stub
		double d0=this.getDistances();
		double d1=arg1.getDistances();
		return (d0>d1) ? 1 : ((d0<d1) ? -1 : 0);
	}
}