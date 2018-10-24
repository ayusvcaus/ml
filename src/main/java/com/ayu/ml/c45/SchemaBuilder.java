package com.ayu.ml.c45;

import java.io.BufferedReader;
import java.io.FileReader;

import com.ayu.ml.c45.Const;
import com.ayu.ml.c45.Schema;

public class SchemaBuilder {
	
	public Schema buildSchema(String FileName) throws Exception {
		
		BufferedReader reader = new BufferedReader(new FileReader(FileName));
		Schema result = new Schema();
		String titles[] = reader.readLine().split(",");
		for (String s:titles) {
			result.addAttributesName(s);
		}
		String buffer = "";
		while ((buffer=reader.readLine())!=null) {
			if (buffer.contains("?")) {
				continue;
			}
			String[] val = buffer.split(",");
			//we only consider integer or double here..
			for (String s:val) {
				if (s.contains(".")) {
					result.addType(Const.DOUBLE);
				} else {
					result.addType(Const.INTEGER);
				}
			}
			break; //read one line only
		}
		for (int i=0; i<result.length(); i++) {
			result.addDataType(Const.CONTINUOUS);
		}
		reader.close();
		return result;
	}	
}
