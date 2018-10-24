package com.ayu.ml.kmeans;

import java.awt.Color;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

public class PhotoKMeans2 {
	
	public static class DataItem
	{
		public int r;
		public int g;
		public int b;
		public int group;
	}
	
	static DataItem[] center;
	static DataItem[] centerTotal;
	static DataItem[][] data;

	public static void main(String[] args) throws Exception
	{
		//String path = "/Users/allen.yu/Desktop/me.jpg";
		String path = "data/kmeans/huiling1.jpg";
	    doKmeans(path,200);
	}
	
	public static void  doKmeans(String path , int k) {
		System.out.println("k="+k);
		long t1 =System.currentTimeMillis();
		data=initData(dataIn(path));
		initCenterCluster(k);
		int j = 0;
		do {
			j++;
			setGroup();			
		} while(center.length>setNewCenter());
		System.out.println(j+" regressions have been finished, cluseters锛�");
		for (int i = 0; i < center.length; i++) {				
			System.out.println("("+center[i].r+","+center[i].g+","+center[i].b+")");
		}
		int idx = path.lastIndexOf("/") +1;
		
		dataOut(path.substring(0, idx)+k+"-"+path.substring(idx));
		long t2 =System.currentTimeMillis();
		System.out.println("Time consumed="+(t2-t1));
	}
	
	public static void initCenterCluster(int k) {
		center = new DataItem[k];
		centerTotal = new DataItem[k];
		
		int width;
		int height;
		System.out.println("Random k centers");
		for (int i = 0; i < k; i++) {
			DataItem cen = new DataItem();
			DataItem cen1 = new DataItem();
			width = (int)(Math.random()*data.length);
			height = (int)(Math.random()*data[0].length);
			cen.group=i;
			cen.r=data[width][height].r;
			cen.g=data[width][height].g;
			cen.b=data[width][height].b;
			center[i]=cen;
			cen1.r=0;
			cen1.g=0;
			cen1.b=0;
			cen1.group=0;
			centerTotal[i]=cen1;
			//width=0;
			//height=0;
			System.out.println("("+center[i].r+","+center[i].g+","+center[i].b+")");
		}
		System.out.println("initCenterCluster done");
	}
	
