package com.ayu.ml.id3;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DecisionTree {
    private Integer attrSelMode; // ��ѷ�������ѡ��ģʽ��1��ʾ����Ϣ���������2��ʾ����Ϣ�����ʶ�������δʵ��2

    public DecisionTree() {
        this.attrSelMode = 1;
    }

    public DecisionTree(int attrSelMode) {
        this.attrSelMode = attrSelMode;
    }

    public void setAttrSelMode(Integer attrSelMode) {
        this.attrSelMode = attrSelMode;
    }

    /**
     * ��ȡָ�����ݼ��е���������
     * 
     * @param datas
     *            ָ�������ݼ�
     * @return ����������map
     */
    public static Map<String, Integer> classOfData(List<List<String>> data) {
        Map<String, Integer> classes = new HashMap();
        List<String> tuple = null;
        for (int i = 0; i < data.size(); i++) {
            tuple = data.get(i);
            String c = tuple.get(tuple.size() - 1);
            try {
            	classes.put(c, classes.get(c) + 1);
            } catch (Exception e) {
            	classes.put(c, 1);
            }
        }
        return classes;
    }

    /**
     * ��ȡ���������������������������
     * 
     * @param classes
     *            ��ļ�ֵ����
     * @return �����������
     */
    public static String maxClass(Map<String, Integer> classes) {
        String maxC = "";
        int max = -1;
        for (String key: classes.keySet()) {
        	int val = classes.get(key);
            if (val > max) {
                max = val;
                maxC = key;
            }
        }        
        return maxC;
    }

    /**
     * ���������
     * 
     * @param datas
     *            ѵ��Ԫ�鼯��
     * @param attrList
     *            ��ѡ���Լ���
     * @return �����������
     */
    public static TreeNode buildTree(List<List<String>> data,  List<String> attrList) {

        System.out.print("��ѡ�����б� ");
        for (int i = 0; i < attrList.size(); i++) {
            System.out.print(" " + attrList.get(i) + " ");
        }

        System.out.println();
        TreeNode node = new TreeNode();
        node.setData(data);
        node.setCandAttr(attrList);
        Map<String, Integer> classes = classOfData(data);
        System.out.println("classes="+classes);// #

        String maxC = maxClass(classes);

        System.out.println("maxC="+maxC);// #

        System.out.println("��ŷ������͵ĸ�����" + classes.size());
        System.out.println("ʣ��������Ϊ" + attrList.size());
        if (classes.size() == 1 || attrList.size() == 1) {
            node.setName(maxC);
            return node;
        }

        Gain gain = new Gain(data, attrList);
        int bestAttrIndex = gain.bestGainAttrIndex();
        System.out.println("��ѷ�����������Ϊ" + bestAttrIndex);// #
        List<String> rules = gain.getValues(data, bestAttrIndex);
        System.out.println("�������Ϊ" + rules);// #
        node.setRule(rules);
        node.setName(attrList.get(bestAttrIndex));

        attrList.remove(bestAttrIndex);

        List<List<List<String>>> allData = new ArrayList() ;
        for (String rule: rules) {
            List<List<String>> di = gain.dataOfValue(bestAttrIndex, rule);
            allData.add(di);
        }
        for(int i=0;i<allData.size();i++){
            for (int j = 0; j < allData.get(i).size(); j++) {
                allData.get(i).get(j).remove(bestAttrIndex);
            }

            System.out.println("ʣ���������Ϊ" + attrList);// #
            System.out.println();
            if (allData.get(i).size() == 0 || attrList.size() == 0) {
                TreeNode leafNode = new TreeNode();
                leafNode.setName(maxC);
                leafNode.setData(allData.get(i));
                leafNode.setCandAttr(attrList);
                node.getChild().add(leafNode);
            } else {
                TreeNode newNode = buildTree(allData.get(i), attrList);
                node.getChild().add(newNode);
            }
        }
        return node;
    }
}