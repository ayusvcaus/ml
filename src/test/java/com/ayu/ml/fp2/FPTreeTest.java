package com.ayu.ml.fp2;

import java.util.List;
import java.util.LinkedHashMap;


import org.junit.Test;

import com.ayu.ml.fp2.FPTree;

public class FPTreeTest {
	
	@Test
    public void testFPTree() throws Exception {  
        // TODO Auto-generated method stub  
        /*String s1="i1"; 
        int flag=s1.compareTo("I1"); 
        System.out.println(flag);*/  
        //¶ÁÈ¡Êý¾Ý  
		String file = "data/apriori/amazon.txt";		
        List<List<String>> records = FPTree.readData(file);  
        
        int support = 2;
        List<TreeNode> orderheader = FPTree.buildHeaderLink(records, support);  
        
        LinkedHashMap<String,Integer> orderedMap = FPTree.getOrderedMap(orderheader);
        
        FPTree.fpgrowth(records, null, support);  
    } 

}
