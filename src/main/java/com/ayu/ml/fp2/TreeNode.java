package com.ayu.ml.fp2;

import java.util.ArrayList;  
import java.util.List;  
  
  
  
public class TreeNode implements Comparable<TreeNode>{  
  
    private String name; // 节点名称  
    private Integer count; // 计数  
    private TreeNode parent; // 父节点  
    private List<TreeNode> children; // 子节点  
    private TreeNode nextHomonym; // 下一个同名节点  
    
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
     * 添加一个节点 
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
    *  是否存在着该节点,存在返回该节点，不存在返回空 
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
        // 跟默认的比较大小相反，导致调用Arrays.sort()时是按降序排列  
        //return count0 - this.count;  
    	return arg0.getCount() - count;
    }  
}  