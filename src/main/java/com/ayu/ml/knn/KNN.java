package com.ayu.ml.knn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Random;

import java.util.stream.IntStream;

public class KNN {

	public static ReturnML rml = new ReturnML();
	

	/**
	 * @param args
	 * @throws IOException
	 * @author haolidong
	 * @Description:  [主函数主要对于三个案例进行测试，分别为简单分类，约会测试以及手写识别]  
	 */
	public static void main(String[] args) throws IOException {
		
		  //int num = Runtime.getRuntime().availableProcessors();
		  //System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", Integer.toString(new Random().nextInt(5) + 1));
		  //int[] arr = {1, 2, 3, 4};
		  
		  //Arrays.stream(arr).parallel().forEach(i->{});;
		  
		long t0 = System.currentTimeMillis();
		testClassify();
		long t1 = System.currentTimeMillis();
		//datingClassify();
		handwritingClassify();
		long t2 = System.currentTimeMillis();
		//handwritingClassify();
		datingClassify();
		long t3 = System.currentTimeMillis();
		
		System.out.println("t1-t0="+(t1-t0));
		System.out.println("t2-t1="+(t2-t1));
		System.out.println("t3-t2="+(t3-t2));
		System.out.println("t3-t0="+(t3-t0));
		
		//test();
	}
	
	/**
	 * @author haolidong
	 * @Description:  [简单的通过文本文件创建二维矩阵并输出]  
	 */
	public static void test(){
		file2matrix("data/knn/datingTestSet2.txt");
		autoNorm();
		for (int i = 0; i < rml.AR.size(); i++) {
			System.out.print(i+":    ");
			for (int j = 0; j < rml.AR.get(i).size(); j++) {
				System.out.print(rml.AR.get(i).get(j)+"          ");
			}
			System.out.println(rml.AS.get(i));
		}
	}
	/**
	 * @return 返回标签号
	 * @author haolidong
	 * @Description:  [函数主要对于KNN的简单分类]  
	 */
	public static String testClassify(){

		List<Double> input = new ArrayList() {{
		    add(0.0);
		    add(0.0);
		}};
		List<Double> a1 = new ArrayList() {{
		    add(1.0);
		    add(1.1);
		}};
		List<Double> a2 = new ArrayList() {{
		    add(1.0);
		    add(1.0);
		}};
		List<Double> a3 = new ArrayList() {{
		    add(0.0);
		    add(0.0);
		}};
		List<Double> a4 = new ArrayList() {{
		    add(0.0);
		    add(0.1);
		}};
		List<List<Double>> group = new ArrayList() {{
		    add(a1);
		    add(a2);
		    add(a3);
		    add(a4);
		}};
		List<String> labels = new ArrayList() {{
		    add("A");
		    add("A");
		    add("B");
		    add("B");
		}};
		
		String lab=classify(input, group, labels, 3);
		System.out.println(lab);
		return lab;
	}

	/**
	 * @param inX     测试用例的输入
	 * @param dataSet 训练数据矩阵
	 * @param labels  训练数据标签
	 * @param k       kNN中对于前面K项的排名
	 * @return        测试用例的标签
	 * @author haolidong
	 * @Description:  [KNN的核心分类算法]  
	 */
	public static String classify(List<Double> inX, List<List<Double>> dataSet, List<String> labels, int k) {
		
		List<Distances> dis = new ArrayList();
	    IntStream.range(0, dataSet.size()).forEach(i->{	
            List<Double> d = dataSet.get(i);
	    	double[] sum = {0.0};
			IntStream.range(0, d.size()).forEach(j->{
				double z = inX.get(j)-d.get(j);
				sum[0] +=z*z;
			});
			dis.add(new Distances(sum[0], i));
		});
	    Collections.sort(dis);

		Map<String,Integer> classCount = new HashMap();
		
    	IntStream.range(0, k).forEach(i->{
			String voteIlabel = labels.get(dis.get(i).getSortedDistIndicies());
        	try {
        		classCount.put(voteIlabel, classCount.get(voteIlabel)+1); 
        	} catch (Exception e) {
        		classCount.put(voteIlabel, 1);
        	}		
		});
				
    	List<Entry<String, Integer>> list = sortMap(classCount);

    	return list.get(0).getKey();

	}
	
