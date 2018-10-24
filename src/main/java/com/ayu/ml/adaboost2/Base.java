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
    //收集三个基分类器结果存入result
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
    //以2.5为界，大于2.5的为-1，小于2.5的为1

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
    //以5.5为界，大于5.5的为1，小于5.5的为-1

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
    //以8.5为界，小于8.5的为1，大约8.5的为-1
}
