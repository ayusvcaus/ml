package com.ayu.ml.nb2;

public class MathM {

    public static float sum(float[] R) {
        float sum = 0;
        for (float i : R)
            sum += i;
        return sum;
    }

    public static float[] sum(float A[], float B[]) {
        float C[] = new float[A.length];
        for (int i=0; i<C.length; i++)
            C[i] = A[i] + B[i];
        return C;
    }

    public static float[] fVect(float[] A, float pDenom) {
        float[] fvect = new float[A.length];
        for (int i=0; i<A.length; i++) {
            fvect[i] = (float) Math.log(A[i] / pDenom);   //A[i] / pDenom;//
        }
        return fvect;
    }
    
    public static double multiply(float[] A,float[] B) {
        double C = 0;
        for(int i=0; i<A.length; i++) {
            C += A[i]*B[i];
        }
        return C;
    }
}