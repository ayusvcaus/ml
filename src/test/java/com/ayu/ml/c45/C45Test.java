package com.ayu.ml.c45;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.util.List;

import org.junit.Test;

import com.ayu.ml.c45.ConfigReader;
import com.ayu.ml.c45.EntryBuilder;
import com.ayu.ml.c45.SchemaBuilder;
import com.ayu.ml.c45.DataEntry;
import com.ayu.ml.c45.Schema;
import com.ayu.ml.c45.TreeNode;

public class C45Test{
	
	@Test
	public void testC45() throws Exception {
		String FileName = ConfigReader.getTestFile();
		TreeNode node = null;
		if (!new File(Const.DATA_PATH+(ConfigReader.isPrune()?"Prune_":"")+ConfigReader.getModelFile()).exists()) {
			node = Train.BuildModel();
		} else {
			ObjectInputStream reader = new ObjectInputStream(new FileInputStream(Const.DATA_PATH+(ConfigReader.isPrune()?"Prune_":"")+ConfigReader.getModelFile()));
			node = (TreeNode)reader.readObject();
			reader.close();
		}
		System.out.println("Loading Model Finished");
		SchemaBuilder sbuilder = new SchemaBuilder();
		Schema schema = sbuilder.buildSchema(Const.DATA_PATH+FileName);
		EntryBuilder ebuilder = new EntryBuilder();
		List<DataEntry> data = ebuilder.loadEntry(Const.DATA_PATH+FileName, schema);
		System.out.println("FillData");
		System.out.println("filling size:"+data.size());
		fillData(data,node,schema);
		BufferedWriter writer = new BufferedWriter(new FileWriter(Const.DATA_PATH+"out_"+FileName));
		StringBuffer buf = new StringBuffer(schema.toString()+"\n");
		for (DataEntry d:data) {
			buf.append(d.toString()+"\n");
		}
		writer.write(buf.toString());
		writer.close();
		
	}
	
	public static void fillData(List<DataEntry> data, TreeNode root, Schema schema) {
		for (DataEntry d : data) {
			TreeNode node = root;
			while (!node.isLeaf()) {
				node = node.findChild(d.getData(node.getIndex()), schema.isDiscrete(node.getIndex()));
			}
			String finalResult = node.getValue();
			d.setData(d.length()-1, finalResult);
		}
	}
}
