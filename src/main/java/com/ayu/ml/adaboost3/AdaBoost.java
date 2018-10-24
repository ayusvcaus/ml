package com.ayu.ml.adaboost3;

import java.util.List;
import java.util.ArrayList;

public class AdaBoost {  
	  
    Instance[] instances;  
    List<Classifier> classifierList;   //������������  
    List<Double> alphaList;            //ÿ������������Ȩ��  
      
    public AdaBoost(Instance[] instances) {  
          
        this.instances = instances;  
    }  
      
    //public void adaboost(int T) {  
    public void adaboost() {        
        int len = instances.length;  
        double[] weights = new double[len];   //Ȩ��  
        for (int i=0; i<len; i++) {  
            weights[i] = 1.0 / len;  
        }  
        classifierList = new ArrayList<>();  
        alphaList = new ArrayList<>(); 
        int errCnt = 0;
        //for (int t=0; t<T; t++) { 
        do {
            Classifier cf = getMinErrorRateClassifier(weights);  
            classifierList.add(cf);  
            double errorRate = cf.getErrorRate();  
            //��������������Ȩ��  
            double alpha = 0.5 * Math.log((1 - errorRate) / errorRate);  
            alphaList.add(alpha);  
            //����������Ȩ��  
            errCnt = getErrorCount();
            System.out.println(errCnt);
            if (errCnt==0) {
            	break;
            }
            
            double z = 0;  
            for (int i=0; i<weights.length; i++) {  
                weights[i] = weights[i] * Math.exp(-alpha * instances[i].label * cf.classify(instances[i]));  
                z += weights[i];  
            }  
            for (int i=0; i<weights.length; i++) {  
                weights[i] /= z;  
            }
            //errCnt = getErrorCount();
            //System.out.println(errCnt); 
        } while (true);
    }  
      
    private int getErrorCount() {  
          
        int count = 0;  
        for(Instance instance : instances) {  
            if(predict(instance) != instance.label) {  
                count++;  
            }
        }  
        return count;  
    }  
      
    /** 
     * Ԥ�� 
     * @param instance 
     * @return 
     */  
    public int predict(Instance instance) {  
          
        double p = 0;  
        for(int i = 0; i < classifierList.size(); i++) {  
            p += classifierList.get(i).classify(instance) * alphaList.get(i);  
        }  
        if(p > 0) return 1;  
        return -1;  
    }  
  
    /** 
     * �õ���������͵ķ����� 
     * @param W 
     * @return 
     */  
    private Classifier getMinErrorRateClassifier(double[] weights) {  
          
        double errorRate = Double.MAX_VALUE;  
        ClassifierImpl minErrorRateClassifier = null;  
        int dimLength = instances[0].dim.length;  
        for (int i=0; i<dimLength; i++) {  
        	ClassifierImpl sc = new ClassifierImpl();  
            sc.train(instances, weights, i);  
            if (errorRate>sc.getErrorRate()){  
                errorRate  = sc.getErrorRate();  
                minErrorRateClassifier = sc;  
            }  
        }  
        return minErrorRateClassifier;  
    }  
}  