package com.ayu.ml.svm;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.List;

import com.ayu.ml.svm.Data;

public class SVM {

	private int exampleNum;
	private int exampleDim;
	private double[] w;
	private double lambda;
	private double lr = 0.001;//0.00001
	private double threshold = 0.001;
	private double cost;
	private double[] grad;
	private double[] yp;
	
	public SVM(double lambda) {
		this.lambda = lambda;		
	}
	
	private void costAndGrad(List<List<Double>> x, List<Double> y) {
		cost = 0;
		for (int m=0; m<exampleNum; m++) {
			yp[m]=0;
			for (int d=0; d<exampleDim; d++) {
				yp[m]+= x.get(m).get(d)*w[d];
			}
			
			if (y.get(m)*yp[m]-1<0) {
				cost += 1-y.get(m)*yp[m];
			}		
		}
		
		for(int d=0; d<exampleDim; d++) {
			cost += 0.5*lambda*w[d]*w[d];
		}
		
		for (int d=0; d<exampleDim; d++) {
			grad[d] = Math.abs(lambda*w[d]);	
			for (int m=0;m<exampleNum;m++) {
				if (y.get(m)*yp[m]-1<0) {
					grad[d]-= y.get(m)*x.get(m).get(d);
				}
			}
		}				
	}
	
	private void update() {
		for(int d=0; d<exampleDim; d++) {
			w[d] -= lr*grad[d];
		}
	}
	
	public void train(Data d, int maxIters) {
		List<Double> y = d.getY();
		List<List<Double>> x = d.getX();
		
		exampleNum = x.size();
		if (exampleNum <=0)  {
			System.out.println("num of example <=0!");
			return;
		}
		exampleDim = x.get(0).size();
		w = new double[exampleDim];
		grad = new double[exampleDim];
		yp = new double[exampleNum];
		
		for (int iter=0;iter<maxIters;iter++) {
			
			costAndGrad(x,y);
			//System.out.println("cost:"+cost);
			if (cost<threshold) {
				break;
			}
			update();			
		}
	}
	
	public int predict(List<Double> x) {
		double pre=0;
		for (int j=0;j<x.size();j++) {
			pre+=x.get(j)*w[j];
		}
		if(pre >=0)//这个阈值一般位于-1到1
			return 1;
		else return -1;
	}
	
	
	public static Data loadData(String trainFile) throws IOException {
		
		File file = new File(trainFile);
		RandomAccessFile raf = new RandomAccessFile(file,"r");
		StringTokenizer tokenizer,tokenizer2; 
		
		Data d = new Data();
		
		List<Double> y = new ArrayList<>();
		List<List<Double>> x = new ArrayList<>();
		String line = null;
		while((line = raf.readLine()) !=null ) {			
			tokenizer = new StringTokenizer(line," ");
			y.add(Double.parseDouble(tokenizer.nextToken()));
			List<Double> z = new ArrayList<>();
			z.add(new Double(1));
			while(tokenizer.hasMoreTokens()) {
				tokenizer2 = new StringTokenizer(tokenizer.nextToken(),":");
				tokenizer2.nextToken();
				z.add(Double.parseDouble(tokenizer2.nextToken()));			
			}	
			x.add(z);
		}
		
		d.setY(y);
		d.setX(x);
		return d;
	}	
}
