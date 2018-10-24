package com.ayu.ml.svm;

import org.junit.Test;

import com.ayu.ml.svm.Data;
import com.ayu.ml.svm.SVM;

public class SVMTest {
	
	private int times = 7000;
	private double lambda = 0.0001;
	
	
	@Test
	public void testSVM() throws Exception {
		
		double[] y = new double[400];
		double[][] X = new double[400][11];
		String trainFile = "data/svm/svm_train_data.txt";
		Data train = SVM.loadData(trainFile);
		
		
		SVM svm = new SVM(lambda);
		svm.train(train,times);
		
		String testFile = "data/svm/svm_test_data.txt";
		Data test = SVM.loadData(testFile);
		
		
		int error=0;
		for(int i=0;i<test.getX().size();i++) {
			if(svm.predict(test.getX().get(i)) != test.getY().get(i)) {
				error++;
			}
		}
		System.out.println("total:"+test.getX().size());
		System.out.println("error:"+error);
		System.out.println("error rate:"+((double)error/test.getX().size()));
		System.out.println("acc rate:"+((double)(test.getX().size()-error)/test.getX().size()));
		
	}

}
