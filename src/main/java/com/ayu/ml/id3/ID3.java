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
     * 文件流读取训练元组
     * 
     * @return 训练元组集合
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
     * 文件流读取候选属性
     * 
     * @return 候选属性集合
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
     * 递归打印树结构 
     * @param root 当前待输出信息的结点 
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
        List<List<String>> data = readFData("data/id3/lenses.txt"); //读入数据文件 长宽不限 需用tab键隔开
        List<String> features = readFCandAttr("data/id3/features.txt"); //读入特征文件 长宽不限 需用tab键隔开
        TreeNode t = DecisionTree.buildTree(data, features);
        
        printTree(t);
    }
}