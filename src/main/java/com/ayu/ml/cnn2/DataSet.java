package com.ayu.ml.cnn2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Created by shaoaq on 16-10-8.
 */
public class DataSet {
    private final long size;
    private final long trainSize;
    private final long testSize;
    //private final String fileName;
    private final Set<Long> testSelected;
    private final List<Record> data;

    public DataSet(String fileName, double testRatio) throws IOException {
        //this.fileName = fileName;
    	data = new ArrayList<>();
        /*try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            size = reader.lines().count();
        }*/
        size = readFile(fileName);
        this.testSize = (long) (size * testRatio);
        this.trainSize = size - testSize;
        testSelected = ThreadLocalRandom.current().longs(0, size).distinct().limit(testSize).boxed().collect(Collectors.toSet());
    }

    public long getTrainSize() {
        return trainSize;
    }

    /**
     * 从DataSet训练数据里随机获取指定个数的数据执行
     *
     * @param size
     * @param consumer
     */
    public void randomTrainRecordForEach(int size, Consumer<Record> consumer) throws IOException {
        Set<Long> selected = ThreadLocalRandom.current().longs(0, trainSize).distinct().limit(size).boxed().collect(Collectors.toSet());
        /*try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            long lineNumber = 0L;
            long trainIndex = -1L;
            int recordIndex = 0;
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (!testSelected.contains(lineNumber)) {
                    trainIndex++;
                    if (selected.contains(trainIndex)) {
                        double[] doubles = Arrays.stream(line.split(",")).mapToDouble(Double::parseDouble).toArray();
                        Record record = new Record(new Double(doubles[0]).intValue(), recordIndex++, Arrays.copyOfRange(doubles, 1, doubles.length));
                        consumer.accept(record);
                    }
                }
                lineNumber++;
            }
        }
        
        ////
        long trainIndex = -1L;
        int recordIndex = 0;
    	for (int i=0; i<data.size(); i++) {
    		if (!testSelected.contains((long)i)) {
    			trainIndex++;
    			if (selected.contains(trainIndex)) {
    				Record r = data.get(i);
    				r.index = recordIndex++;
                    consumer.accept(r);
    			}
            }   	
    	}
        */
        AtomicLong trainIndex = new AtomicLong(-1L);
        AtomicInteger recordIndex = new AtomicInteger(0);
    	LongStream.range(0, data.size()).parallel().forEach(i->{
    		if (!testSelected.contains(i)) {
    			if (selected.contains(trainIndex.incrementAndGet())) {
    				Record r = data.get((int)i);
    				r.index = recordIndex.getAndIncrement();
                    consumer.accept(r);
    			}
            }  
    	});
    }

    /**
     * 遍历DataSet的测试数据执行
     *
     * @param consumer
     */
    public void testRecordForEach(Consumer<Record> consumer) throws IOException {
        /*try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            long lineNumber = 0L;
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (testSelected.contains(lineNumber)) {
                    double[] doubles = Arrays.stream(line.split(",")).mapToDouble(Double::parseDouble).toArray();
                    Record record = new Record(new Double(doubles[0]).intValue(), 0, Arrays.copyOfRange(doubles, 1, doubles.length));
                    consumer.accept(record);
                }
                lineNumber++;
            }
        }
        ////
    	for (int i=0; i<data.size(); i++) {
    		if (testSelected.contains((long)i)) {
                consumer.accept(data.get(i));
            }   	
    	}
        */
    	LongStream.range(0, data.size()).forEach(i->{
    		if (testSelected.contains((long)i)) {
                consumer.accept(data.get((int)i));
            }  
    	});

    }
    
    public long readFile(String fileName) throws IOException {
    	long lineNumber = 0L;
    	try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) { 
                double[] doubles = Arrays.stream(line.split(",")).mapToDouble(Double::parseDouble).toArray();
                Record record = new Record(new Double(doubles[0]).intValue(), 0, Arrays.copyOfRange(doubles, 1, doubles.length), lineNumber++);
                data.add(record);
            }
        }
    	return lineNumber;
    }

    public static class Record {
        private final int label;
        //private final int index;
        private int index;
        private final double[] data;
        private final long lineNum;

        public Record(int label, int index, double[] data, long lineNum) {
            this.label = label;
            this.index = index;
            this.data = data;
            this.lineNum = lineNum;
        }

        public int getIndex() {
            return index;
        }

        public double[] getData() {
            return data;
        }

        public int getLabel() {
            return label;
        }
        
        public long getLineNum() {
            return lineNum;
        }
    }
}
