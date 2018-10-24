package com.ayu.ml.adaboost3;

import java.io.IOException;

import org.junit.Test;

import com.ayu.ml.adaboost3.Utils;

public class AdaBoostTest {
	
	@Test
    public void testAdaBoost() throws IOException{

        Double[][] data = Utils.loadData("data/adaboost/seeds_dataset.txt");
        Instance[] instances = new Instance[data.length];
        for (int i=0; i<data.length; i++) {
        	Double[] dim = new Double[data[i].length-1];
        	for (int j=0; j<data[i].length-1; j++) {
        		dim[j] = data[i][j];
        	}
        	instances[i] = new Instance(dim, data[i][data[i].length-1].intValue());
        }
        AdaBoost ab = new AdaBoost(instances);  
        ab.adaboost();  
    } 
}
