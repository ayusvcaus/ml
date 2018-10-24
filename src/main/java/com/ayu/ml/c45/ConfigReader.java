package com.ayu.ml.c45;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.ayu.ml.c45.Const;

public class ConfigReader {
	
	private static Properties p;
	
	static {
		try {
		    p = new Properties();
		    FileInputStream inputStream = new FileInputStream(new File(Const.DATA_PATH+Const.CONFIGURATION));
		    p.load(inputStream);
		    inputStream.close();
		} catch (IOException e) {
			
		}
	}
	
	public static float getAccuracy() throws Exception {
		return  Float.parseFloat((String)p.get("ACCURACY"));
	}
	
	public static float getPruningSize() throws Exception {
		return Float.parseFloat((String)p.get("TRAININGSIZE"));
	}
	
	public static float getLearningSize() throws Exception {
		return Float.parseFloat((String)p.get("learnigsize"));
	}
	
	public static boolean isPrune() throws Exception {
		return ((String)p.get("isPruendModel")).equals("1");
	}
	
	public static int getDiscreteThreshold() throws Exception {
		return Integer.parseInt((String) p.get("DISCRETE_THRESHOLD"));
	}
	
	public static String getTrainFile() throws Exception {
		return(String)p.get("TRAINFILE");
	}
	
	public static String getValidateFile() throws Exception {
		return(String)p.get("VALIDATEFILE");
	}
	
	public static String getTestFile() throws Exception {
		return(String)p.get("TestFILE");
	}
	
	public static String getModelFile() throws Exception {
		return(String)p.get("ModelName");
	}	
}
