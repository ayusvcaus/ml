package com.ayu.ml.adaboost2;

public class AdaBoost {
    String[] classfied;
    double[]weights;
    Arith ari = new Arith();
    double[]alfa;
    String[][]feature;

    public AdaBoost(String[][]a,String[]b) {
        classfied=b;
        for (int i=0; i<classfied.length; i++) {
        }
        feature=a;
        weights=new double[b.length];
        alfa=new double[a.length];
        for (int i=0; i<weights.length; i++) {
            weights[i]=ari.div(1,b.length);
        }
    }
    //初始化权重系数


    public double[]alfa(){
        for (int i=0;i<alfa.length;i++) {
            int index=0;
            double error=error(weights,feature[0]);
            for (int j=1; j<feature.length; j++) {
                if (error(weights,feature[j])<error) {
                    error=error(weights,feature[j]);
                    index=j;
                }
            }
            if (error!=0&&error!=1) {
                alfa[index]=ari.mul(ari.div(1, 2),Math.log(ari.div(1-error,error)));
            }
            weight(alfa[index],feature[index]);
        }
        return alfa;
    }
    //计算基分类器权重

    public double error(double[]a,String[]b) {
        double error=0;
        for (int i=0;i<b.length;i++) {
            if (!b[i].equals(classfied[i])) {
                error=error+a[i];
            }
        }
        return error;
    }
    //计算误差

    public void weight(double a,String[]b) {
        double zm=0;
        for (int i=0; i<b.length; i++){
            zm=zm+weights[i]*Math.exp(ari.mul(-a,ari.mul(Double.parseDouble(b[i]),Double.parseDouble(classfied[i]))));
        }
        for (int i=0; i<weights.length; i++){
            weights[i]=ari.mul(ari.div(weights[i],zm),Math.exp(ari.mul(-a,ari.mul(Double.parseDouble(b[i]),Double.parseDouble(classfied[i])))));
        }
    }
    //计算样本权重
}