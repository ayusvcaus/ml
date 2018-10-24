package com.ayu.ml.id3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class ID3 {

    /**
     * �ļ�����ȡѵ��Ԫ��
     * 
     * @return ѵ��Ԫ�鼯��
     * @throws IOException
     */
    public static List<List<String>> readFData(String fileUrl) throws IOException {
        List<List<String>> data = new ArrayList();
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileUrl)), "UTF-8"));
        String temp = null;
        String[] tempArray = null;
        while ((temp = in.readLine()) != null) {
            tempArray = temp.split("\\t");
            List<String> s = new ArrayList();
            for (int i = 0; i < tempArray.length; i++) {
                s.add(tempArray[i]);
            }
            data.add(s);
        }
        in.close();
        return data;
    }

    /**
     * �ļ�����ȡ��ѡ����
     * 
     * @return ��ѡ���Լ���
     * @throws IOException
     */
    public static List<String> readFCandAttr(String fileUrl) throws IOException {
        List<String> candAttr = new ArrayList();
        String temp = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileUrl)), "UTF-8"));
        while ((temp = in.readLine()) != null) {
            candAttr.add(temp);
        }
        in.close();
        return candAttr;
    }
    
    /** 
     * �ݹ��ӡ���ṹ 
     * @param root ��ǰ�������Ϣ�Ľ�� 
     */  
    public static void printTree(TreeNode root){  
        System.out.println("name:" + root.getName());  
        List<String> rules = root.getRule();  
        System.out.print("node rules: {");  
        for (String r: rules) {  
            System.out.print(r + " ");  
        }  
        System.out.print("}");  
        System.out.println("");  
        List<TreeNode> children = root.getChild();  
        int size =children.size();  
        if (size == 0) {  
            System.out.println("-->leaf node!<--");  
        } else {  
            System.out.println("size of children:" + children.size());  
            for (int i = 0; i < children.size(); i++) {  
                System.out.print("child " + (i + 1) + " of node " + root.getName() + ": ");  
                printTree(children.get(i));  
            }  
        }  
    } 

    public static void main(String[] args) throws IOException {
        List<List<String>> data = readFData("data/id3/lenses.txt"); //���������ļ� ������ ����tab������
        List<String> features = readFCandAttr("data/id3/features.txt"); //���������ļ� ������ ����tab������
        TreeNode t = DecisionTree.buildTree(data, features);
        
        printTree(t);
    }
}