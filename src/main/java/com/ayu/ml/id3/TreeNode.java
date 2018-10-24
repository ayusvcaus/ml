package com.ayu.ml.id3;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class TreeNode {

    private String name; // �ڵ������������Ե����ƣ�
    private List<String> rule; // ���ķ��ѹ��� ��������
    private List<TreeNode> child; // �ӽ�㼯��
    private List<List<String>> data; // ���ֵ��ý���ѵ��Ԫ��
    private List<String> candAttr; // ���ֵ��ý��ĺ�ѡ����

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