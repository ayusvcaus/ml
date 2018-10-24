package com.ayu.ml.apriori;

import java.io.IOException;
import org.junit.Test;

public class AprioriTest {
	
	@Test
	public void testAprori() throws IOException {

		AprioriAlgorithm apriori = AprioriAlgorithm.getInstance();

		String path = "data/apriori/";
		String[] files = {"amazon.txt"};//, "jcpenny.txt", "shoprite.txt", "staples.txt", "walmart.txt"};
		for (String file: files) {
		    apriori.generateRules(20, 40, path + file);
		    System.out.println("--------------------------");
		}
	}
}
