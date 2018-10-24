package com.ayu.ml.fp2;

import java.util.ArrayList;  
import java.util.List;  
  
  
  
public class TreeNode implements Comparable<TreeNode>{  
  
    private String name; // �ڵ�����  
    private Integer count; // ����  
    private TreeNode parent; // ���ڵ�  
    private List<TreeNode> children; // �ӽڵ�  
    private TreeNode nextHomonym; // ��һ��ͬ���ڵ�  
    
    public TreeNode() {  
    
    }  
  
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
  
    public Integer getCount() {  
        return count;  
    }  
  
    public void setCount(Integer count) {  
        this.count = count;  
    }  
    public void sum(Integer count) {  
        this.count += count;  
    }  
    public TreeNode getParent() {  
        return parent;  
    }  
  
    public void setParent(TreeNode parent) {  
        this.parent = parent;  
    }  
  
    public List<TreeNode> getChildren() {  
        return children;  
    }  
  
    public void setChildren(List<TreeNode> children) {  
        this.children = children;  
    }  
  
    public TreeNode getNextHomonym() {  
        return nextHomonym;  
    }  
  
    public void setNextHomonym(TreeNode nextHomonym) {  
        this.nextHomonym = nextHomonym;  
    }  
    /** 
     * ���һ���ڵ� 
     * @param child 
     */  
    public void addChild(TreeNode child) {  
        if (children == null) {  
        	children = new ArrayList<>();  
        	children.add(child);   
        } 
        children.add(child);  
    }  
    /** 
    *  �Ƿ�����Ÿýڵ�,���ڷ��ظýڵ㣬�����ڷ��ؿ� 
    * @param name 
    * @return 
    */  
    public TreeNode findChild(String name) {  
    	
        if (children != null) {  
            for (TreeNode child : children) {  
                if (child.getName().equals(name)) {  
                    return child;  
                }  
            }  
        }  
        return null;  
    }  
  
  
    @Override  
    public int compareTo(TreeNode arg0) {  
        // TODO Auto-generated method stub  
        //int count0 = arg0.getCount();  
        // ��Ĭ�ϵıȽϴ�С�෴�����µ���Arrays.sort()ʱ�ǰ���������  
        //return count0 - this.count;  
    	return arg0.getCount() - count;
    }  
}  