package com.ayu.ml.c45;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;
import java.util.Map.Entry;

import com.ayu.ml.c45.Tool;

public class TreeNode implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private List<TreeNode> children;
	private List<String> path; 
	private Map<String, Long> targetValueMap;
	private String value;
	private int index;
	private String nodeName;
	private int size;
	
	public Map<String, Long> getTargetValueMap() {
		return targetValueMap;
	}
	
	public void setTargetValueMap(Map<String, Long> targetValueMap) {
		this.targetValueMap = targetValueMap;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public List<TreeNode> getChildren() {
		return children;
	}
	
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	
	public void addChildren(TreeNode node){
		this.children.add(node);
	}
	
	public List<String> getPath() {
		return path;
	}
	
	public String getPath(int i) {
		return path.get(i);
	}
	
	public void setPath(List<String> path) {
		this.path = path;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public String getNodeName() {
		return nodeName;
	}
	
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	public TreeNode() {
		children = new ArrayList<>();
		path = new ArrayList<>();
	}
	
	public String showPath() {
		String returnMessage = "(";
		for (String p:path){
			returnMessage+=" "+p+" ";
		}
		returnMessage+=")";
		return returnMessage;
	}
	
	public void addPath(String s) {
		path.add(s);
	}
	
	public boolean isLeaf() {
		return nodeName.toLowerCase().startsWith("leaf");
	}
	
	public TreeNode getMostChild() {
		int max = 0;
		TreeNode result = null;
		for (TreeNode c:children) {
			if (c.getSize()>max) {
				max = c.getSize();
				result = c;
			}
		}
		return result;
	}
	
	public TreeNode findChild(String data, boolean isDiscrete){
		if (data.equals("?")) {
			return getMostChild();
		}
		if (!isDiscrete) {
			String cmp = getPath().get(0).replace("<=", "");
			if (Tool.cmp(data, cmp)) {
				return children.get(0);
			}
			return children.get(1);
		} else {
			for (int i=0; i<path.size(); i++) {
				String cmp = path.get(i).replace("=", "");
				if (Tool.equal(data, cmp)) {
					return children.get(i);
				}
			}
		}
		return getMostChild();
	}
	
	public String mostTargetValue() {
		Iterator<Entry<String, Long>> it = targetValueMap.entrySet().iterator();
		String returnMessage = null;
		long max = Long.MIN_VALUE;
		while (it.hasNext()) {
			Entry<String,Long> e = it.next();
			if (e.getValue()>max) {
				max = e.getValue();
				returnMessage = e.getKey();
			}
		}
		return returnMessage;
	}
}
