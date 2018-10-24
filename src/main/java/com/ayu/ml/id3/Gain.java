package com.ayu.ml.id3;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import com.ayu.ml.id3.DecimalCalculate;

public class Gain {
    private List<List<String>> trainData = null; // ѵ��Ԫ��
    private List<String> attrList = null; // ��ѡ���Լ�

    public Gain(List<List<String>> data, List<String> attrList) {
        this.trainData = data;
        this.attrList = attrList;
    }

    /**
     * ��ȡ��Ѻ�ѡ�������ϵ�ֵ�򣨼ٶ������������ϵ�ֵ�������޵����ʻ�������͵ģ�
     * 
     * @param attrIndex
     *            ָ���������е�����
     * @return ֵ�򼯺�
     */
    public List<String> getValues(List<List<String>> data, int attrIndex) {
    	Set<String> values = new HashSet();
        for (int i = 0; i < data.size(); i++) {
        	values.add(data.get(i).get(attrIndex));
        }
        return new ArrayList<String>(values);
    }

    /**
     * ��ȡָ�����ݼ���ָ����������������ֵ�������
     * 
     * @param d
     *            ָ�������ݼ�
     * @param attrIndex
     *            ָ��������������
     * @return ����������map
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
     * ���data��Ԫ����������������Ϣ����data����
     * 
     * @param data
     *            ѵ��Ԫ��
     * @return data����ֵ
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
     * ��ȡָ����������ָ��ֵ�������Ԫ��
     * 
     * @param attrIndex
     *            ָ������������
     * @param value
     *            ָ�������е�ֵ��
     * @return ָ����������ָ��ֵ�������Ԫ��
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
     * ���ڰ�ָ�����Ի��ֶ�trainData��Ԫ���������Ҫ��������Ϣ
     * 
     * @param attrIndex
     *            ָ�����Ե�����
     * @return ��ָ�����Ի��ֵ�������Ϣֵ
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
     * ��ȡ��ѷ������Ե�����
     * 
     * @return ��ѷ������Ե�����
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