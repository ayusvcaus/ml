package com.ayu.ml.c45;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import com.ayu.ml.c45.ConfigReader;
import com.ayu.ml.c45.EntryBuilder;
import com.ayu.ml.c45.SchemaBuilder;
import com.ayu.ml.c45.TreeTools;
import com.ayu.ml.c45.DataEntry;
import com.ayu.ml.c45.Schema;
import com.ayu.ml.c45.TreeNode;

public class Train {
	
	public static void saveModel(TreeNode node) throws Exception {
		//TreeNode node = BuildModel();
		//TreeTools.printTree(node);
		//TreeTools.printDNF(node);
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(Const.DATA_PATH+ConfigReader.getModelFile()));
		os.writeObject(node);
		os.close();
	}
	
	public static TreeNode BuildModel() {
		try {
			System.out.println("=====Building Model===========");
			SchemaBuilder sbuilder = new SchemaBuilder();
			EntryBuilder ebuilder = new EntryBuilder();
			String FileName = ConfigReader.getTrainFile();
			Schema schema = sbuilder.buildSchema(Const.DATA_PATH+FileName);
			Set<Integer> set = new HashSet<Integer>();
			for (int i=0; i<schema.length()-1; i++) {
				set.add(i);
			}
			List<DataEntry> e = ebuilder.buildEntry(Const.DATA_PATH+FileName, schema, true);
			TreeNode root = TreeTools.buildTree(schema, e, set);
			System.out.println("=====finish building...");
			System.out.println("Training Data size:"+e.size());
			//System.out.println("Training Model Accuracy:"+Validate.doValid(e, root, schema)*100+"%");
			saveModel(root);
			return root;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
	}
	
	public static TreeNode buildModel(List<DataEntry> data, Schema schema) throws Exception{
		System.out.println("=====Building Model===========");
		Set<Integer> set = new HashSet<>();
		for (int i=0; i<schema.length()-1; i++) {
			set.add(i);
		}
		TreeNode root = TreeTools.buildTree(schema, data, set);
		System.out.println("=====finish building...");
		return root;
	}
}
