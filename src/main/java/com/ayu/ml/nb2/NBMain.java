package com.ayu.ml.nb2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import com.ayu.ml.nb2.MathM;

/**
 * @author Home
 * 
 */
public class NBMain {

    /**
     * @param args
     */
    final static String dataPath = "data/nb2/data.csv";

    static List<String[]> dataList = new ArrayList<>();//Ѹ�����ʼ���
    static List<float[]> vectorList = new ArrayList<>();//ѵ����������
    //static List<String> vocabList;//�ʵ�
    static float[] trainCategory; //ѵ�������trainCategory=[0,1,0,1,0,1]
    static int numTrainDocs = 0;//ѵ�����ı�����
    static int numwords = 0; //�ʵ�size
    
    public static void main(String[] args) throws IOException { 
    	List<String> vocabList = loadDataSet(dataPath);
        Model model = trainBayes();
        System.out.println(Arrays.toString(model.getP0Vect()));
        System.out.println(Arrays.toString(model.getP1Vect()));
        String[] test1 = {"love", "my", "dalmation"};
        String[] test2 = {"stupid", "garbage"};
        System.out.println(classifyNB(setofWords2Vec(vocabList, test1, test1.length), model));
        System.out.println(classifyNB(setofWords2Vec(vocabList, test2, test2.length), model));

    }

    public static int classifyNB(float[] vec2Classify, Model model){
        double p1 = MathM.multiply(vec2Classify, model.getP1Vect())+Math.log(model.getPAbusive());
        double p0 = MathM.multiply(vec2Classify, model.getP0Vect())+Math.log(1-model.getPAbusive());
        if(p1>p0)
            return 1;
        else
            return 0;
    }

    public static List<String> loadDataSet(String dataPath) throws IOException {
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(new File(dataPath)));
            while ((line = br.readLine()) != null) {
                String[] info = line.split(",");
                dataList.add(info);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        numTrainDocs = dataList.size();
        Set<String> vocabSet = new HashSet<>();
        trainCategory = new float[dataList.size()];
        int j = 0;
        for (String[] str : dataList) {
            for (int i=0; i<str.length-1; i++)
            	vocabSet.add(str[i]);
            trainCategory[j++] = Integer.parseInt(str[str.length-1]);
        }
        List<String> vocabList = new ArrayList<>(vocabSet);
        Collections.sort(vocabList);


        for (String[] str : dataList) {
            float[] temp = setofWords2Vec(vocabList, str, str.length-1);
            vectorList.add(temp);
        }
        
        numwords = vocabList.size();
        return vocabList;
    }
    
    public static float[] setofWords2Vec(List<String> vocabList,String[] postingDoc, int n){
        float[] temp = new float[vocabList.size()];
        int index = -1;
        for (int i = 0; i <n; i++) {
            index = vocabList.indexOf(postingDoc[i]);
            temp[index] = 1.0f;
        }
        return temp;
    }
    
    // ����������
    /*
     * 1.���ȼ��������������ĵ���class=1���ĸ��ʣ���p(c1);p(c0) = 1-p(c1); 2.����p(wi|c1)�Լ�p(wi|c0)
     * 
     * List<String[]> dataList,List<Integer[]> vectorList
     */

    public static Model trainBayes() {

        float pAbusive = (float) (MathM.sum(trainCategory) / numTrainDocs);
        float[] p0Num = new float[numwords];
        float[] p1Num = new float[numwords];
        Arrays.fill(p0Num, 1);
        Arrays.fill(p1Num, 1);
        //��Ϊ�ܶ�ʳ��ִ���Ϊ0��Ϊʹ���ʲ�Ϊ0.�����дʳ�ʼ��Ϊ1����ĸ��ʼ��Ϊ2
        float p0Denom = 2.0f;
        float p1Denom = 2.0f;
        for (int i=0; i<numTrainDocs; i++) {
            float[] temp = vectorList.get(i);
            if (trainCategory[i]==1) {
                p1Num = MathM.sum(p1Num, temp);
                p1Denom += MathM.sum(temp);    

            } else {
                p0Num = MathM.sum(p0Num, temp);
                p0Denom += MathM.sum(temp);
            }
        }
        //�����������p(w|c)��С�����Ϊ�������ò�����ȷ�𰸡������Գ˻�ȡ��Ȼ������ln(a*b) = ln(a)+ln(b)��
        float[] p1Vect = MathM.fVect(p1Num, p1Denom);   
        float[] p0Vect = MathM.fVect(p0Num, p0Denom);
        Model m = new Model(p0Vect, p1Vect, pAbusive);
        return m;
    }
}