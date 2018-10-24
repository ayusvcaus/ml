package com.ayu.ml.cnn2;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

import com.ayu.ml.cnn2.DataSet.Record;
import com.ayu.ml.cnn2.MathUtil;
import com.ayu.ml.cnn2.Layer;


/**
 * Created by shaoaq on 16-10-8.
 */
public class OutputLayer implements LayerWithKernel {
    private final Size size;
    private final double[] bias;
    private final double[][][][] errors;
    private final int batchSize;
    private final int outCount;
    private final Layer preLayer;
    private final double[][][][] outs;
    private double[][][][] kernels;
    //private Layer nextLayer;


    public OutputLayer(int batchSize, int classNum, Layer preLayer) {
        this.batchSize = batchSize;
        this.outCount = classNum;
        this.preLayer = preLayer;
        this.size = new Size(1, 1);
        Size kernelSize = preLayer.getSize();      
        this.kernels = new double[preLayer.getOutCount()][classNum][kernelSize.getWidth()][kernelSize.getHeight()];
        //Kernels初始化为随机数?
        Random random = new Random();
        for (int i = 0; i < kernels.length; i++) {
            for (int j = 0; j < kernels[0].length; j++) {
                for (int k = 0; k < kernels[0][0].length; k++) {
                    for (int l = 0; l < kernels[0][0][0].length; l++) {
                        kernels[i][j][k][l] = (random.nextDouble() - 0.5) / 10;
                    }
                }
            }
        }
        bias = new double[classNum];
        errors = new double[batchSize][outCount][size.getWidth()][size.getHeight()];
        outs = new double[batchSize][outCount][size.getWidth()][size.getHeight()];
    }

    /**
     * 对矩阵元素求和
     *
     * @param error
     * @return 注意这个求和很可能会溢出
     */


    @Override
    public void forEachOutput(Record record, Function<Integer, Consumer<double[][]>> function) {
       
    	//for (int i = 0; i < outCount; i++) {
        //    function.apply(i).accept(outs[record.getIndex()][i]);
        //}
        
        IntStream.range(0, outCount).parallel().forEach(i->{
        	function.apply(i).accept(outs[record.getIndex()][i]);
        });
    }

    @Override
    public void setOutput(Record record) {
    	 //TODO: 可以并行化
    	
    	//int recordIndex = record.getIndex();
    	
    	/*
        for (int j = 0; j < outs[recordIndex].length; j++) {
            final int finalJ = j;
            final double[][][] sum = {null};// 对每一个输入map的卷积进行求和
            lastLayer.forEachOutput(recordIndex, i -> lastRectangle -> {
                double[][] kernel = kernels[i][finalJ];
                if (sum[0] == null) {
                    sum[0] = MathUtil.validConvolutional(lastRectangle, kernel);
                } else {
                    sum[0] = MathUtil.trans(MathUtil.validConvolutional(lastRectangle, kernel), sum[0], v1 -> v2 -> v1 + v2);
                }
            });
            final double bias = this.bias[j];
            sum[0] = MathUtil.trans(sum[0], value -> MathUtil.sigmoid(value + bias));
            outs[recordIndex][j] = sum[0];
        }
        */
    	
        IntStream.range(0, outCount).parallel().forEach(j->{
            final int finalJ = j;
            final double[][][] sum = {null};// 对每一个输入map的卷积进行求和
            preLayer.forEachOutput(record, i -> lastRectangle -> {
                double[][] kernel = kernels[i][finalJ];
                double [][] tmp = MathUtil.validConvolutional(lastRectangle, kernel);
                if (sum[0] == null) {
                    sum[0] = tmp;
                } else {
                    sum[0] = MathUtil.trans(tmp, sum[0], v1 -> v2 -> v1 + v2);
                }
            });
            final double bias = this.bias[j];
            sum[0] = MathUtil.trans(sum[0], value -> MathUtil.sigmoid(value + bias));
            outs[record.getIndex()][j] = sum[0];
        });
    }

    @Override
    public Size getSize() {
        return null;
    }

    @Override
    public int getOutCount() {
        return outCount;
    }

