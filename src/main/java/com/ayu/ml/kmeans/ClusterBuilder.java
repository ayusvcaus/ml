package com.ayu.ml.kmeans;

import  com.ayu.ml.kmeans.Datum;

public class ClusterBuilder implements Runnable {
	private Datum[][] data;
	private Datum[] center;
	private int p;
	
	public ClusterBuilder(Datum[][] data, Datum[] center, int p) {
		this.data = data;
		this.center=center;
		this.p=p;
	}
	
	@Override
	public void run() {
	    for (int q=0; q<data[0].length; q++) {
			int cluster = -1;
		    int dis = Integer.MAX_VALUE;
		    for (int i=0; i <center.length; i++) {
			    int distance = getDistance(center[i], data[p][q]);
			    if (distance <= dis) {
			        dis = distance;
			        cluster = i;
			        if (dis==0) {
			        	break;
			        }
			    }
		    }
		    if (cluster>=0) {
		        data[p][q].cluster = cluster;
		        center[cluster].totalRed += data[p][q].red;
		        center[cluster].totalGreen += data[p][q].green;
		        center[cluster].totalBlue += data[p][q].blue;
		        center[cluster].totalCluster++;
		    }
	    }
	}	
	
	public int  getDistance(Datum c, Datum d) {
		int rdiff = c.red - d.red;
		int gdiff = c.green - d.green;
		int bdiff = c.blue - d.blue;
		return rdiff * rdiff + gdiff * gdiff + bdiff * bdiff;
	}
}