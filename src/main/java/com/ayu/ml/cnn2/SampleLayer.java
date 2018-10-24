package com.ayu.ml.cnn2;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

import com.ayu.ml.cnn2.DataSet.Record;
import com.ayu.ml.cnn2.MathUtil;
import com.ayu.ml.cnn2.Layer;

/**
 * Created by shaoaq on 16-10-9.
 */
public class SampleLayer implements Layer {
    private final Size scaleSize;
    private final int outCount;
    private final Layer preLayer;
    private final Size size;
    private final double[][][][] outs;
    private final double[][][][] errors;
    private Layer nextLayer;

    public SampleLayer(int batchSize, Size scaleSize, Layer preLayer) {
        this.scaleSize = scaleSize;
        this.outCount = preLayer.getOutCount();//采样层输出和上一层输出个数一致
        this.preLayer = preLayer;
        this.size = preLayer.getSize().divide(scaleSize);
        errors = new double[batchSize][outCount][size.getWidth()][size.getWidth()];
        outs = new double[batchSize][outCount][size.getWidth()][size.getHeight()];
    }


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
    	
    	preLayer.forEachOutput(record, i-> lastRectangle -> {
            outs[record.getIndex()][i] = MathUtil.scaleMatrix(lastRectangle, scaleSize);
        });
    }

    public Size getScaleSize() {
        return scaleSize;
    }

    @Override
    public Size getSize() {
        return size;
    }

    @Override
    public int getOutCount() {
        return outCount;
    }
    
    @Override
    public boolean setErrors(Record record) {
    	
    	//int recordIndex = record.getIndex();
    	
        if (nextLayer instanceof LayerWithKernel) {
            forEachOutput(record, i -> out -> {
                final double[][][] sum = {null};// 对每一个卷积进行求和
                nextLayer.forEachOutput(record, j -> nextOut -> {
                    double[][] nextError = nextLayer.getError(record, j);
                    double[][] nextKernel = ((LayerWithKernel) nextLayer).getKernel(i, j);
                    double[][] roatedNextKernel = MathUtil.transRotate180(nextKernel);
                    if (sum[0] == null) {
                        sum[0] = MathUtil.fullConvolutional(nextError, roatedNextKernel);
                    } else {
                        sum[0] = MathUtil.trans(MathUtil.fullConvolutional(nextError, roatedNextKernel), sum[0], v1 -> v2 -> v1 + v2);
                    }
                });
                errors[record.getIndex()][i] = sum[0];
            });
        }
        return true;
    }

    @Override
    public void setNextLayer(Layer layer) {
        this.nextLayer = layer;
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
    public void forward(DataSet.Record record) {
        setOutput(record);
    }

    @Override
    public boolean backPropagation(DataSet.Record record) {
        return setErrors(record);
    }
}
