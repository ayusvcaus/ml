package com.ayu.ml.fp;

import java.util.ArrayList;
import java.util.List;


/** 
 * fp-tree�ڵ�����ݽṹ��һ��item��ʾһ���ڵ㣩 
 * @author shimin 
 * 
 */ 

public class TNode implements Comparable<TNode> { 
	
    private String itemName; //��Ŀ��  
    private int count; //�������ݿ��г��ִ���  
    private TNode parent; //���ڵ�  
    private List<TNode> children; //�ӽڵ�  
    private TNode next;//��һ��ͬ���ڵ�  
      
    public TNode() {  
        this.children = new ArrayList<>();  
    } 
    
    public TNode(String name) {  
        this.itemName = name;  
        this.count = 1;  
        this.children = new ArrayList<>();  
    }  
    
    public TNode findChildren(String childName) {  
    	
        for (TNode node : children) {  
            if(node.getItemName().equals(childName)) {  
                return node;  
            }  
        }  
        return null;  
    }  
    
    public TNode getNext() {  
        return next;  
    } 
    
    public TNode getParent() {  
        return parent;  
    } 
    
    public void setNext(TNode next) {  
        this.next = next;  
    }  
    
    public void increaseCount(int num) {  
        count += num;  
    }  
    
    public int getCount() {  
        return count;  
    }  
    
    public String getItemName() {  
        return itemName;  
    }  
    
    public List<TNode> getChildren() {  
        return children;  
    }
    
    public void setParent(TNode parent) {  
        this.parent = parent;  
    } 
    
    @Override  
    public int compareTo(TNode o) {  
        return o.getCount() - count;  
    }  
}