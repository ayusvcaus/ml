package com.ayu.ml.kmeans;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ayu.ml.kmeans.Datum;
import com.ayu.ml.kmeans.PhotoReader;
import com.ayu.ml.kmeans.PhotoPrinter;
import com.ayu.ml.kmeans.ClusterBuilder;

public class PhotoKmeans {
	
	public static Datum[] center;
	public static Datum[][] data;

	public static void main(String[] args) throws Exception {

		String path = "data/kmeans/huiling1.jpg";

		long t1 = System.currentTimeMillis();
		int[] K = new int[1];
		data = read(path);
		int k = 200;
		System.out.println("k=" + k);
		
		long t2 = System.currentTimeMillis();
		System.out.println("Time2 consumed=" + (t2 - t1));
		initCenterCluster(k, data, center=new Datum[k]);
	    doKmeans(data, center);
	    long t3 = System.currentTimeMillis();
	    System.out.println("Time2 consumed=" + (t3 - t2));
	    int idx = path.lastIndexOf("/") +1;
	    
	    print(path.substring(0, idx) + k + "-" + path.substring(idx), data, center);
	    
	    long t4 = System.currentTimeMillis();
	    System.out.println("Time consumed=" + (t4 - t3));
		System.out.println("Time consumed=" + (t4 - t1));
	}
	
	public static void  doKmeans(Datum[][] data, Datum[] center) {
		do {
			buildClusters(data, center);
		} while (center.length>buildCenters(center));
	}

	
	public static void initCenterCluster(int k, Datum[][] data, Datum[] center) {
		for (int i=0; i<k; i++) {
			center[i] = new Datum();
			int width = (int)(Math.random()*data.length);
			int height = (int)(Math.random()*data[0].length);
			center[i].cluster = i;
			center[i].red = data[width][height].red;
			center[i].green = data[width][height].green;
			center[i].blue = data[width][height].blue;
		}
	}
	
	public static void buildClusters(Datum[][] data, Datum[] center) {		
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for (int p=0; p<data.length; p++) {
			executor.execute(new ClusterBuilder(data, center, p));
		}
		executor.shutdown();
		while (!executor.isTerminated());
	}
	
	public static int buildCenters(Datum[] center) {
		int sum = 0;	
		for (int i=0; i<center.length; i++) {
			if (center[i].totalCluster>0) {
				int newRed = (int)center[i].totalRed/center[i].totalCluster;
				int newGreen = (int)center[i].totalGreen/center[i].totalCluster;
				int newBlue = (int)center[i].totalBlue/center[i].totalCluster;
			    
			    if (center[i].red!=newRed || center[i].green!=newGreen || center[i].blue!=newBlue ) {
			    
				    center[i].red = newRed;
				    center[i].green = newGreen;
				    center[i].blue = newBlue;
				    continue;
			    } 
			}
			sum++;
		}
		return sum;
	}
	
	public static Datum[][] read(String path) {
		BufferedImage bi = null;
		try {		
			bi = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int width = bi.getWidth();
		int height = bi.getHeight();
		System.out.println("width="+ width + ", height=" + height);
		
		Datum[][] localData = new Datum[width][height];
				
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for (int i=0; i<width; i++) {
			executor.execute(new PhotoReader(localData, i, bi));
		}
		executor.shutdown();
		while (!executor.isTerminated());
		return localData;
	}
	
	public static void  print(String path, Datum[][] data, Datum[] center) {
		BufferedImage nbi = new BufferedImage(data.length, data[0].length, BufferedImage.TYPE_INT_RGB);
		
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for (int i=0; i<data.length; i++) {
			executor.execute(new PhotoPrinter(nbi, data, center, i));
		}
		executor.shutdown();
		while (!executor.isTerminated());

		try {
			ImageIO.write(nbi, "jpg", new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
