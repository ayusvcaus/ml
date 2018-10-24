package com.ayu.ml.nb;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MultinomialModelNaiveBayes {

	private static Map<String,BigDecimal> classifierResult=new HashMap<String,BigDecimal>();
	
	private static BigDecimal zoomFactor = new BigDecimal(10);
	
	public static BigDecimal priorProbability(String classifier){
		BigDecimal molecular=new BigDecimal(TrainSampleDataManager.classWordCount(classifier));
		BigDecimal denominator=new BigDecimal(TrainSampleDataManager.sampleWordCount());
		return molecular.divide(denominator,10, BigDecimal.ROUND_CEILING);
	}

	public static BigDecimal classConditionalProbability(String classifier, String word){
		BigDecimal molecular = new BigDecimal(TrainSampleDataManager.wordInClassCount(word, classifier)+1);
		BigDecimal denominator = new BigDecimal(TrainSampleDataManager.classWordCount(classifier)+ TrainSampleDataManager.sampleWordKindCount());		
		return molecular.divide(denominator,10, BigDecimal.ROUND_CEILING);
	}

	public static Map<String,BigDecimal> classifyResult(Set<String> words){		
		Map<String,BigDecimal> resultMap=new HashMap<>();
		Set<String> classifierSet = TrainSampleDataManager.getAllClassifiers();
		for (String classifier: classifierSet) {
			BigDecimal probability = new BigDecimal(1.0);
			for (String word: words){
				probability = probability.multiply(classConditionalProbability(classifier, word)).multiply(zoomFactor);
			}
			resultMap.put(classifier, probability.multiply(priorProbability(classifier)));
		}
		classifierResult = resultMap;
		return resultMap;
	}
	
	public static String getClassifyResultName(){
		if(classifierResult.isEmpty()){
			return "N/A";
		}
		BigDecimal result=new BigDecimal(0);
		String classifierName="";
		Set<String> classifierSet=classifierResult.keySet();
		for(String classifier : classifierSet){
			BigDecimal classifierValue=classifierResult.get(classifier);
			if(classifierValue.compareTo(result)>=1){
				result=classifierValue;
				classifierName=classifier;
			}
		}
		return classifierName;
	}
}
