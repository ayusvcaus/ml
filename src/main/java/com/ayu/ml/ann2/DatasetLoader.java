package com.ayu.ml.ann2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatasetLoader {

    public static List<double[]> loadSet(String path) throws IOException{

        List<double[]> dataSet = new ArrayList<>();
        BufferedReader fileReader = new BufferedReader(new FileReader(path));
        String line;
        String[] splitLine;
        double[] lineNumeric;

        while((line = fileReader.readLine()) != null){
            splitLine = line.split("[\t+ ?| +,]");
            lineNumeric = new double[splitLine.length];

            for(int idx = 0; idx < splitLine.length; idx++)
                lineNumeric[idx] = Double.valueOf(splitLine[idx]);

            dataSet.add(lineNumeric);
        }
        return dataSet;
    }

    public static void normalizeMinMax(int min, int max, int inputs, List<double[]> dataSet){
        List<double[]> minMaxArray = new ArrayList<>();

        for (int colIdx=0; colIdx<inputs; colIdx++){ // for each column, initialize a [2] min/max double array
            double[] newMinMax = new double[2];
            newMinMax[0] = Double.MAX_VALUE;
            newMinMax[1] = Double.MIN_VALUE;
            minMaxArray.add(newMinMax);
        }

        for (double[] row : dataSet){ // find min/max for each column in the set
            for (int colIdx=0; colIdx<inputs; colIdx++){
                if (row[colIdx]<minMaxArray.get(colIdx)[0])
                    minMaxArray.get(colIdx)[0] = row[colIdx];
                if (row[colIdx]>minMaxArray.get(colIdx)[1])
                    minMaxArray.get(colIdx)[1] = row[colIdx];
            }
        }
        for (int row = 0; row<dataSet.size(); row++){
            for (int colIdx = 0; colIdx<inputs; colIdx++)
                dataSet.get(row)[colIdx] = (dataSet.get(row)[colIdx] - minMaxArray.get(colIdx)[0])/(minMaxArray.get(colIdx)[1] - minMaxArray.get(colIdx)[0]);
        }
    }
}
