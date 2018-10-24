package com.ayu.ml.knn;

import org.junit.Test;
import com.ayu.ml.knn.KNN;

public class KNNTest {
	
	
	public void setup() {
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "4");
	}
	
	@Test
	public void testDatingClassify() throws Exception {

		long t1 = System.currentTimeMillis();
		KNN.datingClassify();
		long t2 = System.currentTimeMillis();

		System.out.println("Dating t2-t1="+(t2-t1));
	}
	
	@Test
	public void testHandwritingClassify() throws Exception {

		long t2 = System.currentTimeMillis();
		KNN.handwritingClassify();
		long t3 = System.currentTimeMillis();

		System.out.println("Handwriting t3-t2="+(t3-t2));
	}


}
