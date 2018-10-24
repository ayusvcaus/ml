package com.ayu.ml.nb;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


public class ContentTokenizer {

    public static Map<String, Long> segStr(String content){

        Map<String, Long> words = new LinkedHashMap<String, Long>();
        content = content.replaceAll("[,;\\.!$\"\']", "");
        
        String[] strs = content.split(" ");
        Arrays.stream(strs).forEach(s->{
        	try {
        		words.put(s, words.get(s));
        	} catch (Exception e) {
        		words.put(s, 1l);
        	}
        });
        return words;
   
    }
}
