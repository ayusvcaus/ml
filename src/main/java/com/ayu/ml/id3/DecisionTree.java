package com.ayu.ml.id3;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DecisionTree {
    private Integer attrSelMode; // 最佳分裂属性选择模式，1表示以信息增益度量，2表示以信息增益率度量。暂未实现2

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
     * 获取指定数据集中的类别及其计数
     * 
     * @param datas
     *            指定的数据集
     * @return 类别及其计数的map
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
     * 获取具有最大计数的类名，即求多数类
     * 
     * @param classes
     *            类的键值集合
     * @return 多数类的类名
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
     * 构造决策树
     * 
     * @param datas
     *            训练元组集合
     * @param attrList
     *            候选属性集合
     * @return 决策树根结点
     */
    public static TreeNode buildTree(List<List<String>> data,  List<String> attrList) {

        System.out.print("候选属性列表： ");
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

        System.out.println("存放分类类型的个数是" + classes.size());
        System.out.println("剩余特征数为" + attrList.size());
        if (classes.size() == 1 || attrList.size() == 1) {
            node.setName(maxC);
            return node;
        }

        Gain gain = new Gain(data, attrList);
        int bestAttrIndex = gain.bestGainAttrIndex();
        System.out.println("最佳分类特征索引为" + bestAttrIndex);// #
        List<String> rules = gain.getValues(data, bestAttrIndex);
        System.out.println("分类规则为" + rules);// #
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

            System.out.println("剩余分类特征为" + attrList);// #
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