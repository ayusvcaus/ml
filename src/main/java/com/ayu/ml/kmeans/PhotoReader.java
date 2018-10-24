package com.ayu.ml.kmeans;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class PhotoReader implements Runnable {
	private Datum[][] data;
	private int i;
	private BufferedImage bi;
	private int height;
	
	public PhotoReader(Datum[][] data, int i, BufferedImage bi) {
		this.data = data;
		this.i = i;
		this.bi = bi;
		this.height = bi.getHeight();
	}
	
	@Override
	public void run() {
		for (int j=0; j<height; j++) {
			Datum di = new Datum();
			Color c = new Color(bi.getRGB(i, j));
			di.red = c.getRed();
			di.green = c.getGreen();
			di.blue = c.getBlue();
			di.cluster = -1;
			data[i][j] = di;
		}
	}	
}
