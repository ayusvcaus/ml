package com.ayu.ml.rf;

import java.util.*;

public class Node {
    // For RandomForest use indicator.
    boolean inRandomForest;

    // Indicates the Random subspace in Random Forest.
    int attrSubspaceNum;

    // The useful choosen attributes.
    List<Boolean> chosenAttributes;

    // Attributes' type(categorical/continuous) specification.
    List<Boolean> typeSpecification;

    // Entropy calculated on labelsCount which is formed by splitting boundary.
    double entropy;

    // Using the splitting boundary form a labelsCount.
    // It's a hash map, the key is label, the value is the number of it.
    Map<String, Integer> labelsCount;

    // Leaf node's label, which is used to produce prediction. NULL if non-leaf nodes.
    String label;

    // To tag if current node needs more splitting.
    boolean isConsistent;

    // The best attribute that needs to be split.
    int bestAttribute;

    // The decision boundary for the best attribute. Also using this to binarize the data.
    CellData decision;

    // Left child.
    Node left;

    // Right child.
    Node right;

    /**
     * Utility of LOG function, support any base.
     * @param x Log parameter.
     * @param base Base, should be integer.
     * @return The log result.
     */
    private static double log(double x, int base) {
        //return (Math.log(x) / Math.log(base));
        
        return Math.log(x);
    }

    /**
     * A utility that facilitates the counting process in a hash map for certain key.
     * Same as in python Collections.count().
     * @param hashMap The hash map that needs to be updated.
     * @param key The key that needs to be updated.
     * @return The updated hash map.
     */
    private void count(Map<String, Integer> map, String key) {    
        try {
        	map.put(key, map.get(key) + 1);;
        } catch (Exception e) {
        	map.put(key, 1);
        }
    }
    
    /**
     * For current examples, generate the label count, for later calculating entropy and consistency check.
     * @param examples The examples that current node received.
     */
    private void processLabels(Entries examples) {
        List<String> labels = new ArrayList<>();
        labelsCount = new HashMap<>();
        for (Entry e: examples.entries) {
            //labelsCount = count(labelsCount, e.label);
            count(labelsCount, e.label);
            labels.add(e.label);
        }
        if (labelsCount.size() == 1) {

            // If only one label exists in current example then set the prediction label to it.
            label = labels.get(0);

            // No need to split more, current node is consistent with examples.
            isConsistent = true;

        } else {
            // Received more than 1 labels, meaning not consistent, set the tag to false.
            isConsistent = false;
        }
    }

