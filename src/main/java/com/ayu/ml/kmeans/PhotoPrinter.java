package com.ayu.ml.kmeans;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.ayu.ml.kmeans.Datum;

public class PhotoPrinter implements Runnable {
	BufferedImage nbi;
	private Datum[][] data;
	private Datum[] center;
	private int i;

	public PhotoPrinter(BufferedImage nbi, Datum[][] data, Datum[] center, int i) {
		this.nbi = nbi;
		this.data = data;
		this.center = center;
		this.i = i;
	}
	
	@Override
	public void run() {
		for (int j=0; j<data[0].length; j++) {
			int red = center[data[i][j].cluster].red > 255 ? 255: center[data[i][j].cluster].red;
			int green = center[data[i][j].cluster].green > 255 ? 255: center[data[i][j].cluster].green;
			int blue = center[data[i][j].cluster].blue > 255 ? 255: center[data[i][j].cluster].blue;
			Color c = new Color(red, green, blue);
			nbi.setRGB(i, j, c.getRGB());
		}
	}		
}