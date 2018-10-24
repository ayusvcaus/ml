package com.ayu.ml.rf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.ayu.ml.rf.CellData;
import com.ayu.ml.rf.Entries;
import com.ayu.ml.rf.Entry;

public class DecisionTree {

    public DecisionTree(List<Boolean> typeSpecification, List<Boolean> chosenAttributes, boolean inRandomForest) {
        trainData = new Entries();
        this.typeSpecification = typeSpecification;
        this.chosenAttributes = chosenAttributes;
        this.inRandomForest = inRandomForest;
    }

    // A boolean array indicates the attributes that user choose to use/ignore.
    private List<Boolean> chosenAttributes;

    // Store attributes' name if the data has a header.
    private List<String> attributesName;

    // For RandomForest use indicator.
    private boolean inRandomForest;

    // Attributes' type(categorical/continuous) specification.
    private List<Boolean> typeSpecification;

    // Training Data.
    public Entries trainData;

    // Decision Tree's root node.
    public Node root;

    // Indicates the Random subspace in Random Forest.
    public int attrSubspaceNum;

    private Node buildID3Tree(Entries examples, List<Integer> attributes){
        Node node = new Node(examples, attributes, typeSpecification, chosenAttributes, inRandomForest, attrSubspaceNum);

        // If current node is already consistent with examples, return.
        if (node.isConsistent) {
            return node;

        } else {
            // If there's no longer attributes, no need to continue.
            if (attributes.size() == 0) {

                // There's no attributes to continue splitting though current node is not consistent, then
                // take a majority vote for this leaf node's label.
                node.label = Collections.max(node.labelsCount.entrySet(), Map.Entry.comparingByValue()).getKey();
                return node;

            }

            Entries newExamplesLeft = new Entries();
            Entries newExampleRight = new Entries();

            if (!typeSpecification.get(node.bestAttribute)) {
                // Sort examples according to best splitting attribute, for splitting dataset later.
                Collections.sort(examples.entries, new Comparator<Entry>() {
                    @Override
                    public int compare(Entry o1, Entry o2) {
                        return new CellData().compare(o1.attributes.get(node.bestAttribute),
                                o2.attributes.get(node.bestAttribute));
                    }
                });

                // Split dataset according to decision of the best splitting attribute.
                for (int i = 0; i < examples.entries.size(); i++) {
                    Entry entryTemp = examples.entries.get(i);
                    if ((Double) entryTemp.attributes.get(node.bestAttribute).value <= ((Double) node.decision.value)) {
                        newExamplesLeft.entries.add(entryTemp);
                    } else {
                        newExampleRight.entries.add(entryTemp);
                    }
                }
            } else {
                // Split dataset according to decision of the best splitting attribute.
                for (int i = 0; i < examples.entries.size(); i++) {
                    Entry entryTemp = examples.entries.get(i);
                    if ((entryTemp.attributes.get(node.bestAttribute).value).equals(node.decision.value)) {
                        newExamplesLeft.entries.add(entryTemp);
                    } else {
                        newExampleRight.entries.add(entryTemp);
                    }
                }
            }

            // Generating remaining attributes.
            List<Integer> newAttributes = new ArrayList<>(attributes);
            newAttributes.remove(Integer.valueOf(node.bestAttribute));

            // If the dataset after splitting is not empty, then branching and grow the tree. Else end growing.
            if (newExamplesLeft.entries.size() != 0) {
                node.left = buildID3Tree(newExamplesLeft, newAttributes);
            } else {
                node.left = new Node();
            }
            if (newExampleRight.entries.size() != 0) {
                node.right = buildID3Tree(newExampleRight, newAttributes);
            } else {
                node.right = new Node();
            }

            return node;
        }
    }

    /**
     * Using current test entry to traverse the built tree and return predicted label.
     * @param entry Current test entry.
     * @param node  The decision tree's root node.
     * @return  The current test entry's predicted label.
     */
    private String getPrediction(Entry entry, Node node) {
        if (node.left == null && node.right == null)
            return node.label;
        if (!typeSpecification.get(node.bestAttribute)) {
            if (((Double) entry.attributes.get(node.bestAttribute).value) <= ((Double) node.decision.value)) {
                return getPrediction(entry, node.left);
            } else {
                return getPrediction(entry, node.right);
            }
        } else {
            if ((entry.attributes.get(node.bestAttribute).value).equals(node.decision.value)) {
                return getPrediction(entry, node.left);
            } else {
                return getPrediction(entry, node.right);
            }
        }
    }


    /**
     * Funtion to start building the tree.
     */
    public void trainTree() {
        List<Integer> attributes = new ArrayList<>();

        // The attributes index array. To indicate the remaining unsplit attributes.
        // Initially all attributes are remained.
        for (int i = 0; i < trainData.entries.get(0).attributes.size(); i ++) {
            if (chosenAttributes.get(i))
                attributes.add(i);
        }

        //start = new Node();
        root = buildID3Tree(trainData, attributes);
    }

    /**
     * For random forest testing purpose.
     * @param e Entry that random forest wants to get result on.
     * @return The predicted label of the input entry.
     */
    public String test(Entry e) {
        String predictedLabel = getPrediction(e, root);
        return predictedLabel;
    }
    
    public void setTrainData(Entries trainData) {
    	this.trainData = trainData;
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