	public static List<Entry<String, Integer>> sortMap(Map<String,Integer> map) {  
        List<Entry<String, Integer>> list = new ArrayList(map.entrySet());  
        Collections.sort(list, new Comparator<Entry<String, Integer>>() {  
            @Override  
            public int compare(Entry<String, Integer> arg0, Entry<String, Integer> arg1) {  
                return arg1.getValue() - arg0.getValue();  
            }  
        });
        return list;   
	}
	
	
	/**
	 * @param fileName  文件名
	 * @author haolidong
	 * @Description:  [读入文件然后转化为数组矩阵]  
	 */
	public static void file2matrix(final String fileName){
		 	File file = new File(fileName);
	        BufferedReader reader = null;
	        try {
	            reader = new BufferedReader(new FileReader(file));
	            String tempString = null;
	            // 一次读入一行，直到读入null为文件结束
	            while ((tempString = reader.readLine()) != null) {
	                // 显示行号
	                String[] strArr = tempString.split("\t");
	                List<Double> ad = new ArrayList();
	                /*for (int i = 0; i < strArr.length-1; i++) {
						ad.add(Double.parseDouble(strArr[i]));
					}*/
	                IntStream.range(0, strArr.length-1).forEach(i->{ad.add(Double.parseDouble(strArr[i]));});
	                rml.AR.add(ad);
	                rml.AS.add(new String(strArr[strArr.length-1]));
	            }
	            reader.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e1) {
	                }
	            }
	        }
	}
	
	/**
	 * @author haolidong
	 * @Description:  [对于输入矩阵的归一化：X:(X-min)/(max-min)] 
	 */
	public static void autoNorm(){
		List<Double> min = new ArrayList();
		List<Double> max = new ArrayList();
		List<Double> range = new ArrayList();

		IntStream.range(0, rml.AR.get(0).size()).forEach(i->{
			min.add(rml.AR.get(0).get(i));
			max.add(rml.AR.get(0).get(i));
		});

		rml.AR.forEach(d->{
			IntStream.range(0, d.size()).forEach(i->{
				if(d.get(i)>max.get(i)){
					max.set(i, d.get(i));
				}
				if(d.get(i)<min.get(i)){
					min.set(i, d.get(i));
				}
			});
		});

		IntStream.range(0, rml.AR.get(0).size()).forEach(i->{
			range.add(max.get(i)-min.get(i));
		});

		rml.AR.forEach(d->{
			IntStream.range(0, d.size()).forEach(i->{
				d.set(i, (d.get(i) - min.get(i))/range.get(i));
			});
		});
	}
	/**
	 * @author haolidong
	 * @Description:  [约会的分类案例]  
	 */
	public static void datingClassify(){
		//double hoRatio = 0.50;
		file2matrix("data/knn/datingTestSet2.txt");
		
		autoNorm();
		
		int numTestVecs = (int) (rml.AR.size()* 0.50);
		
		List<List<Double>> group = new ArrayList();
		List<String> labels = new ArrayList();
		
		//autoNorm();

		IntStream.range(0, rml.AR.size()-numTestVecs).forEach(i->{
			List<Double> ad = new ArrayList<Double>();
			IntStream.range(0, rml.AR.get(i).size()).forEach(j->{
				ad.add(rml.AR.get(i+numTestVecs).get(j));
			});
			group.add(ad);
			labels.add(rml.AS.get(i+numTestVecs));
		});
		
		//System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", Integer.toString(numTestVecs));
		int[] errorCount1 = {0};
		IntStream.range(0, numTestVecs).parallel().forEach(i->{
			String s1 = classify(rml.AR.get(i), group,labels,3);
			String s2= rml.AS.get(i).trim();
			//System.out.println("the classifier came back with: "+s1+"  the real answer is: "+s2);
			if (!s1.equals(s2)) {
				errorCount1[0]++;
			}		
		});
		System.out.println("the total error rate is:"+1.0*errorCount1[0]/numTestVecs);
	}
	
	/**
	 * @param file 输入的二进制图片文件
	 * @return 返回图像矩阵
	 * @author haolidong
	 * @Description:  [二进制图片文件转化为图像矩阵] 
	 */
	public static List<Double> img2vector(File file){
		List<Double> ad = new ArrayList<Double>();
		//File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            
            while ((tempString = reader.readLine()) != null) { 
            	char[] ch=tempString.toCharArray();
                IntStream.range(0, ch.length).forEach(i->{
                	double d=Integer.parseInt(ch[i]+"");
                	ad.add(d);
                });
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
		return ad;
		
	}
	
	/**
	 * @throws IOException
	 * @author haolidong
	 * @Description:  [手写识别] 
	 */
	public static void handwritingClassify() throws IOException
	{
		
		List<List<Double>> vectorUnderTest = new ArrayList();
		List<List<Double>> trainingMat = new ArrayList();
		List<String> trainingLabel = new ArrayList();
		List<String> testLabel = new ArrayList();
		
		 String pathTest="data/knn/testDigits/";
		 String pathTrain="data/knn/trainingDigits/";
		  File fileTrain=new File(pathTrain);
		  File fileTest=new File(pathTest);
		  File[] trainList = fileTrain.listFiles();
		  File[] testList = fileTest.listFiles();

		  IntStream.range(0, trainList.length).forEach(i->{
			  if (trainList[i].isFile()) {
				  List<Double> ad = img2vector(trainList[i]);
				  String path = "";
				  try {
				      path=trainList[i].getCanonicalPath();
				  } catch (Exception e) {
					  System.out.println("Other exception:"+e.toString());
				  }
				  trainingLabel.add(path.substring(path.lastIndexOf('\\')+1, path.indexOf('_')));
				  trainingMat.add(ad);
			  }
          });
		  
		  IntStream.range(0, testList.length).forEach(i->{
			  if (testList[i].isFile()) {
				  List<Double> ad = img2vector(testList[i]);
				  String path = "";
				  try {
				      path=testList[i].getCanonicalPath();
				  } catch (Exception e) {
					  System.out.println("Other exception:"+e.toString());
				  }
				  testLabel.add(path.substring(path.lastIndexOf('\\')+1, path.indexOf('_')));
				  vectorUnderTest.add(ad);
			  }
          });		  

		  int[] errorCount = {0};
		  long t5 = System.currentTimeMillis();
		  
		  IntStream.range(0, testList.length).parallel().forEach(i->{		  
			  String classifyLabel = classify(vectorUnderTest.get(i), trainingMat, trainingLabel, 3);
			  //System.out.println("the classifier came back with:"+classifyLabel+", the real answer is:"+testLabel.get(i));
			  if(!classifyLabel.equals(testLabel.get(i))){
				  errorCount[0]++;
			  }
          });
	
		  long t6 = System.currentTimeMillis();
		  System.out.println("t6-t5="+(t6-t5));

		  System.out.println("testList.lengths:"+testList.length);
		  System.out.println("the total number of errors is:"+errorCount[0]);
		  System.out.println("the total error rate is: "+1.0*errorCount[0]/testList.length);
	}
}