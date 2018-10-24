package com.ayu.ml.fp;

import java.util.ArrayList;
import java.util.List;


/** 
 * fp-tree节点的数据结构（一个item表示一个节点） 
 * @author shimin 
 * 
 */ 

public class TNode implements Comparable<TNode> { 
	
    private String itemName; //项目名  
    private int count; //事务数据库中出现次数  
    private TNode parent; //父节点  
    private List<TNode> children; //子节点  
    private TNode next;//下一个同名节点  
      
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