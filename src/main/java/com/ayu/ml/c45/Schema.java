package com.ayu.ml.c45;

import java.util.List;
import java.util.ArrayList;

import com.ayu.ml.c45.Const;

public class Schema {
	
	private List<String> attributesName;
	private List<Character> type;
	private List<Character> dataType;
	
	public Schema(){
		attributesName = new ArrayList<>();
		type = new ArrayList<>();
		//To determine is discrete or continuous...
		dataType = new ArrayList<>();
	}
	
	public void addAttributesName(String name){
		attributesName.add(name);
	}
	
	public void addType(Character t){
		type.add(t);
	}
	
	public void addDataType(Character T){
		dataType.add(T);
	}
	
	public void setDataType(int index,Character T){
		dataType.set(index, T);
	}
	
	public char getType(int order){
		return type.get(order);
	}
	
	public char getDataType(int idx){
		return dataType.get(idx);
	}
	
	public String getName(int order){
		return attributesName.get(order);
	}
	
	public boolean isDiscrete(int index){
		return dataType.get(index)==Const.DISCRETE;
	}
	
	public int length(){
		return attributesName.size();
	}
	
	public void showSchema(){
		System.out.println("===================");
		for(int i=0;i<attributesName.size();i++){
			System.out.println(i+" Attributes: "+attributesName.get(i)+" Type: "+type.get(i)+" IsDiscrete:"+isDiscrete(i));
		}
		System.out.println("====================");
	}
	
	public String toString(){
		
		if (attributesName.size()==0) {
			return "";
		}
		String value = attributesName.get(0);
		for (int i=1; i<attributesName.size(); i++){
			value +=","+attributesName.get(i);
		}
		return value;
	}
}
