package com.ayu.ml.adaboost3;

public class ClassifierImpl implements Classifier {  
	
	private double errorRate;  
	private int errorNumber;  
	  
    private double threshold ;  //�������ֵ  
    private int dimNum;         //���ĸ�ά�ȷ���  
    private int fuhao = 1;      //����ֵ���ߵĴ���  
     
    @Override
    public int classify(Instance instance) {  
          
        if (instance.dim[dimNum]>= threshold) {  
            return fuhao;  
        }else {  
            return -fuhao;  
        }  
    }  
      
    /** 
     * ѵ����threshold��fuhao 
     * @param instances 
     * @param W ������Ȩ�� 
     * @param dim ���������ĸ�ά�Ƚ���ѵ�� 
     */  
    @Override
    public void train(Instance[] instances, double[] weights, int dimNum) {           
        errorRate = Double.MAX_VALUE;  
        this.dimNum = dimNum;  
        double adaThreshold = 0;  
        int adaFuhao = 0;  
        for (Instance instance : instances) {  
            threshold = instance.dim[dimNum];  
            for (int fuhaoIt=0; fuhaoIt<2; fuhaoIt++) {  
                fuhao = -fuhao;  
                double error = 0;  
                int errorNum = 0;  
                for (int i=0; i<instances.length; i++) {  
                    if (classify(instances[i])!=instances[i].label) {  
                        error += weights[i];  
                        errorNum++;  
                    }  
                }  
                if (errorRate>error){  
                    errorRate = error;  
                    errorNumber = errorNum;  
                    adaThreshold = threshold;  
                    adaFuhao = fuhao;  
                }  
            }  
        }  
        threshold = adaThreshold;  
        fuhao = adaFuhao;  
    }  
    
    @Override
    public Double getErrorRate() {
    	return errorRate;
    }
    
    @Override
    public Integer getErrorNumber() {
    	return errorNumber;
    }
}  