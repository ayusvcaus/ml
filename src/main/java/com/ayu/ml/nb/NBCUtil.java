package com.ayu.ml.nb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ayu.ml.nb.ArffUtil;


/**
 * NBC means Naive Bayes Classifier
 * @author wenjun_yang
 *
 */
public class NBCUtil {
	
	ArffUtil util = new ArffUtil();
	private List<String> attributeList = null;
	private List<String[]> dataList = null;
	private String decAttributeName = null;
	private int decAttributeIndex = -1;
	
	private Map<String, List<String[]>> seperatedDataTable = null;
	
	public NBCUtil(String decAttributeName, List<String> attributeList, List<String[]> dataList) {
		this.attributeList = attributeList;
		this.dataList = dataList;
		this.decAttributeName = decAttributeName;
		
		this.decAttributeIndex = util.getValueIndex(decAttributeName, this.attributeList);
		this.seperatedDataTable = seperateDataList(dataList);
	}
	
	private Map<String, List<String[]>> seperateDataList(List<String[]> dataList) {
		Map<String, List<String[]>> map = new HashMap<String, List<String[]>>();
		
		for(String[] arr : dataList) {
			if(decAttributeIndex >= 0 && decAttributeIndex < arr.length) {
				String currentKey = arr[decAttributeIndex]; 
				if(map.containsKey(currentKey)) {
					List<String[]> tempList = map.get(currentKey);
					tempList.add(arr);
					map.put(currentKey, tempList);
				} else {
					List<String[]> tempList = new ArrayList<String[]>();
					tempList.add(arr);
					map.put(currentKey , tempList);
				}
			}
		}
		
		return map;
	}
	
	public Boolean predict(Map<String, String> predictData, String targetDecAttributeValue) {
		if(predictData.containsKey(decAttributeName)) predictData.remove(decAttributeName);
		
		List<String[]> positiveDataTable = new ArrayList<String[]>();
		if(seperatedDataTable.containsKey(targetDecAttributeValue)) {
			positiveDataTable = seperatedDataTable.get(targetDecAttributeValue);
		}
		
		double resultP = 1.;
		
		// Step 1: 閫愪釜灞炴�х殑姣旂巼杩涜璁＄畻
		// 鍗筹細 璁＄畻 P(Attr=Value|Y=true) / P(Attr=Value|Y=false) 鐨勫��
		for(String attrName : predictData.keySet()) {
			String attrValue = predictData.get(attrName);
			int attrIndex = util.getValueIndex(attrName, attributeList);
			int attrPositiveCount = 0;
			int attrNegativeCount = 0;
			
			for(String[] arr : dataList) {
				if(arr[attrIndex].equals(attrValue)) {
					if(arr[decAttributeIndex].equals(targetDecAttributeValue)) {
						attrPositiveCount++;
					} else {
						attrNegativeCount++;
					}
				}
			}
			double temp =  (attrPositiveCount / (double)positiveDataTable.size() ) /
							(attrNegativeCount / (double)(dataList.size() - positiveDataTable.size()));
			resultP *= temp;
		}
		// 鏈�鍚庤绠� P(Y=true) / P(Y=false)
		resultP *= positiveDataTable.size() / (double)(dataList.size() - positiveDataTable.size());
		System.out.println(resultP);
		if(resultP > 1) {
			return true;
		} else {
			return false;
		}
	}
}
