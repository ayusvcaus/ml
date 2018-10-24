package com.ayu.ml.c45;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import java.util.Map.Entry;

import com.ayu.ml.c45.ConfigReader;
import com.ayu.ml.c45.DataEntry;
import com.ayu.ml.c45.Schema;
import com.ayu.ml.c45.SplitResult;

public class EntropyTools {
	public static SplitResult getSplitResult(int idx, Schema schema, List<DataEntry> records) {

		SplitResult result = null;
		if (!schema.isDiscrete(idx)) {
			// this means the data is continuous
			// remove those attributes is missing
			List<DataEntry> data = filterData(idx, records);
			double entropy = getEntropy(data);
			Collections.sort(data, new Comparator<DataEntry>() {
				public int compare(DataEntry o1, DataEntry o2) {
					return Tool.comp(o1.getData(idx), o2.getData(idx));
				}
			});
			// the naive way just split at the middle. Actually it can be
			// optimized by target value changed.
			
			List<DataEntry> left = new ArrayList<>();
			List<DataEntry> right = new ArrayList<>(data);
			Map<String,Long> leftMap = new HashMap<>();
			Map<String,Long> rightMap = new HashMap<>(getTargetValueMap(data));

			double maxGainRatio = 0;
			for (int i=0; i<data.size()-1; i++) {
				left.add(data.get(i));
				String key = data.get(i).getData(schema.length()-1);
				Long val = leftMap.get(key);
				leftMap.put(key, val==null?1L:val+1);
				right.remove(0);
				val = rightMap.get(key);
				rightMap.put(key, val-1);
				if (!left.get(left.size() - 1).getData(idx).equals(right.get(0).getData(idx))) {
					double attributeVal = Tool.add(data.get(i).getData(idx), data.get(i + 1).getData(idx)) / 2;
					double leftEntropy = getEntropy(leftMap,left.size());
					double rightEntropy = getEntropy(rightMap,right.size());
					double attributeEntropyWithWeight = Tool.div(left.size() + "", data.size() + "") * leftEntropy + Tool.div(right.size() + "", data.size() + "") * rightEntropy;
					double attributeEntropy = getSplitRation2((double)left.size()/data.size());
					double infoGain = entropy-attributeEntropyWithWeight;
					double gainRatio = infoGain/attributeEntropy;
					//System.out.println("F:"+GainRatio+" E:"+Entropy+" "+schema.getName(order)+","+AttributeVal+",V:"+AttributeInfo+",S:"+splitratio+",###==L:"+left.size()+","+leftEntropy+"==R:"+right.size()+","+rightEntropy);
					if (gainRatio>maxGainRatio) {
						maxGainRatio = gainRatio;
						result = new SplitResult();
						result.setInfoGain(infoGain);
						result.setInfoGainRatio(gainRatio);
						result.addDescribe("<="+attributeVal);
						result.addDescribe(">"+attributeVal);
						result.addData(new ArrayList<>(left));
						result.addData(new ArrayList<>(right));
					}
				}
			}
			//At this case means the attribute value is all the same
			if (maxGainRatio==0){
				result = new SplitResult();
				result.setInfoGain(0);
				result.setInfoGainRatio(0);
				result.addDescribe("="+(data.size()==0?"null":data.get(0).getData(idx)));
				result.addData(new ArrayList<>(data));
			}
		} else {
			assert (schema.getDataType(idx) == Const.DISCRETE);
			Map<String, List<DataEntry>> map = null;
			try {
				map = dataToMap(idx, records);
			} catch (Exception e) {
				System.err.println(idx);
				for (DataEntry dd:records) {
					System.err.println(dd.toString());
				}
				result = new SplitResult();
				result.setInfoGainRatio(0);
				return result;
			}
			result = new SplitResult();
			double infoGain = getEntropy(records) - getAttributeEntropyWithWeight(map, records.size());
			double gainRatio = infoGain/getAttributeEntropy(map, (long)records.size());
			result.setInfoGain(infoGain);
			result.setInfoGainRatio(gainRatio);
			Iterator<Entry<String, List<DataEntry>>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, List<DataEntry>> e = it.next();
				result.addDescribe("=" + e.getKey());
				result.addData(e.getValue());
			}
		}
		result.setAttributeName(schema.getName(idx));
		return result;
	}
	
	public static double getEntropy(Map<String, Long> targetMap, long totalNum){
		assert totalNum>0;
		double entropy = 0;
		Iterator<Entry<String, Long>> it = targetMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Long> e = it.next();
			if (e.getValue()>0) {
			    double p = (double)e.getValue()/totalNum;
			    entropy += p*Tool.log2(p)*(-1);
			}
		}
		return entropy;
	}
	
	public static double getEntropy(List<DataEntry> data) {
		Map<String, Long> map = getTargetValueMap(data);
		return getEntropy(map, data.size());
	}
	
	public static Map<String,Long> getTargetValueMap(List<DataEntry> data){
		Map<String, Long> map = new HashMap<>();
		for (DataEntry e : data) {
			String key = e.getData(e.length() - 1);
			Long val = map.get(key);
			map.put(key, val == null ? 1L : val + 1);
		}
		return map;
	}

	private static Map<String, List<DataEntry>> dataToMap(int idx, List<DataEntry> record) {
		Map<String, List<DataEntry>> map = new HashMap<String, List<DataEntry>>();
		List<DataEntry> missingAttributes = new ArrayList<>();
		long max = Long.MIN_VALUE;
		String contents = "";
		for (DataEntry e : record) {
			if (e.getData(idx).equals("?")) {
				missingAttributes.add(e);
				continue;
			}
			List<DataEntry> val = map.get(e.getData(idx));
			if (val == null) {
				val = new ArrayList<>();
			}
			val.add(e);
			map.put(e.getData(idx), val);
		}
		Iterator<Entry<String, List<DataEntry>>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, List<DataEntry>> e = it.next();
			if (e.getValue().size() > max) {
				contents = e.getKey();
				max = e.getValue().size();
			}
		}
		List<DataEntry> val = map.get(contents);
		val.addAll(missingAttributes);
		map.put(contents, val);
		return map;
	}


	private static double getAttributeEntropyWithWeight(Map<String, List<DataEntry>> map, long totalNum) {
		double info = 0;
		Iterator<Entry<String, List<DataEntry>>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, List<DataEntry>> e = it.next();
			info += Tool.div(e.getValue().size() + "", totalNum + "") * getEntropy(e.getValue());
		}
		return info;
	}

	private static double getAttributeEntropy(Map<String, List<DataEntry>> map, long totalNum) {
		double ratio = 0;
		Iterator<Entry<String, List<DataEntry>>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, List<DataEntry>> e = it.next();
			double p = Tool.div(e.getValue().size() + "", totalNum + "");
			ratio += p * Tool.log2(p) * (-1);
		}
		return ratio;
	}

	private static double getSplitRation2(double p) {
		//double ratio = p * Tool.log2(p) + (1 - p) * Tool.log2(1 - p);
		//return (-1)*ratio;
		return (-1)* (p * Tool.log2(p) + (1 - p) * Tool.log2(1 - p));
	}
	
	private static List<DataEntry> filterData(int idx, List<DataEntry> data) {
		List<DataEntry> result = new ArrayList<>();
		for (DataEntry e : data) {
			if (!e.getData(idx).equals("?"))
				result.add(e);
		}
		return result;
	}
	
	public static boolean isPure(List<DataEntry> data) throws Exception{
		Map<String, Long> map = getTargetValueMap(data);
		if (map.size()==1) {
			return true;
		}
		double entropy = getEntropy(map, data.size());
		if (entropy<ConfigReader.getAccuracy()) {
			return true;
		}
		return false;
	}
	
	public static String getMostTargetValue(List<DataEntry> data){
		if (data.size()==0) {
			return "None";
		}
		Map<String, Long> map = new HashMap<>();
		long max = Long.MIN_VALUE;
		String result = "";
		for (DataEntry d:data) {
			String key = d.getData(d.length()-1);
			Long val = map.get(key);
			map.put(key, val==null?1L:val+1);
			if(map.get(key)>max){
				max = map.get(key);
				result =key;
			}
		}
		return result;
	}
}