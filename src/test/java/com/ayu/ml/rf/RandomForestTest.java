package com.ayu.ml.rf;

import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ayu.ml.rf.Utils;
import com.ayu.ml.rf.RandomForest;
import com.ayu.ml.rf.DecisionTree;

public class RandomForestTest {
	
	private String filePath = "data/rf/smallerData.csv";

	private List<Boolean> typeSpecification;
	private List<Boolean> chosenAttributes;
	private List<String[]> entries;
	
	private List<String[]> trainEntries;
	private List<String[]> testEntries;
	
	private DecisionTree dtree;
	
	
	
	@Before
	public void setup() {
        typeSpecification = Arrays.asList(true, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
        chosenAttributes = Arrays.asList(false, true, false, true, true, true, true, true, true, true, true,true, true, true, true, true, true, true);
    	trainEntries = new ArrayList<>();
    	testEntries = new ArrayList<>();
	}
 
    /**
     * Test of learn method, of class RandomForest.
     */
    @Test
    public void testRandonForest() throws Exception {
    	
    	RandomForest forest = new RandomForest(typeSpecification, chosenAttributes);
    	forest.initialize(8);
        Utils.saperatData(entries, filePath, forest, trainEntries, testEntries, 0.9);
        forest.setTrainData(Utils.loadData(trainEntries, typeSpecification));
        forest.trainForest();
       
        Entries testData =Utils.loadData(testEntries, typeSpecification);
        
        double correct = 0;
        for (Entry e: testData.entries) {
            Map<String, Integer> map = new HashMap<>();
            for (DecisionTree dt : forest.getRandomForest()) {           	
                Utils.count(map, dt.test(e));
            }

            String finalLabel = Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getKey();

            if (e.label.equals(finalLabel)) {
               correct++;
            } 
        }
        System.out.println("Forest Accuracy: " +  Double.toString(100 * correct / testData.entries.size()).substring(0, 5) + "%");
    }

    @Test
    public void testDecisionTree() throws Exception {
        dtree = new DecisionTree(typeSpecification, chosenAttributes, true);

        Utils.saperatData(entries, filePath, dtree, trainEntries, testEntries, 0.9);

        dtree.setTrainData(Utils.loadData(trainEntries, typeSpecification));
        dtree.trainTree();
        
        Entries testData = Utils.loadData(testEntries, typeSpecification);
        
        double correct = 0;
        for (Entry e: testData.entries) {     	
            if (e.label.equals(dtree.test(e))) {
                correct++;
             }
        }
        System.out.println("Tree Accuracy: " +  Double.toString(100*correct / testData.entries.size()).substring(0, 5) + "%");
    }
}