package com.ayu.ml.adaboost;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * Class: AdaBoost
 * Description: to execute AdaBoost algorithm
 * Company: HUST
 * @author Sonly
 * Date: 2017Äê5ÔÂ23ÈÕ
 */
public class AdaBoost {
	
	/**
	 * Method: AdaBoostTrain
	 * Description: train AdaBoost
	 * @param dataSet
	 * @param labelList
	 * @return
	 */
    public static List<Stump> adaBoostTrain(List<List<Double>> dataSet, List<Integer> labelList) {
        int row = labelList.size();
        List<Double> weights = Utils.getInitWeights(row);
        List<Stump> result = new ArrayList<>();
        List<Double> accError = Utils.InitAccWeightError(row);
        int n = 1;
        while (true){
            Stump stump = Utils.buildStump(dataSet, labelList, weights, n);
            result.add(stump);
            weights = Utils.updateWeights(stump, labelList, weights);
            accError = Utils.accWeightError(accError, stump);
            double error = Utils.calErrorRate(accError, labelList);
            if (error<0.001) {
                break;
            }
            n++;
        }
        return result;
    }
    
    /**
     * Method: main
     * Description: the entrance of AdaBoost
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String file = "data/adaboost/seeds_dataset.txt";
        List<List<Double>> dataSet=Utils.loadDataSet(file);
        List<Integer> labelSet=Utils.loadLabelSet(file);
        List<Stump> stump = adaBoostTrain(dataSet, labelSet);
        Utils.showStumpList(stump);
        System.out.println("finished");
    }
}
