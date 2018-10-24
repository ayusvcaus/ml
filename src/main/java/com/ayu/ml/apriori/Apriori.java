package com.ayu.ml.apriori;


import java.util.ArrayList;  
import java.util.Collections;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
import java.util.Set; 
import java.util.HashSet; 
  
public class Apriori {  
  
    private final static int SUPPORT = 2; // ֧�ֶ���ֵ  
    private final static double CONFIDENCE = 0.7; // ���Ŷ���ֵ  
  
    private final static String ITEM_SPLIT = ","; // ��֮��ķָ���  
    private final static String CON = "-->"; // ��֮��ķָ���  
    
    private final static List<String> transList = new ArrayList() {{
        add("1,2,5,");  
        add("2,4,");  
        add("2,3,");  
        add("1,2,4,");  
        add("1,3,");  
        add("2,3,");  
        add("1,3,");  
        add("1,2,3,5,");  
        add("1,2,3,");  
    }};
     
    public Map<String, Integer> getFC() {  
        Map<String, Integer> frequentCollectionMap = new HashMap<String, Integer>(getItem1FC());// ���е�Ƶ����  
  
        //frequentCollectionMap.putAll(getItem1FC());  
  
        Map<String, Integer> itemkFcMap = new HashMap<>(getItem1FC());  
        //itemkFcMap.putAll(getItem1FC());  
        while (!itemkFcMap.isEmpty()) {  
            Map<String, Integer> candidateCollection = getCandidateCollection(itemkFcMap);  
            Set<String> ccKeySet = candidateCollection.keySet();  
            // �Ժ�ѡ��������ۼӼ���  
            for (String trans : transList) {  
                for (String candidate : ccKeySet) {  
                    boolean flag = true;// �����жϽ������Ƿ���ָú�ѡ�������֣�������1  
                    String[] candidateItems = candidate.split(ITEM_SPLIT);  
                    for (String candidateItem : candidateItems) {  
                        if (!trans.contains(candidateItem + ITEM_SPLIT)) {  
                            flag = false;  
                            break;  
                        }  
                    }  
                    if (flag) {  
                        candidateCollection.put(candidate, candidateCollection.get(candidate)+1);  
                    }  
                }  
            }  
  
            // �Ӻ�ѡ�����ҵ�����֧�ֶȵ�Ƶ������  
            itemkFcMap.clear();  
            for (String candidate : ccKeySet) {  
                Integer count = candidateCollection.get(candidate);  
                if (count >= SUPPORT) {  
                    itemkFcMap.put(candidate, count);  
                }  
            }  
            // �ϲ�����Ƶ����  
            frequentCollectionMap.putAll(itemkFcMap);  
        }  
        return frequentCollectionMap;  
    }  
  
    private Map<String, Integer> getCandidateCollection(Map<String, Integer> itemkFcMap) {  
        Map<String, Integer> candidateCollection = new HashMap<String, Integer>();  
        Set<String> itemkSet1 = itemkFcMap.keySet();  
        Set<String> itemkSet2 = itemkFcMap.keySet();  
  
        for (String itemk1 : itemkSet1) {  
            for (String itemk2 : itemkSet2) {  
                // ��������  
                String[] tmp1 = itemk1.split(ITEM_SPLIT);  
                String[] tmp2 = itemk2.split(ITEM_SPLIT);  
  
                String c = "";  
                if (tmp1.length == 1) {//itemkFcMap��ŵ��Ǻ�ѡ1�����ʱ  
                    if (tmp1[0].compareTo(tmp2[0]) < 0) {  
                        c = tmp1[0] + ITEM_SPLIT + tmp2[0] + ITEM_SPLIT;  
                    }  
                } else {  
                    boolean flag = true;//�Ƿ���Խ�������  
                    for (int i=0; i<tmp1.length-1; i++) {  
                        if (!tmp1[i].equals(tmp2[i])) {  
                            flag = false;  
                            break;  
                        }  
                    }  
                    if (flag && (tmp1[tmp1.length - 1].compareTo(tmp2[tmp2.length - 1]) < 0)) {  
                        c = itemk1 + tmp2[tmp2.length - 1] + ITEM_SPLIT;  
                    }  
                }  
                // ���м�֦  
                boolean hasInfrequentSubSet = false;// �Ƿ��з�Ƶ�������Ĭ����  
                if (!c.equals("")) {  
                    String[] tmpC = c.split(ITEM_SPLIT);  
                    //���Ե�������ignoreIndex  
                    for (int ignoreIndex=0; ignoreIndex<tmpC.length; ignoreIndex++) {  
                        String subC = "";  
                        for (int j=0; j<tmpC.length; j++) {  
                            if (ignoreIndex != j) {  
                                subC +=  tmpC[j] + ITEM_SPLIT;  
                            }  
                        }  
                        if (itemkFcMap.get(subC)==null) {  
                            hasInfrequentSubSet = true;  
                            break;  
                        }  
                    }  
                } else {  
                    hasInfrequentSubSet = true;  
                }  
  
                if (!hasInfrequentSubSet) {  
                    //�����������ĺ�ѡ���ӵ�candidateCollection ������  
                    candidateCollection.put(c, 0);  
                }  
            }  
        }  
        return candidateCollection;  
    }  
  
