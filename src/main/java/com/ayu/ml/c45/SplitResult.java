package com.ayu.ml.c45;

import java.util.ArrayList;
import java.util.List;

public class SplitResult {
	
	private double gainRatio;
	private double infoGain;
	private String attributeName;
	private List<String> splitDescribe;
	private List<List<DataEntry>> splitData;
	
	public SplitResult(){
		splitDescribe = new ArrayList<>();
		splitData = new ArrayList<>();
	}
	
	public double getInfoGainRatio() {
		return gainRatio;
	}
	
	public void setInfoGainRatio(double gainRatio) {
		this.gainRatio = gainRatio;
	}
	
	public double getInfoGain() {
		return infoGain;
	}
	
	public void setInfoGain(double infoGain) {
		this.infoGain = infoGain;
	}
	
	public String getAttributeName() {
		return attributeName;
	}
	
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	
	public List<DataEntry> getSplitData(int index) {
		return splitData.get(index);
	}
	
	public void addData(List<DataEntry> splitData) {
		this.splitData.add(splitData);
	}
	
	public void addDescribe(String describe){
		splitDescribe.add(describe);
	}
	
	public String getDescribe(int index){
		return splitDescribe.get(index);
	}
	
	public int length(){
		return splitDescribe.size();
	}
	
	public String toString(){
		String returnMessage = "====================\n";
		returnMessage += attributeName+"\n";
		returnMessage+="InfoGain:"+infoGain+"\n";
		returnMessage+="IfnoGainRatio:"+gainRatio+"\n";
		returnMessage+= "Split on:\n";
		for(String s:splitDescribe){
			returnMessage+=" "+s;
		}
		returnMessage += "\n====================\n";
		return returnMessage;
	}
}