    @Override
    public boolean setErrors(Record record) {
    	
    	//int recordIndex = record.getIndex();
    	
        double[] target = new double[outCount];
        double[] realOuts = Arrays.stream(outs[record.getIndex()]).mapToDouble(out -> out[0][0]).toArray();
        int label = record.getLabel();
        target[label] = 1;
        /*
        for (int i = 0; i < outCount; i++) {
            errors[recordIndex][i][0][0] = realOuts[i] * (1 - realOuts[i]) * (target[i] - realOuts[i]);
        }
        */
        IntStream.range(0, outCount).parallel().forEach(i->{
        	errors[record.getIndex()][i][0][0] = realOuts[i] * (1 - realOuts[i]) * (target[i] - realOuts[i]);
        });
        return label == MathUtil.getMaxIndex(realOuts);
    }

    public int getLabel(Record record) {
        double[] realOuts = Arrays.stream(outs[record.getIndex()]).mapToDouble(out -> out[0][0]).toArray();
        return  MathUtil.getMaxIndex(realOuts);
    }

    @Override
    public void setNextLayer(Layer nextLayer) {
        //this.nextLayer = nextLayer;
    }

    @Override
    public double[][] getError(Record record, int outIndex) {
        return errors[record.getIndex()][outIndex];
    }

    @Override
    public double[][] getOut(int recordIndex, int outIndex) {
        return outs[recordIndex][outIndex];
    }

    @Override
    public void forward(Record record) {
        setOutput(record);
    }

    @Override
    public double[][] getKernel(int preLayerOutIndex, int thisLayerOutIndex) {
        return kernels[preLayerOutIndex][thisLayerOutIndex];
    }

    @Override
    public void updateKernels() {
    	/*
        for (int j = 0; j < outCount; j++) {
            for (int i = 0; i < preLayer.getOutCount(); i++) {
            	// 对batch的每个记录delta求和
                double[][] deltaKernel = null;
                for (int r = 0; r < batchSize; r++) {
                    double[][] error = errors[r][j];
                    if (deltaKernel == null) {
                        deltaKernel = MathUtil.validConvolutional(preLayer.getOut(r, i), error);
                    } else {// 累积求和
                        deltaKernel = MathUtil.trans(
                                deltaKernel,
                                MathUtil.validConvolutional(preLayer.getOut(r, i), error),
                                v1 -> v2 -> v1 + v2);
                    }
                }
                deltaKernel = MathUtil.trans(deltaKernel, v -> v / batchSize);
                // 更新卷积核
                double[][] kernel = kernels[i][j];
                deltaKernel = MathUtil.trans(MathUtil.trans(kernel, v -> v * (1 - Cnn.LAMBDA * Cnn.ALPHA))
                        , MathUtil.trans(deltaKernel, v -> v * Cnn.ALPHA),
                        v1 -> v2 -> v1 + v2);
                kernels[i][j] = deltaKernel;
            }
        }
        */
        IntStream.range(0, outCount).parallel().forEach(j->{
        	IntStream.range(0, preLayer.getOutCount()).forEach(i->{
        		// 对batch的每个记录delta求和
                double[][] deltaKernel = null;
                for (int r = 0; r < batchSize; r++) {
                    double[][] error = errors[r][j];
                    double[][] tmp =  MathUtil.validConvolutional(preLayer.getOut(r, i), error);
                    if (deltaKernel == null) {
                        deltaKernel = tmp;
                    } else {// 累积求和
                        deltaKernel = MathUtil.trans(
                                deltaKernel,
                                tmp,
                                v1 -> v2 -> v1 + v2);
                    }
                }
                deltaKernel = MathUtil.trans(deltaKernel, v -> v / batchSize);
                // 更新卷积核
                double[][] kernel = kernels[i][j];
                deltaKernel = MathUtil.trans(MathUtil.trans(kernel, v -> v * (1 - Cnn.LAMBDA * Cnn.ALPHA))
                        , MathUtil.trans(deltaKernel, v -> v * Cnn.ALPHA),
                        v1 -> v2 -> v1 + v2);
                kernels[i][j] = deltaKernel;
        	});
        });
    }

    @Override
    public void updateBias() {
        /*
    	for (int j = 0; j < outCount; j++) {
            double[][] error = sum(errors, j);
            double deltaBias = sum(error) / batchSize;
            double bias = this.bias[j] + Cnn.ALPHA * deltaBias;
            this.bias[j] = bias;
        }
        */
        IntStream.range(0, outCount).parallel().forEach(j->{
        	double[][] error = MathUtil.sum(errors, j);
            double deltaBias = MathUtil.sum(error) / batchSize;
            double bias = this.bias[j] + Cnn.ALPHA * deltaBias;
            this.bias[j] = bias;
        });
    }
        

    @Override
    public boolean backPropagation(Record record) {
        return setErrors(record);
    }
}
