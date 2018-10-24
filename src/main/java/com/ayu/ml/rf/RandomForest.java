package com.ayu.ml.rf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.ayu.ml.rf.DecisionTree;

public class RandomForest {
    // Attributes' type(categorical/continuous) specification.
    private List<Boolean> typeSpecification;
    // Store attributes' name if the data has a header.
    
    private List<String> attributesName;

    // A data structure to store all the decision trees.
    private List<DecisionTree> randomForest;

    // Training Data.
    private Entries trainData;

    // The useful choosen attributes.
    private List<Boolean> chosenAttributes;

    // The random factor for training subset selection.
    public double trainSubsetFraction;

    // Indicates the Random subspace in Random Forest.
    private int attrSubspaceNum;

    /**
     * A utility that facilitates the counting process in a hash map for certain key.
     * Same as in python Collections.Counter().
     * @param hashMap The hash map that needs to be updated.
     * @param key The key that needs to be updated.
     * @return The updated hash map.
     */
    private void count(Map<String, Integer> map, String key) {
        try {
        	map.put(key, map.get(key) + 1);
        } catch (Exception e) {
        	map.put(key, 1);
        }
    }


    /**
     * Constructor for random forest.
     * @param typeSpecification Attributes' type(categorical/continuous) specification.
     * @param chosenAttributes A boolean array indicates the attributes that user choose to use/ignore.
     * @param delimiter Data CSV file delimiter.
     */
    public RandomForest(List<Boolean> typeSpecification, List<Boolean> chosenAttributes) {
        this.typeSpecification = typeSpecification;
        this.chosenAttributes = chosenAttributes;

        // The random factor for training subset selection is usually 2/3 of the rows.
        trainSubsetFraction = 2.0 / 3.0;
    }


    /**
     * Initialize all the trees.
     * @param treesNumber Trees to grow.
     */
    public void initialize(int treesNumber) {
    	randomForest = new ArrayList<>(treesNumber);
        for (int i = 0; i < treesNumber; i ++) {
            randomForest.add(new DecisionTree(typeSpecification, chosenAttributes, true));
        }
    }

    /**
     * Funtion to start growing trees in forest.
     */
    public void trainForest() {
        int trainSubsetSize = (int) (trainData.entries.size()*trainSubsetFraction);

        for (DecisionTree dt: randomForest) {
            Set<Integer> trainIndexes = new HashSet<>();
            for (int i=0; i<trainSubsetSize; i ++) {
                Integer index = (int) (Math.random()*(trainData.entries.size()));
                while (trainIndexes.contains(index)) {
                    index = (int) (Math.random()*(trainData.entries.size()));
                }
                trainIndexes.add(index);
                dt.trainData.entries.add(trainData.entries.get(index));
            }
            dt.attrSubspaceNum = attrSubspaceNum;
            dt.setAttributesName(attributesName);
            dt.trainTree();
        }
    }
    
    public void setTrainData(Entries trainData) {
    	this.trainData = trainData;
    }
    
    public List<DecisionTree> getRandomForest() {
    	return randomForest;
    }
    
    public void setAttributesName(List<String> attributesName) {
    	this.attributesName = attributesName;
    }
    
    public List<String> getAttributesName() {
    	return attributesName;
    }
    
    
    public void setAttrSubspaceNum(int attrSubspaceNum) {
    	this.attrSubspaceNum = attrSubspaceNum;
    }
}
