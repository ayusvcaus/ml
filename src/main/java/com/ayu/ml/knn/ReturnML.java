package com.ayu.ml.knn;

import java.util.List;
import java.util.ArrayList;

/**
 * @author haolidong
 * @Description:  [该类主要用于保存信息矩阵以及矩阵标签]
 */
public class ReturnML {
	
	public List<List<Double>> AR;
	public List<String> AS;	
	
	public ReturnML() {
		AR = new ArrayList();
		AS = new ArrayList();
	}
}