package com.ayu.ml.adaboost2;

import java.math.BigDecimal;

public class Arith {
	private static final int DEF_DIV_SCALE=10;

    public double add(double v1,double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
   
    public double sub(double v1,double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }
    
    public double mul(double v1,double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
    
    public double div(double v1,double v2) {
        return div(v1,v2,DEF_DIV_SCALE);
    }
    
    public double div(double v1,double v2,int scale) {
    	if (scale<0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    public double mul(double v1,double v2,int scale){
        if (scale<0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1=new BigDecimal(Double.toString(v1));
        BigDecimal b2=new BigDecimal(Double.toString(v2));
        if (v1!=0&&v2!=0) {
            BigDecimal b3 = new BigDecimal(Double.toString(1));
            BigDecimal b4 = new BigDecimal(b3.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue());
            return b1.divide(b4,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        else {
            return 0;
        }
    }
    
    public double round(double v,int scale){
        if (scale<0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }   
}
