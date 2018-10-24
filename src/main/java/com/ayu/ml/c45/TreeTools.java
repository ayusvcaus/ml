package com.ayu.ml.c45;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.ayu.ml.c45.DataEntry;
import com.ayu.ml.c45.Schema;
import com.ayu.ml.c45.SplitResult;
import com.ayu.ml.c45.TreeNode;

public class TreeTools {
	
	private static int count = 0;
	
	public static TreeNode buildTree(Schema schema, List<DataEntry> data, Set<Integer> attributes) throws Exception{
		TreeNode node = new TreeNode();
		node.setSize(data.size());
		node.setTargetValueMap(EntropyTools.getTargetValueMap(data));
		if (EntropyTools.isPure(data)||attributes.size()==0) {
			node.setNodeName("LeafNode");
			node.setValue(EntropyTools.getMostTargetValue(data));
			return node;
		}
		SplitResult best = null;
		double gainInfoRatio = 0;
		int bestAttributes = -1;
		for (Integer attributesNum:attributes) {

			SplitResult result = EntropyTools.getSplitResult(attributesNum, schema, data);
			//System.out.println(schema.getName(attributesNum)+","+result.getInfoGain());
			if (result.getInfoGainRatio()>gainInfoRatio) {
				best = result;
				gainInfoRatio = result.getInfoGainRatio();
				bestAttributes = attributesNum;
			}
		}
		//Can not split anymore, the rest attributes is helpless
		if (gainInfoRatio==0) {
			node.setNodeName("LeafNode");
			node.setValue(EntropyTools.getMostTargetValue(data));
			return node;
		}
		//System.out.println("Won:"+schema.getName(BestAttributes)+","+best.getInfoGain());
		node.setIndex(bestAttributes);
		node.setNodeName(schema.getName(bestAttributes));
		//attributes.remove(BestAttributes);
		for (int i=0; i<best.length(); i++) {
			node.addPath(best.getDescribe(i));
			node.addChildren(buildTree(schema, new ArrayList<>(best.getSplitData(i)), new HashSet<>(attributes)));
		}
		//attributes.add(BestAttributes);
		return node;
	}
	
	public static void printTree(TreeNode root) {
		if (root==null) {
			return;
		}
		Queue<TreeNode> queue = new LinkedList<>();
		queue.add(root);
		int level = 1;
		int rest =  0;
		while (!queue.isEmpty()) {
			TreeNode node = queue.poll();
			level--;
			if (node.getNodeName().toLowerCase().startsWith("leaf")) {
				System.out.print(" "+node.getValue());
			} else {
				System.out.print(" "+node.getNodeName()+node.showPath());
			}
			for (TreeNode n:node.getChildren()) {
				queue.add(n);
				rest++;
			}
			if (level==0) {
				level = rest;
				rest = 0;
				System.out.println();
			}
		}
		return;
	}
	
	public static void printDNF(TreeNode root) {
		count = 0;
		dfs(root, new ArrayList<>());
	}
	
	private static boolean dfs(TreeNode root, List<String> path) {
		if (count>16) {
			return false;
		}
		if (root.isLeaf()) {
			count +=1;
			System.out.print("If: "+path.get(0));
			for (int i=1; i<path.size(); i++) {
				System.out.print("&"+path.get(i));
			}
			System.out.println();
			System.out.println("Then:"+root.getValue());
		} else {
			for (int i=0; i<root.getChildren().size(); i++) {
				path.add(root.getNodeName()+root.getPath(i));
				boolean result = dfs(root.getChildren().get(i), path);
				path.remove(path.size()-1);
				if (!result) {
					break;
				}
				
			}
		}
		if (count>16) {
			return false;
		}
		return true;
	}
}
