package com.ayu.ml.nb;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ayu.ml.nb.ArffUtil;
import com.ayu.ml.nb.NBCUtil;

import com.google.gson.Gson;


public class MyNB {

	public static void main(String[] args) {

		ArffUtil util = new ArffUtil();
		File arff = new File("D:\\Program Files\\Weka-3-7\\data\\weather.nominal.arff");
		Map<String, List<String>> optionMap = new HashMap<String, List<String>>();
		List<String> attrList = new ArrayList<String>();
		List<String[]> dataList = util.getData(arff);
		util.retrieveAttributes(arff, optionMap, attrList);

		Map<String, String> predictData = new HashMap<String, String>();
		predictData.put("outlook", "sunny");
		predictData.put("temperature", "mild");
		predictData.put("humidity", "normal");
		predictData.put("windy", "TRUE");
		
		NBCUtil nbcUtil = new NBCUtil("play", attrList, dataList);
		System.out.println(nbcUtil.predict(predictData, "yes"));;		
	}

}
