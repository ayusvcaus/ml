package com.ayu.ml.fp2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
//import java.util.LinkedList;  
import java.util.List;  
import java.util.Map; 
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import com.ayu.ml.fp2.TreeNode;
  
public class FPTree {  
   
    public static List<List<String>> readData(String file) throws Exception {  
    	
        List<List<String>> records = new ArrayList<>();  
        BufferedReader br = null;
        try {  
            br = new BufferedReader(new FileReader(new File(file)));  
            String line = "";  
            while((line = br.readLine()) != null) {  
            	String[] tmp = line.split("\\s+");
            	List<String> list = new ArrayList(Arrays.asList(tmp[1].split(",")));
                records.add(list);  
            }          
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {
        	if (br!=null) {
        		try {
        			br.close();
        		} catch (Exception e) {
        		}
        	}
        }       
        return records;
    }
    
    //������ͷ��  
    public static List<TreeNode> buildHeaderLink(List<List<String>> records, int support) {  
    	
    	if (records==null || records.size()==0) {
    		return null;
    	}
        List<TreeNode> header= new ArrayList<>();  
  
        Map<String, TreeNode> map = new HashMap<String, TreeNode>();  
        
        for (List<String> items : records) {  
              
            for (String item:items) {  
                //�������������1��������������  
                if(map.containsKey(item)){  
                    map.get(item).sum(1);  
                } else {  
                    TreeNode node = new TreeNode();  
                    node.setName(item);  
                    node.setCount(1);   
                    map.put(item, node);  
                }  
             }  
        }  
         // ��֧�ֶȴ��ڣ�����ڣ�minSup������뵽F1��  
        Set<String> names = map.keySet();  
        for (String name : names) {  
            TreeNode tnode = map.get(name);  
            if (tnode.getCount() >= support) {  
                header.add(tnode);  
            }  
        }  
        Collections.sort(header);
        return header;  
    }  
 
    //ѡ�����򣬽���,���ͬ����L �еĴ�������  
    //public static List<String> itemsort(List<String> list, List<TreeNode> header, LinkedHashMap<String,Integer> orderedMap){ 
    public static List<String> itemsort(List<String> list, List<TreeNode> header) {  
        //List<String> list=new ArrayList<String>();  
        //ѡ������  
    	/*System.out.println(list);
    	Set<String> set = new HashSet<>(list);
    	list = new ArrayList(set);
        int len=list.size();  
        for (int i=0; i<len; i++) {  
            for (int j=i+1; j<len; j++) {  
                String key1=list.get(i);  
                String key2=list.get(j);  
                Integer value1=findcountByName(key1,header);  
                if (value1==-1) {
                	continue;  
                }
                Integer value2=findcountByName(key2,header);  
                if (value2==-1) {
                	continue;  
                }
                if (value1<value2) {  
                    String tmp=key2;  
                    list.remove(j);  
                    list.add(j,key1);  
                    list.remove(i);  
                    list.add(i,tmp);  
                } else if (value1==value2) {  
                    int v1 = orderedMap.get(key1);  
                    int v2 = orderedMap.get(key2);  
                    if (v1>v2) {  
                        String tmp=key2;  
                        list.remove(j);  
                        list.add(j,key1);  
                        list.remove(i);  
                        list.add(i,tmp);  
                    }  
                }  
             }  
        }  
        
        //System.out.println(list);
    	return list;
    	*/
    	Set<String> set = new HashSet<>(list);
        List<String> itemsDesc = new ArrayList<>();  
        for (TreeNode node : header) {  
            if (set.contains(node.getName())) {  
                itemsDesc.add(node.getName());  
           }  
        }         
        return itemsDesc; 
    }  
    
    public static Integer findcountByName(String itemname, List<TreeNode> header){  
  
        for (TreeNode node:header) {  
            if (node.getName().equals(itemname)) {  
                return node.getCount();  
            }  
        }  
        return -1;  
    }  
      
    /** 
     *  
     * @param records �������ļ�¼,��I1,I2,I3 
     * @param header �����н��ܵı�ͷ 
     * @return ���ع����õ��� 
     */  
    //public static TreeNode builderFpTree(List<List<String>> records, List<TreeNode> header, LinkedHashMap<String,Integer> orderedMap) throws Exception {  
    public static TreeNode builderFpTree(List<List<String>> records, List<TreeNode> header) throws Exception { 
        if (records== null || records.isEmpty() || header==null || header.isEmpty()) {  
            return null;  
        }  
        TreeNode root = new TreeNode();  
        for (List<String> items : records) {  
        	//List<String> list = itemsort(items, header, orderedMap);
        	List<String> list = itemsort(items, header);
            //addNode(root, items, header); 
        	addNode(root, list, header);
        }   
        return root;  
    } 
    
    //���Ѿ��з�֦���ڵ�ʱ���ж������Ľڵ��Ƿ����ڸ÷�֦��ĳ���ڵ㣬��ȫ���غϣ��ݹ�  
    public static TreeNode addNode(TreeNode root, List<String> items, List<TreeNode> header) throws Exception {  
        if (items==null || items.isEmpty()) {
        	return null;  
        }
        String item = items.remove(0);
        //��ǰ�ڵ�ĺ��ӽڵ㲻�����ýڵ㣬��ô���ⴴ��һ֧��֧��  
        TreeNode node=root.findChild(item);  
        if (node==null) {  
            node=new TreeNode();  
            node.setName(item);  
            node.setCount(1);  
            node.setParent(root);  
            root.addChild(node);  
              
            //�ӽ������ڵ�ӵ���ͷ��   
            for (TreeNode head:header) {  
                if (head.getName().equals(item)) {  
                    while (head.getNextHomonym()!=null) {  
                        head=head.getNextHomonym();  
                    }  
                    head.setNextHomonym(node);  
                    break;  
                }  
            }  
            //�ӽ������ڵ�ӵ���ͷ��  
        } else{  
            node.setCount(node.getCount()+1);  
        }  
   
        addNode(node, items, header);  
        return root;  
    }  
    
    //��Ҷ���ҵ����ڵ㣬�ݹ�֮  
    public static void toRoot(TreeNode node,List<String> newrecord){  
        if (node.getParent()==null) {
        	return;  
        }
        String name = node.getName();  
        newrecord.add(name);  
        toRoot(node.getParent(),newrecord);  
    } 
    
    //������FP-tree��������ϣ������Ƶ���  
    /*public void combineItem(TreeNode node, List<String> newrecord, String Item){  
        if (node.getParent()==null) {
        	return;  
        }  
        newrecord.add(node.getName());  
        toroot(node.getParent(), newrecord);  
    } */ 
    
    //fp-growth   
    //public static void fpgrowth(List<List<String>> records, String item, int support, LinkedHashMap<String,Integer> orderedMap) throws Exception {  
    public static void fpgrowth(List<List<String>> records, String item, int support) throws Exception {  
        //�����µ�����ģʽ���ĸ�����¼�������¹���FP-tree  
        //List<List<String>> newrecords= new ArrayList<>();  
        //������ͷ  
        List<TreeNode> header = buildHeaderLink(records, support);  
        if (header.isEmpty()) {  
            System.out.println("-----------------");  
        }
        //����FP-Tree  
        //TreeNode fptree = builderFpTree(records, header, orderedMap);  
        TreeNode fptree = builderFpTree(records, header); 
        //�����ݹ������  
        if (fptree==null) {  
            System.out.println("-----------------");  
            return;  
        }  
        //��ӡ���,���Ƶ���  
        if (item!=null) {  
            //Ѱ������ģʽ��,����β��ʼ  
            for (int i=header.size()-1; i>=0; i--) {  
                TreeNode head=header.get(i);  
                //String itemname=head.getName();  
                Integer count=0;  
                while (head.getNextHomonym()!=null) {  
                    head=head.getNextHomonym();  
                    //Ҷ��count���ڶ��٣������������¼  
                    count=count+head.getCount();  
                      
                }  
                //��ӡƵ��� 
                if (!item.contains(head.getName())) {
                   System.out.println(head.getName()+","+item+"\t"+count);  
                }
            }  
        }
        
        List<List<String>> newrecords= new ArrayList<>();
        
        //Ѱ������ģʽ��,����β��ʼ  
        for (int i=header.size()-1; i>=0; i--){  
            TreeNode head=header.get(i);  
            String itemname = null;  
            //�����  
            if (item==null) {  
                itemname=head.getName();  
            } else {  
            	if (!item.contains(head.getName()))
                   itemname=head.getName()+","+item;  
            	else 
            		return;
       
            }  
              
            while (head.getNextHomonym()!=null){  
                head=head.getNextHomonym();  
                //Ҷ��count���ڶ��٣������������¼  
                Integer count=head.getCount();  
                for (int n=0; n<count; n++){  
                   List<String> record = new ArrayList<>();  
                   toRoot(head.getParent(),record);  
                   newrecords.add(record);  
                }  
            }  
            //System.out.println("-----------------");  
            //�ݹ�֮,������FP-Tree  
            //fpgrowth(newrecords,itemname, support, orderedMap);  
            fpgrowth(newrecords,itemname, support);
        }  
    } 
    
    //������򣬴˲�Ҳ����ʡ�ԣ�Ϊ�˼����ټӹ�������鷳����  
    public static LinkedHashMap<String,Integer> getOrderedMap(List<TreeNode> orderheader){ 
    	LinkedHashMap<String,Integer> orderedMap = new LinkedHashMap<>(); 
        for (int i=0; i<orderheader.size(); i++) {  
            TreeNode node=orderheader.get(i);  
            orderedMap.put(node.getName(), i);  
        }  
        return orderedMap;  
    }   
}  