	public static DataItem [][] initData(int[][] data) {
		DataItem [][] dataitems = new DataItem[data.length][data[0].length];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				DataItem di = new DataItem();
				Color c = new Color(data[i][j]);
				di.r=c.getRed();
				di.g=c.getGreen();
				di.b=c.getBlue();
				di.group=-1;
				dataitems[i][j]=di;
			}
		}
		System.out.println("initData done");
		return dataitems;
	}
	
	public static class Reader implements Runnable {
		private int[][] data;
		private int i;
		private BufferedImage bi;
		private int height;
		
		public Reader(int[][] data, int i, BufferedImage bi, int height) {
			this.data = data;
			this.i=i;
			this.bi=bi;
			this.height=height;
		}
		
		@Override
		public void run() {
			for (int j = 0; j < height; j++)
			{
				data[i][j] = bi.getRGB(i, j);
			}
		}		
	}
	
	public static void setGroup() {		
		int threadNum =  2*Runtime.getRuntime().availableProcessors()/3;
		ExecutorService executor = Executors.newFixedThreadPool(threadNum);
		for (int p = 0; p < data.length; p++) {
			executor.execute(new GroupSetter(data, center, centerTotal, p));
		}
		executor.shutdown();
		while (!executor.isTerminated());
	}

	public static class GroupSetter implements Runnable {
		private DataItem[][] data;
		private DataItem[] center;
		private DataItem[] centerTotal;
		private int p;
		
		public GroupSetter(DataItem[][] data, DataItem[] center, DataItem[] centerTotal, int p) {
			this.data = data;
			this.center=center;
			this.centerTotal = centerTotal;
			this.p=p;
		}
		
		@Override
		public void run() {
			DataItem[][] data2 = data;
			DataItem[] center2 = center;
			DataItem[] centerTotal2 = centerTotal;
		    for (int q = 0; q < data2[0].length; q++) {
				int group = -1;
			    int dis = Integer.MAX_VALUE;
				int pp = p;
				DataItem[] center3 = center2;
				DataItem[] centerTotal3 = centerTotal2;
			    for (int i = 0; i < center3.length; i++) {
				    int distance = getDistance(center3[i], data2[pp][q]);
				    if (distance <= dis) {
				        dis = distance;
				        group = i;
				        if (dis==0) {
				        	//i = center3.length;
				        	break;
				        }
				    }
			    }
			    data2[pp][q].group = group;
			    centerTotal3[group].r += data2[pp][q].r;
			    centerTotal3[group].g += data2[pp][q].g;
			    centerTotal3[group].b += data2[pp][q].b;
			    centerTotal3[group].group += 1;
		    }
		}	
	
		public int  getDistance(DataItem k,DataItem d) {
			int rdiff = k.r-d.r;
			int gdiff = k.g-d.g;
			int bdiff = k.b-d.b;
			return rdiff*rdiff+gdiff*gdiff+bdiff*bdiff;
		}
	}
	
	public static int setNewCenter() {
		int sum = 0;
		ExecutorService executor = Executors.newFixedThreadPool(2);
		for (int i = 0; i < centerTotal.length; i++) {
			Future<Integer> future = executor.submit(new NewCenterBuilder(center, centerTotal, i));
			try {
			    sum += future.get();
			} catch (InterruptedException e) {
				
			} catch (ExecutionException e) {
				
			}
		}
		executor.shutdown();
		while (!executor.isTerminated());
		return sum;
	}
	
	public static class NewCenterBuilder implements Callable {
		private DataItem[] center;
		private DataItem[] centerTotal;
		private int i;

		public NewCenterBuilder(DataItem[] center, DataItem[] centerTotal, int i) {
			this.center=center;
			this.centerTotal = centerTotal;
			this.i=i;
		}
		
		@Override
		public Integer call() {
			double rr = centerTotal[i].r/centerTotal[i].group;
			double gg = centerTotal[i].g/centerTotal[i].group;
			double bb = centerTotal[i].b/centerTotal[i].group;
			
            double threshold  = 0.01;
            
            double rdiff = center[i].r-rr;
            double gdiff = center[i].g-gg;
            double bdiff = center[i].b-bb;
            
            // abs(x) = squre-root(x**2)
            double rRoot = Double.longBitsToDouble(((Double.doubleToLongBits(rdiff * rdiff)-(1l<<52))>>1 ) + (1l<<61));
            double gRoot = Double.longBitsToDouble(((Double.doubleToLongBits(gdiff * gdiff)-(1l<<52))>>1 ) + (1l<<61));
            double bRoot = Double.longBitsToDouble(((Double.doubleToLongBits(bdiff * bdiff)-(1l<<52))>>1 ) + (1l<<61));
           
			if (rRoot/center[i].r > threshold || gRoot/center[i].g > threshold || bRoot/center[i].b > threshold) {
				center[i].r=(int)rr;
				center[i].g=(int)gg;
				center[i].b=(int)bb;
				return 0;
			} 
			return 1;
		}		
	}
	
	public static int [][]  dataIn(String path) {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int width = bi.getWidth();
		int height = bi.getHeight();
		System.out.println("width="+ width + "   height=" + height);
		int [][] data = new int[width][height];
		
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for (int i = 0; i < width; i++) {
			executor.execute(new Reader(data, i, bi, height));
		}
		executor.shutdown();
		while (!executor.isTerminated());
		System.out.println("Loading done");
		return data;
	}
	
	public static void  dataOut(String path) {
		BufferedImage nbi = new BufferedImage(data.length, data[0].length, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				Color c = new Color(center[data[i][j].group].r, center[data[i][j].group].g,center[data[i][j].group].b);
				nbi.setRGB(i, j, c.getRGB());
			}
		}
		try {
			ImageIO.write(nbi, "jpg", new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("dataOut done");
	}
}
