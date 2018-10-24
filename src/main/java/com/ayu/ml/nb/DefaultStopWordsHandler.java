package com.ayu.ml.nb;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * <p>Title:DefaultStopWordsHandler</p>
 * <p>Description: 鏄惁涓哄仠鐢ㄨ瘝妫�娴�
 * </p>
 * @createDate锛�2013-8-30
 * @author xq
 * @version 1.0
 */
public class DefaultStopWordsHandler {

	private static Set<String> stopWordsSet =new HashSet() { {
		add("and");
		add("or");
		add("this");
		add("that");
		add("thus");
		add("yet");
		add("is");
		add("are");
		add("do");
		add("does");
		add("did");
		add("done");
		add("of");
		add("on");
		add("in");
		add("at");
		add("the");
		add("up");
		add("upon");
		add("get");
		add("what");
		add("where");
		add("how");
		add("so");
		add("to");
		add("forward");
		add("back");
		add("down");
		add("be");
		add("was");
		add("were");
		add("got");
		add("gotten");
		add("will");
		add("should");
		add("shall");
	}};

	public static boolean isStopWord(String word) {
		return stopWordsSet.contains(word);
	}
	
	public static Set<String> dropStopWords(Set<String> oldWords){
		Set<String> set = new HashSet<String>();
		for (String word: oldWords)		{
			if (!isStopWord(word)){
				set.add(word);
			}
		}
		return set;
	}
}
