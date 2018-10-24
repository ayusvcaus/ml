package com.ayu.ml.cnn2;

import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.logging.Logger;

import com.ayu.ml.cnn2.Cnn;
import com.ayu.ml.cnn2.CnnBuilder;
import com.ayu.ml.cnn2.DataSet;
import com.ayu.ml.cnn2.Size;


/**
 * Created by shaoaq on 16-10-8.
 */
public class CNN2Test {
	
    private static Logger logger = Logger.getLogger(CNN2Test.class.getSimpleName());

    @Test
    public void testCNN2() throws IOException, ParserConfigurationException, ClassNotFoundException {
    	String path = "data/cnn2/";
        DataSet dataSet = new DataSet(path+"data.ds", 0.3);
        Cnn cnn = new CnnBuilder(50)
                .setInputLayer(new Size(28, 28))
                .addConvolutionalLayer(6, new Size(5, 5))
                .addSampleLayer(new Size(2, 2))
                .addConvolutionalLayer(12, new Size(5, 5))
                .addSampleLayer(new Size(2, 2))
                .setOutputLayer(10)
                .build();
        long now = System.currentTimeMillis();
        cnn.train(dataSet, 3);
        logger.info("cost:" + (System.currentTimeMillis() - now));
        cnn.saveModel(path+"demo.model");

        Cnn cnn1 = Cnn.readModel(path+"demo.model");
        final int[] testRight = {0};
        final int[] testCount = {0};
        now = System.currentTimeMillis();
        dataSet.testRecordForEach(record -> {
            if (cnn1.test(record)) {
                testRight[0]++;
            }
            testCount[0]++;
        });
        logger.info("cost:" + (System.currentTimeMillis() - now));
        double testP = 1.0 * testRight[0] / testCount[0];
        logger.info("test precision " + testRight[0] + "/" + testCount[0] + "=" + testP);
    }
}
