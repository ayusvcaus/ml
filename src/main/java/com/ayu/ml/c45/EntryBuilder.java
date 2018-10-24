package com.ayu.ml.c45;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.HashSet;

import com.ayu.ml.c45.Const;
import com.ayu.ml.c45.DataEntry;
import com.ayu.ml.c45.Schema;

public class EntryBuilder {
	
	//this is used to judge the field is discrete or not..
	public List<DataEntry>buildEntry(String FileName, Schema schema, boolean isTrain) throws Exception{
		List<DataEntry> result = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(FileName));
		//we assume only the integer can be discrete.. 
		List<Set<String>> sets = new ArrayList<Set<String>>();
		for (int i=0; i<schema.length(); i++) {
			sets.add(new HashSet<String>());
		}
		//skip the first line.
		reader.readLine();
		String s = "";
		while ((s=reader.readLine())!=null) {
			DataEntry entry = new DataEntry();
			String []record = s.split(",");
			if (record[record.length-1].equals("?")&&isTrain) {
				//those data do not help
				continue;
			}
			for (int i = 0; i<record.length; i++) {
				entry.insertEntry(record[i]);
				sets.get(i).add(record[i]);
			}
			result.add(entry);
		}
		for (int i=0; i<schema.length(); i++) {
			if (sets.get(i).size()<ConfigReader.getDiscreteThreshold()) {
				schema.setDataType(i, Const.DISCRETE);
			} else {
				schema.setDataType(i, Const.CONTINUOUS);
			}
		}
		reader.close();
		if (isTrain){
			int trainingSize = result.size()* (int)ConfigReader.getLearningSize();
			Set<Integer> scope =  ThreadLocalRandom.current().ints(0, trainingSize).distinct().limit(result.size()).boxed().collect(Collectors.toSet());
			//result = new ArrayList<>(result.subList(0, (int)(result.size()*ConfigReader.getLearningSize())));
			List<DataEntry> result2 = new ArrayList<>();
			for (Integer i: scope) {
				result2.add(result.get(i));
			}
			return result2;
		}
		return result;
	}
	
	//this part is for test data
	public List<DataEntry>loadEntry(String FileName, Schema schema) throws Exception{
		List<DataEntry> result = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(FileName));
		//we assume only the integer can be discrete.. 
		List<Set<String>> sets = new ArrayList<Set<String>>();
		for (int i=0; i<schema.length(); i++) {
			sets.add(new HashSet<String>());
		}
		//skip the first line.
		reader.readLine();
		String s = "";
		while ((s=reader.readLine())!=null) {
			DataEntry entry = new DataEntry();
			String[] record = s.split(",");
			for (int i = 0; i<record.length; i++){
				entry.insertEntry(record[i]);
				sets.get(i).add(record[i]);
			}
			result.add(entry);
		}
		for (int i=0; i<schema.length(); i++){
			if (sets.get(i).size()<ConfigReader.getDiscreteThreshold()) {
				schema.setDataType(i, Const.DISCRETE);
			} else {
				schema.setDataType(i, Const.CONTINUOUS);
			}
		}
		reader.close();
		return result;
	}
}
