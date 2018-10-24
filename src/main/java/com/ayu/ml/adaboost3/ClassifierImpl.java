package com.ayu.ml.adaboost3;

public class ClassifierImpl implements Classifier {  
	
	private double errorRate;  
	private int errorNumber;  
	  
    private double threshold ;  //分类的阈值  
    private int dimNum;         //对哪个维度分类  
    private int fuhao = 1;      //对阈值两边的处理  
     
    @Override
    public int classify(Instance instance) {  
          
        if (instance.dim[dimNum]>= threshold) {  
            return fuhao;  
        }else {  
            return -fuhao;  
        }  
    }  
      
    /** 
     * 训练出threshold和fuhao 
     * @param instances 
     * @param W 样例的权重 
     * @param dim 对样例的哪个维度进行训练 
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