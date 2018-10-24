package com.ayu.ml.adaboost2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

public class InputStringData {
	private String[][] features;
	private String[] labels;
	
    public void loadData(String trainfile) throws IOException {
        List<String[]> data = new ArrayList<>(); 
        List<String> labelList = new ArrayList<>(); 
        FileInputStream fis = new FileInputStream(trainfile);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line="";
        while ((line=br.readLine())!=null){
            String[] s=line.split(",");
            String[] fs = new String[s.length-1];
            System.arraycopy(s, 0, fs, 0, s.length-1);
            data.add(fs);          
            labelList.add(s[s.length-1]);
        }
        br.close();
        features = data.toArray(new String[data.size()][]);
        labels = labelList.toArray(new String[labelList.size()]);
    }
    
    public String[][] getFeatures() {
    	return features;
    }
    
    public String[] getLabels() {
    	return labels;
    }
}
