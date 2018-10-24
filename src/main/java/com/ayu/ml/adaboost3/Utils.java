package com.ayu.ml.adaboost3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
	
    public static Double[][] loadData(String trainfile) throws IOException {
        List<Double[]> data = new ArrayList<>(); 
        FileInputStream fis = new FileInputStream(trainfile);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line="";
        while ((line=br.readLine())!=null){
            String[] s=line.split(",");
            Double[] fs = new Double[s.length];
            for (int i=0; i<s.length; i++) {
            	fs[i] = Double.parseDouble(s[i]);
            }
            data.add(fs);          
        }
        br.close();
        return data.toArray(new Double[data.size()][]);
    }
}
