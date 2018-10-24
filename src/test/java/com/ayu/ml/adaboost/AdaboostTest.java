package com.ayu.ml.adaboost;

import java.util.List;

import org.junit.Test;

import com.ayu.ml.adaboost.AdaBoost;
import com.ayu.ml.adaboost.Stump;
import com.ayu.ml.adaboost.Utils;

public class AdaboostTest {
	
	@Test
    public void adaBoosTrainTest() throws Exception {
		
        String file = "data/adaboost/seeds_dataset.txt";
        List<List<Double>> dataSet = Utils.loadDataSet(file);
        List<Integer> labelSet = Utils.loadLabelSet(file);
        
        List<Stump> G=  AdaBoost.adaBoostTrain(dataSet, labelSet);
        Utils.showStumpList(G);
        System.out.println("finished");
	}

}
