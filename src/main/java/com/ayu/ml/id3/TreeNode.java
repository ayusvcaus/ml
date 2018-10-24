package com.ayu.ml.id3;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class TreeNode {

    private String name; // 节点名（分裂属性的名称）
    private List<String> rule; // 结点的分裂规则 二分属性
    private List<TreeNode> child; // 子结点集合
    private List<List<String>> data; // 划分到该结点的训练元组
    private List<String> candAttr; // 划分到该结点的候选属性

    public TreeNode() {
        this.name = "";
        this.rule = new ArrayList();
        this.child = new ArrayList();
        this.data = null;
        this.candAttr = null;
    }

    public List<TreeNode> getChild() {
        return child;
    }

    public void setChild(List<TreeNode> child) {
        this.child = child;
    }

    public List<String> getRule() {
        return rule;
    }

    public void setRule(List<String> rule) {
        this.rule = rule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }

    public List<String> getCandAttr() {
        return candAttr;
    }

    public void setCandAttr(List<String> candAttr) {
        this.candAttr = candAttr;
    }

}