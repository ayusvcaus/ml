package com.ayu.ml.fp;

import java.util.List;

import org.junit.Test;

import com.ayu.ml.fp.FPTree;

public class FPTreeTest {
	
	@Test
	public void testFPGTree() throws Exception {
		
        FPTree fptree = new FPTree(4);  
        List<List<String>> transactions = fptree.loadTransaction("data/apriori/amazon.txt");  
        fptree.FPGrowth(transactions, null);  		
	}
}