    /**
     * Calculate entropy for a labelsCount.
     * @param n The total number of labels.
     * @param labelsCount The hash map of labels, the key is label, the value is the number of it.
     * @return The labelsCount's entropy.
     */
    private double calculateEntropy(Integer n, Map<String, Integer> labelsCount) {
        double entropy = 0;
        Iterator it = labelsCount.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Double count = ((Integer) pair.getValue()) * 1.0; // * 1.0 is to convert it to double type.
            double p = count / n;
            entropy -= (p * log(p, 2));
        }
        return entropy;
    }

    /**
     * The main function to find the next best splitting attribute.
     * I put it into Node because every node would receive a set of examples when it's created and wouldn't be
     * changed later.
     * @param examples The examples that current node received after its parent's splitting.
     * @param attributes The remaining attributes that haven't been spitted before.
     */
    private void findBestSplitAttr(Entries examples, List<Integer> attributes) {

        // For later random forest attribute subspace selection.
        List<Integer> selectedAttributes = new ArrayList<>();

        // Random attributes subspace selection.
        if (inRandomForest) {
            if (attributes.size() > attrSubspaceNum) {

                Set<Integer> attrIndexes = new HashSet<>();

                for (int i = 0; i < attrSubspaceNum; i ++) {
                    Integer index = (int) (Math.random() * (attributes.size()));

                    while (attrIndexes.contains(index) || !chosenAttributes.get(attributes.get(index))) {
                        index = (int) (Math.random() * (attributes.size()));
                    }

                    attrIndexes.add(index);

                    selectedAttributes.add(attributes.get(index));
                }
            } else { // If the attributes left are less than specified attributes subspace number, than no selection is needed further.
                selectedAttributes.addAll(attributes);
            }
        } else {
            selectedAttributes.addAll(attributes);
        }

        // minEntropy over all attributes and all candidate boundaries.
        double minEntropy = Double.MAX_VALUE;
        
        Map<String, Integer> pos = new HashMap<>();
        Map<String, Integer> neg = new HashMap<>();

        // Traverse all remaining attributes.
        for (Integer attrIdx: selectedAttributes) {
            if (!chosenAttributes.get(attrIdx)) {
                continue;
            }

            if (!typeSpecification.get(attrIdx)) {     // Continuous
                // Sort examples according to current attributes.
                Collections.sort(examples.entries, new Comparator<Entry>() {
                    @Override
                    public int compare(Entry o1, Entry o2) {
                        return CellData.compare(o1.attributes.get(attrIdx),o2.attributes.get(attrIdx));
                    }
                });

                // Trying all candidate boundaries.
                for (int i = 1; i < examples.entries.size(); i++) {

                    for (int j = 0; j < examples.entries.size(); j++) {
                        String newLabel = examples.entries.get(j).label;
                        if (j < i) {
                           count(pos, newLabel);
                        } else {
                            count(neg, newLabel);
                        }
                    }

                    // Calculate pos and neg entropy.
                    double posFraction = (i * 1.0) / examples.entries.size();
                    double posEntropy = posFraction * calculateEntropy(i, pos);
                    double negEntropy = (1 - posFraction) * calculateEntropy(examples.entries.size() - i, neg);
                    pos.clear();
                    neg.clear();
                    // Updating the minEntropy.
                    if ((posEntropy + negEntropy) < minEntropy) {
                        minEntropy = posEntropy + negEntropy;
                        decision = new CellData(CellData.getMean(examples.entries.get(i - 1).attributes.get(attrIdx), examples.entries.get(i).attributes.get(attrIdx)));
                        bestAttribute = attrIdx;
                    }
                }
            } else {        // Categorical
                Set<String> categories = new HashSet<>();

                // Get all categories.
                for (int i = 0; i < examples.entries.size(); i ++) {
                    String data = (String) examples.entries.get(i).attributes.get(attrIdx).value;
                    categories.add(data);
                }

                // Find the best category to split.
                for (String category: categories) {

                    for (int i = 0; i < examples.entries.size(); i ++) {
                        String newLabel = examples.entries.get(i).label;
                        String data = (String) examples.entries.get(i).attributes.get(attrIdx).value;
                        if (category.equals(data)) {
                           count(pos, newLabel);
                        } else {
                            count(pos, newLabel);
                        }
                    }

                    // Calculate pos and neg entropy.
                    double posFraction = (pos.size() * 1.0) / examples.entries.size();
                    double posEntropy = posFraction * calculateEntropy(pos.size(), pos);
                    double negEntropy = (1 - posFraction) * calculateEntropy(neg.size(), neg);
                    pos.clear();
                    neg.clear();
                    // Updating the minEntropy.
                    if ((posEntropy + negEntropy) < minEntropy) {
                        minEntropy = posEntropy + negEntropy;
                        decision = new CellData(category);
                        bestAttribute = attrIdx;
                    }
                }
            }
        }
    }

    /**
     * Constructor for Node when it needs to receive examples and remaining attributes.
     * @param examples The remaining examples after its parent's splitting.
     * @param attributes The remaining attributes after its parent's splitting.
     */
    public Node(Entries examples, List<Integer> attributes, List<Boolean> typeSpecification, List<Boolean> choosenAttributes, boolean inRandomForest, int attrSubspaceNum) {
        left = null;
        right = null;
        label = null;
        this.typeSpecification = typeSpecification;
        this.chosenAttributes = choosenAttributes;
        this.inRandomForest = inRandomForest;
        this. attrSubspaceNum = attrSubspaceNum;

        processLabels(examples);

        entropy = calculateEntropy(examples.entries.size(), labelsCount);

        findBestSplitAttr(examples, attributes);
    }

    /**
     * Constructor of Node when there's no examples left.
     */
    public Node() {
        typeSpecification = new ArrayList<>();
        chosenAttributes = new ArrayList<>();
        labelsCount = new HashMap<>();
        isConsistent = true;
    }
}