    //�õ�Ƶ��1�  
    private Map<String, Integer> getItem1FC() {  
        Map<String, Integer> sItem1FcMap = new HashMap<>();  
        Map<String, Integer> rItem1FcMap = new HashMap<>();// Ƶ��1�  
  
        for (String trans : transList) {  
            String[] items = trans.split(ITEM_SPLIT);  
            for (String item : items) {  
                try {
                	sItem1FcMap.put(item + ITEM_SPLIT, sItem1FcMap.get(item + ITEM_SPLIT) + 1);
                } catch (Exception e) {
                	sItem1FcMap.put(item + ITEM_SPLIT, 1);  
                }
            }  
        }  
  
        Set<String> keySet = sItem1FcMap.keySet();  
        for (String key : keySet) {  
            Integer count = sItem1FcMap.get(key);  
            if (count >= SUPPORT) {  
                rItem1FcMap.put(key, count);  
            }  
        }  
        return rItem1FcMap;  
    }  
  
    //����Ƶ������ϵõ���������  
    public Map<String, Double> getRelationRules(Map<String, Integer> frequentCollectionMap) {  
        Map<String, Double> relationRules = new HashMap<String, Double>();  
        Set<String> keySet = frequentCollectionMap.keySet();  
        for (String key : keySet) {  
            double countAll = frequentCollectionMap.get(key);  
            String[] keyItems = key.split(ITEM_SPLIT);  
            if (keyItems.length>1) {  
                List<String> source = new ArrayList<>();  
                Collections.addAll(source, keyItems);  
                List<Set<String>> result = buildSubSet(source);
                  
                for (Set<String> itemList : result) {  
                    if (itemList.size()<source.size()) {// ֻ�������Ӽ�  
                        Set<String> otherList = new HashSet<>();//��¼һ���Ӽ��Ĳ�  
                        for (String sourceItem : source) {  
                            if (!itemList.contains(sourceItem)) {  
                                otherList.add(sourceItem);  
                            }  
                        }  
                        String reasonStr = "";// �����ǰ��  
                        String resultStr = "";// ����Ľ��  
                        for (String item : itemList) {  
                            reasonStr += item + ITEM_SPLIT;  
                        }  
                        for (String item : otherList) {  
                            resultStr = resultStr + item + ITEM_SPLIT;  
                        }  
                        double countReason = frequentCollectionMap.get(reasonStr);  
                        double itemConfidence = countAll / countReason;// �������Ŷ�  
                        if (itemConfidence >= CONFIDENCE) {  
                            String rule = reasonStr + CON + resultStr;  
                            relationRules.put(rule, itemConfidence);  
                        }  
                    }  
                }  
            }  
        }  
        return relationRules;  
    }  
  
    private List<Set<String>> buildSubSet(List<String> sourceSet) { 
    	List<Set<String>> result = new ArrayList<>();
        int n = sourceSet.size();  
        //n��Ԫ����2^n-1���ǿ��Ӽ�  
        int num = (int) Math.pow(2, n);  
        for (int i=1; i<num; i++) {  
            String binary = Integer.toBinaryString(i);  
            int size = binary.length();  
            for (int k=0; k<n-size; k++) {//�������Ʊ�ʾ�ַ����Ҷ��룬��߲�0  
                binary = "0"+binary;  
            }  
            Set<String> set = new HashSet<>();  
            for (int index=0; index<n; index++) {  
                if (binary.charAt(index)=='1'){  
                    set.add(sourceSet.get(index));  
                }  
            }  
            result.add(set);  
        }
        return result;
    }  
  
    public static void main(String[] args) {  
        Apriori apriori = new Apriori();  
        Map<String, Integer> frequentCollectionMap = apriori.getFC();  
        System.out.println("----------------Ƶ����" + "----------------");  
        Set<String> fcKeySet = frequentCollectionMap.keySet();  
        for (String fcKey : fcKeySet) {  
            System.out.println(fcKey + "  :  "  + frequentCollectionMap.get(fcKey));  
        }  
        Map<String, Double> relationRulesMap = apriori.getRelationRules(frequentCollectionMap);  
        System.out.println("----------------��������" + "----------------");  
        Set<String> rrKeySet = relationRulesMap.keySet();  
        for (String rrKey : rrKeySet) {  
            System.out.println(rrKey + "  :  " + relationRulesMap.get(rrKey));  
        }  
    }  
}