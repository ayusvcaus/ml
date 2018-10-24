package com.ayu.ml.cnn;

import com.ayu.ml.cnn.CNN.LayerBuilder;
import com.ayu.ml.cnn.Layer.Size;
import com.ayu.ml.cnn.Dataset;
import com.ayu.ml.cnn.ConcurenceRunner;
import com.ayu.ml.cnn.TimedTest;
import com.ayu.ml.cnn.TimedTest.TestTask;

public class RunCNN {

	public static void runCnn() {
		//创建一个卷积神经网络
		LayerBuilder builder = new LayerBuilder();
		builder.addLayer(Layer.buildInputLayer(new Size(28, 28)));
		builder.addLayer(Layer.buildConvLayer(6, new Size(5, 5)));
		builder.addLayer(Layer.buildSampLayer(new Size(2, 2)));
		builder.addLayer(Layer.buildConvLayer(12, new Size(5, 5)));
		builder.addLayer(Layer.buildSampLayer(new Size(2, 2)));
		builder.addLayer(Layer.buildOutputLayer(10));
		CNN cnn = new CNN(builder, 50);
		
		//导入数据集
		String fileName = "data/cnn/train.format";
		Dataset dataset = Dataset.load(fileName, ",", 784);
		cnn.train(dataset, 3);//
		//String modelName = "model/model.cnn";
		String modelName = "data/cnn/model.cnn";
		cnn.saveModel(modelName);		
		dataset.clear();
		dataset = null;
		
		//预测
		// CNN cnn = CNN.loadModel(modelName);	
		Dataset testset = Dataset.load("data/cnn/test.format", ",", -1);
		cnn.predict(testset, "data/cnn/test.predict");
	}

	public static void main(String[] args) {

		new TimedTest(new TestTask() {
			@Override
			public void process() {
				runCnn();
			}
		}, 1).test();
		ConcurenceRunner.stop();

	}

}
