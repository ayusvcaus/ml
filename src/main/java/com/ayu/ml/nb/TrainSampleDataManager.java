package com.ayu.ml.nb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TrainSampleDataManager {

	private static final String SAMPLE_DATA=NaiveBayesMain.DEFAULT_DIR+"sample/";

	private static Long count=0L;

	private static Map<String,Map<String,Map<String,Long>> > allWordsMap=new HashMap<>();

	private static Long kindCount=0L;

	public static Long classWordCount(String classifier){
		Map<String,Map<String,Long>> classifierMap=allWordsMap.get(classifier);
		if(classifierMap==null){
			return 0L;
		}
		Set<String> articleSet=classifierMap.keySet();
		Long sum=0L;
		for(String article: articleSet){
			Map<String,Long> articleMap=classifierMap.get(article);
			Set<String> wordsSet=articleMap.keySet();
			for(String words: wordsSet){
				sum+=articleMap.get(words);
			}
		}
		return sum;
	}
	
	public static Long sampleWordCount(){
		if(count!=0L){
			return count;
		}
		Set<String> classifierSet=allWordsMap.keySet();
		Long sum=0L;
		for(String classifierName: classifierSet){
			Map<String,Map<String,Long>> classifierMap=allWordsMap.get(classifierName);
			Set<String> articleSet=classifierMap.keySet();
			for(String article: articleSet){
				Map<String,Long> articleMap=classifierMap.get(article);
				Set<String> wordsSet=articleMap.keySet();
				for(String words: wordsSet){
					sum+=articleMap.get(words);
				}
			}
		}
		count=sum;
		return count;
		
	}
	
	public static Long wordInClassCount(String word, String classifier){
		Long sum=0L;
		Map<String, Map<String,Long>> classifierMap=allWordsMap.get(classifier);
		Set<String> articleSet=classifierMap.keySet();
		for (String article: articleSet){
			Map<String,Long> articleMap=classifierMap.get(article);
			Long value= articleMap.get(word);
			if (value!=null && value>0){
				sum += articleMap.get(word);
			}		
		}
		return sum;
	}

    public static Long sampleWordKindCount(){
    	if(kindCount!=0L){
			return kindCount;
		}
		Set<String> classifierSet=allWordsMap.keySet();
		Long sum=0L;
		for(String classifierName: classifierSet){
			Map<String,Map<String,Long>> classifierMap=allWordsMap.get(classifierName);
			Set<String> articleSet=classifierMap.keySet();
			for(String article: articleSet){
				Map<String,Long> articleMap=classifierMap.get(article);
				sum+=articleMap.size();
			}
		}
		kindCount=sum;
		return kindCount;
	}
    
    public static List<String> readDirs(String filepath) throws FileNotFoundException, IOException {  
    	List<String> fileList = new ArrayList<>();
        try {  
            File file = new File(filepath);  
            if (!file.isDirectory()) {  
                System.out.println("xxxxx");  
                System.out.println("filepath: " + file.getAbsolutePath());
                fileList.add(file.getAbsolutePath());
            } else if (file.isDirectory()) {  
                String[] filelist = file.list();  
                for (int i = 0; i < filelist.length; i++) {  
                    File readfile = new File(filepath + File.separator + filelist[i]);  
                    if (!readfile.isDirectory()) {  
                        fileList.add(readfile.getAbsolutePath());  
                    } else if (readfile.isDirectory()) {  
                        readDirs(filepath + File.separator + filelist[i]);  
                    }  
                }  
            }  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();
        }  
        return fileList;  
    }
    
    public static List<String> readDirs(File file) throws FileNotFoundException, IOException {  
    	String filePah=file.getAbsolutePath();
    	return readDirs(filePah);
    }
    
    public static String readFile(String file) throws FileNotFoundException, IOException {  
        StringBuffer sb = new StringBuffer();  
        InputStreamReader is = new InputStreamReader(new FileInputStream(file));  
        BufferedReader br = new BufferedReader(is);  
        String line = br.readLine();  
        while (line != null) {  
            sb.append(line).append("\r\n");  
            line = br.readLine();  
        }  
        br.close();  
        return sb.toString();  
    }

    public static void process(){
    	try{
    		File sampleDataDir=new File(SAMPLE_DATA);
    		File[] fileList=sampleDataDir.listFiles();
    		if(fileList==null){
    			throw new IllegalArgumentException("Sample data is not exists!");
    		}
    		for (File file:fileList ){
    			List<String> classFileList=readDirs(file);
    			for (String article: classFileList) {
    				String content = readFile(article);
    				Map<String,Long> wordsMap = ContentTokenizer.segStr(content);
    				if (allWordsMap.containsKey(file.getName())) {
    					Map<String, Map<String, Long>> classifierValue = allWordsMap.get(file.getName());
    					classifierValue.put(article, wordsMap);
    					allWordsMap.put(file.getName(), classifierValue);
    				} else {
    					Map<String, Map<String,Long>> classifierValue = new HashMap<>();
    					classifierValue.put(article, wordsMap);
    					allWordsMap.put(file.getName(), classifierValue);
    				}
    			}
    		}
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    }

    public static Set<String> getAllClassifiers(){
    	return allWordsMap.keySet();
    }
}
