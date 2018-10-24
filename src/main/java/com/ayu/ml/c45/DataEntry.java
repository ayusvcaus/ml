package com.ayu.ml.c45;

import java.util.List;
import java.util.ArrayList;

public class DataEntry {
	
	private List<String> data;
	
	public DataEntry () {
		data = new ArrayList<>();
	}
	
	public void insertEntry(String s){
		data.add(s);
	}
	
	public String getData(int idx){
		return data.get(idx);
	}
	
	public void setData(int order, String value){
		data.set(order, value);
	}
	
	public int length(){
		return data.size();
	}
	
	public String toDataString(){
		String returnMessage = "(";
		for (String s:data) {
			returnMessage +=" "+s+" ";
		}
		returnMessage +=")";
		return returnMessage;
	}
	
	public String toString(){
		String returnMessage = "";
		if (data.size()==0) {
			return returnMessage;
		}
		returnMessage = data.get(0);
		for (int i=1; i<data.size(); i++) {
			returnMessage +=","+data.get(i);
		}
		return returnMessage;
	}
}
