package com.ayu.ml.nb;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class NaiveBayesMain {

	public static final String DEFAULT_DIR="data/nb/";
	
	public static void main(String[] args){
		TrainSampleDataManager.process();
		String s="Twenty-eight-year-old. ! David Wright was found guilty in October of five criminal charges for planning with his uncle and a friend to travel to New York to attempt to behead conservative blogger Pamela Geller, who angered Muslims by organizing a Prophet Muhammad cartoon contest.";
		Set<String> words= ContentTokenizer.segStr(s).keySet();	
		Map<String, BigDecimal> resultMap = MultinomialModelNaiveBayes.classifyResult(DefaultStopWordsHandler.dropStopWords(words));
		Set<String> set=resultMap.keySet();
		for(String str: set){
			System.out.println("classifer:"+str+"     probability:"+resultMap.get(str));
		}
		System.out.println("The final result:"+MultinomialModelNaiveBayes.getClassifyResultName());
	}
}
