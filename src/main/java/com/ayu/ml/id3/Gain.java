package com.ayu.ml.id3;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import com.ayu.ml.id3.DecimalCalculate;

public class Gain {
    private List<List<String>> trainData = null; // 训练元组
    private List<String> attrList = null; // 候选属性集

    public Gain(List<List<String>> data, List<String> attrList) {
        this.trainData = data;
        this.attrList = attrList;
    }

    /**
     * 获取最佳侯选属性列上的值域（假定所有属性列上的值都是有限的名词或分类类型的）
     * 
     * @param attrIndex
     *            指定的属性列的索引
     * @return 值域集合
     */
    public List<String> getValues(List<List<String>> data, int attrIndex) {
    	Set<String> values = new HashSet();
        for (int i = 0; i < data.size(); i++) {
        	values.add(data.get(i).get(attrIndex));
        }
        return new ArrayList<String>(values);
    }

    /**
     * 获取指定数据集中指定属性列索引的域值及其计数
     * 
     * @param d
     *            指定的数据集
     * @param attrIndex
     *            指定的属性列索引
     * @return 类别及其计数的map
     */
    public Map<String, Integer> valueCounts(List<List<String>> data, int attrIndex) {
        Map<String, Integer> valueCount = new HashMap();
        for (int i=0; i<data.size(); i++) {
            String c = data.get(i).get(attrIndex);
            try {
            	valueCount.put(c, valueCount.get(c) + 1);
            } catch (Exception e) {
            	valueCount.put(c, 1);
            }
        }
        return valueCount;
    }

    /**
     * 求对data中元组分类所需的期望信息，即data的熵
     * 
     * @param data
     *            训练元组
     * @return data的熵值
     */
    public double infoTrainData(List<List<String>> data) {
        double info = 0;
        int total = data.size();
        for (Integer v: valueCounts(data, attrList.size()).values()) {
            double base = DecimalCalculate.div(v, total, 3);
            info += base * Math.log(base);
        }
        return -1*info;
    }

    /**
     * 获取指定属性列上指定值域的所有元组
     * 
     * @param attrIndex
     *            指定属性列索引
     * @param value
     *            指定属性列的值域
     * @return 指定属性列上指定值域的所有元组
     */
    public List<List<String>> dataOfValue(int attrIndex, String value) {
        List<List<String>> di = new ArrayList();
        for (int i = 0; i < trainData.size(); i++) {
        	List<String> t = trainData.get(i);
            if (t.get(attrIndex).equals(value)) {
                di.add(t);
            }
        }
        return di;
    }

    /**
     * 基于按指定属性划分对trainData的元组分类所需要的期望信息
     * 
     * @param attrIndex
     *            指定属性的索引
     * @return 按指定属性划分的期望信息值
     */
    public double infoAttr(int attrIndex) {
        double info = 0;
        int tSize = trainData.size();
        for (String v: getValues(trainData, attrIndex)) {
            List<List<String>> dv = dataOfValue(attrIndex, v);
            info += DecimalCalculate.mul(DecimalCalculate.div(dv.size(), tSize, 3), infoTrainData(dv));
        }
        return info;
    }

    /**
     * 获取最佳分裂属性的索引
     * 
     * @return 最佳分裂属性的索引
     */
    public int bestGainAttrIndex() {
        int index = 0;
        double gain = 0;
        for (int i = 0; i < attrList.size(); i++) {
        	double tempGain = infoTrainData(trainData) - infoAttr(i);
            if (tempGain > gain) {
                gain = tempGain;
                index = i;
            }
        }
        return index;
    }
}