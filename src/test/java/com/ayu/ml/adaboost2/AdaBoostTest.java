package com.ayu.ml.adaboost2;

import java.io.IOException;

import org.junit.Test;

public class AdaBoostTest {
	
	@Test
    public void testAdaBoost() throws IOException{
        InputStringData data=new InputStringData();
        data.loadData("data/adaboost/seeds_dataset.txt");
        String[][] feature = data.getFeatures();
        String[] classfied = data.getLabels();
        //读取样本类别，并存入classfied
        Base base=new Base();
        String[][]Result = base.result(feature);
        //计算基分类器分类结果并存入数组Result

        AdaBoost myboost = new AdaBoost(Result,classfied);
        double[] alfa = myboost.alfa();
        for (int i=0; i<alfa.length; i++) {
            System.out.print("第"+i+"个基分类器权重："+alfa[i]+" ");
            System.out.println(" ");
        }
        //打印基分类器权重
    }
}
