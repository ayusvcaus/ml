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
    
    //创建表头链  
    public static List<TreeNode> buildHeaderLink(List<List<String>> records, int support) {  
    	
    	if (records==null || records.size()==0) {
    		return null;
    	}
        List<TreeNode> header= new ArrayList<>();  
  
        Map<String, TreeNode> map = new HashMap<String, TreeNode>();  
        
        for (List<String> items : records) {  
              
            for (String item:items) {  
                //如果存在数量增1，不存在则新增  
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
         // 把支持度大于（或等于）minSup的项加入到F1中  
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
 
    //选择法排序，降序,如果同名按L 中的次序排序  
    //public static List<String> itemsort(List<String> list, List<TreeNode> header, LinkedHashMap<String,Integer> orderedMap){ 
    public static List<String> itemsort(List<String> list, List<TreeNode> header) {  
        //List<String> list=new ArrayList<String>();  
        //选择法排序  
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
     * @param records 构建树的记录,如I1,I2,I3 
     * @param header 韩书中介绍的表头 
     * @return 返回构建好的树 
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
    
    //当已经有分枝存在的时候，判断新来的节点是否属于该分枝的某个节点，或全部重合，递归  
    public static TreeNode addNode(TreeNode root, List<String> items, List<TreeNode> header) throws Exception {  
        if (items==null || items.isEmpty()) {
        	return null;  
        }
        String item = items.remove(0);
        //当前节点的孩子节点不包含该节点，那么另外创建一支分支。  
        TreeNode node=root.findChild(item);  
        if (node==null) {  
            node=new TreeNode();  
            node.setName(item);  
            node.setCount(1);  
            node.setParent(root);  
            root.addChild(node);  
              
            //加将各个节点加到链头中   
            for (TreeNode head:header) {  
                if (head.getName().equals(item)) {  
                    while (head.getNextHomonym()!=null) {  
                        head=head.getNextHomonym();  
                    }  
                    head.setNextHomonym(node);  
                    break;  
                }  
            }  
            //加将各个节点加到链头中  
        } else{  
            node.setCount(node.getCount()+1);  
        }  
   
        addNode(node, items, header);  
        return root;  
    }  
    
    //从叶子找到根节点，递归之  
    public static void toRoot(TreeNode node,List<String> newrecord){  
        if (node.getParent()==null) {
        	return;  
        }
        String name = node.getName();  
        newrecord.add(name);  
        toRoot(node.getParent(),newrecord);  
    } 
    
    //对条件FP-tree树进行组合，以求出频繁项集  
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
        //保存新的条件模式基的各个记录，以重新构造FP-tree  
        //List<List<String>> newrecords= new ArrayList<>();  
        //构建链头  
        List<TreeNode> header = buildHeaderLink(records, support);  
        if (header.isEmpty()) {  
            System.out.println("-----------------");  
        }
        //创建FP-Tree  
        //TreeNode fptree = builderFpTree(records, header, orderedMap);  
        TreeNode fptree = builderFpTree(records, header); 
        //结束递归的条件  
        if (fptree==null) {  
            System.out.println("-----------------");  
            return;  
        }  
        //打印结果,输出频繁项集  
        if (item!=null) {  
            //寻找条件模式基,从链尾开始  
            for (int i=header.size()-1; i>=0; i--) {  
                TreeNode head=header.get(i);  
                //String itemname=head.getName();  
                Integer count=0;  
                while (head.getNextHomonym()!=null) {  
                    head=head.getNextHomonym();  
                    //叶子count等于多少，就算多少条记录  
                    count=count+head.getCount();  
                      
                }  
                //打印频繁项集 
                if (!item.contains(head.getName())) {
                   System.out.println(head.getName()+","+item+"\t"+count);  
                }
            }  
        }
        
        List<List<String>> newrecords= new ArrayList<>();
        
        //寻找条件模式基,从链尾开始  
        for (int i=header.size()-1; i>=0; i--){  
            TreeNode head=header.get(i);  
            String itemname = null;  
            //再组合  
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
                //叶子count等于多少，就算多少条记录  
                Integer count=head.getCount();  
                for (int n=0; n<count; n++){  
                   List<String> record = new ArrayList<>();  
                   toRoot(head.getParent(),record);  
                   newrecords.add(record);  
                }  
            }  
            //System.out.println("-----------------");  
            //递归之,以求子FP-Tree  
            //fpgrowth(newrecords,itemname, support, orderedMap);  
            fpgrowth(newrecords,itemname, support);
        }  
    } 
    
    //保存次序，此步也可以省略，为了减少再加工结果的麻烦而加  
    public static LinkedHashMap<String,Integer> getOrderedMap(List<TreeNode> orderheader){ 
    	LinkedHashMap<String,Integer> orderedMap = new LinkedHashMap<>(); 
        for (int i=0; i<orderheader.size(); i++) {  
            TreeNode node=orderheader.get(i);  
            orderedMap.put(node.getName(), i);  
        }  
        return orderedMap;  
    }   
}  