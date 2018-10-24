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
        //��ȡ������𣬲�����classfied
        Base base=new Base();
        String[][]Result = base.result(feature);
        //���������������������������Result

        AdaBoost myboost = new AdaBoost(Result,classfied);
        double[] alfa = myboost.alfa();
        for (int i=0; i<alfa.length; i++) {
            System.out.print("��"+i+"����������Ȩ�أ�"+alfa[i]+" ");
            System.out.println(" ");
        }
        //��ӡ��������Ȩ��
    }
}
