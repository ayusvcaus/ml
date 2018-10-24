package com.ayu.ml.adaboost2;

public class Base {
    private int m = 3;
    public String[][]result(String[][]a) {
        String[][]result = new String[m][a.length];
        double[][]t = new double[a.length][a[0].length];
        for (int i=0; i<t.length; i++) {
            for (int j=0; j<t[0].length; j++) {
                try{
                    t[i][j]  = Double.parseDouble(a[i][j]);
                } catch(Exception e){
                }
            }
        }
        result[0] = G1(t);
        result[1] = G2(t);
        result[2] = G3(t);
        return result;
    }
    //�ռ��������������������result
    public String[] G1(double[][]a) {
        String[]G1 = new String[a.length];
        for (int i=0; i<a.length; i++){
            if (a[i][0]<2.5){
                G1[i] = "1";
            } else {
                G1[i] = "-1";
            }
        }
        return G1;
    }
    //��2.5Ϊ�磬����2.5��Ϊ-1��С��2.5��Ϊ1

    public String[]G2(double[][]b) {
        String[]G2 = new String[b.length];
        for (int i=0; i<b.length; i++) {
            if (b[i][0]>5.5){
                G2[i] = "1";
            }
            else{
                G2[i] = "-1";
            }
        }
        return G2;
    }
    //��5.5Ϊ�磬����5.5��Ϊ1��С��5.5��Ϊ-1

    public String[] G3(double[][]c) {
        String[]G3 = new String[c.length];
        for (int i=0; i<c.length; i++) {
            if (c[i][0]<8.5) {
                G3[i] = "1";
            }
            else{
                G3[i] = "-1";
            }
        }
        return G3;
    }
    //��8.5Ϊ�磬С��8.5��Ϊ1����Լ8.5��Ϊ-1
}
