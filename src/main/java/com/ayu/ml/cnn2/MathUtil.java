package com.ayu.ml.cnn2;

import java.util.function.Function;

/**
 * Created by shaoaq on 16-10-9.
 */
public class MathUtil {
	
    public static double[][] trans(final double[][] doubles, Function<Double, Double> function) {
        double[][] trans = new double[doubles.length][doubles[0].length];
       
        for (int i = 0; i < doubles.length; i++) {
            for (int j = 0; j < doubles[0].length; j++) {
                trans[i][j] = function.apply(doubles[i][j]);
            }
        }
        /*
        IntStream.range(0, doubles.length).forEach(i->{
        	IntStream.range(0, doubles[0].length).forEach(j->{
        		trans[i][j] = function.apply(doubles[i][j]);
        	});
        });
        */
        return trans;
    }

    public static double[][] trans(final double[][] doubles1, final double[][] doubles2, Function<Double, Function<Double, Double>> function) {
        if (doubles1.length != doubles2.length) {
            throw new RuntimeException("two matrix length not equal");
        }
        if (doubles1[0].length != doubles2[0].length) {
            throw new RuntimeException("two matrix length not equal");
        }
        double[][] trans = new double[doubles1.length][doubles1[0].length];
        
        for (int i = 0; i < doubles1.length; i++) {
            for (int j = 0; j < doubles1[0].length; j++) {
                trans[i][j] = function.apply(doubles1[i][j]).apply(doubles2[i][j]);
            }
        }
        return trans;
    }

    /**
     * 矩阵放大
     *
     * @param matrix
     * @param scale
     * @return
     */
    public static double[][] scale(final double[][] matrix, final Size scale) {
        final int m = matrix.length;
        int n = matrix[0].length;
        final double[][] outMatrix = new double[m * scale.getWidth()][n * scale.getHeight()];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int ki = i * scale.getWidth(); ki < (i + 1) * scale.getWidth(); ki++) {
                    for (int kj = j * scale.getHeight(); kj < (j + 1) * scale.getHeight(); kj++) {
                        outMatrix[ki][kj] = matrix[i][j];
                    }
                }
            }
        }
        return outMatrix;
    }


    public static double[][] transRotate180(double[][] doubles) {
        doubles = trans(doubles, v -> v); //deep copy
        int m = doubles.length;
        int n = doubles[0].length;
        // 按列对称进行交换
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n / 2; j++) {
                double tmp = doubles[i][j];
                doubles[i][j] = doubles[i][n - 1 - j];
                doubles[i][n - 1 - j] = tmp;
            }
        }
        // 按行对称进行交换
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m / 2; i++) {
                double tmp = doubles[i][j];
                doubles[i][j] = doubles[m - 1 - i][j];
                doubles[m - 1 - i][j] = tmp;
            }
        }
        return doubles;
    }


    public static Double sigmoid(double x) {
        return 1 / (1 + Math.pow(Math.E, -x));
    }

    private static void p(double[][] matrix) {
        for (int i = 0; i < matrix[0].length; i++) {
            System.out.print("[");
            for (int j = 0; j < matrix.length; j++) {
                System.out.print((j > 0 ? " " : "") + matrix[j][i]);
            }
            System.out.println("]");
        }
    }

    /**
     * 计算full模式的卷积
     * 所谓full模式卷积，在原矩阵的四周补上0值(图片补白边)，使卷积核移动可移动到原矩阵的边缘。然后对该扩展矩阵进行卷积
     * 比如：
     * <pre>
     *         原矩阵                 kernel                             扩展矩阵
     *                                                   [0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0]
     * [1.1 1.2 1.1 1.2 1.1]                             [0.0 0.0 1.1 1.2 1.1 1.2 1.1 0.0 0.0]
     * [1.2 0.0 1.0 0.0 1.2]       [1.0 1.0 2.0]         [0.0 0.0 1.2 0.0 1.0 0.0 1.2 0.0 0.0]
     * [1.1 0.0 1.0 0.0 1.1]       [0.0 1.0 1.0]         [0.0 0.0 1.1 0.0 1.0 0.0 1.1 0.0 0.0]
     * [1.2 1.1 1.2 1.1 1.2]                             [0.0 0.0 1.2 1.1 1.2 1.1 1.2 0.0 0.0]
     *                                                   [0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0]
     * </pre>
     *
     * @param matrix
     * @param kernel
     * @return
     */
    public static double[][] fullConvolutional(double[][] matrix, final double[][] kernel) {
    	// 扩展矩阵
        final double[][] extend = new double[matrix.length + 2 * (kernel.length - 1)][matrix[0].length + 2 * (kernel[0].length - 1)];
        
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++)
                extend[i + kernel.length - 1][j + kernel[0].length - 1] = matrix[i][j];
        }
        /*
        
        IntStream.range(0, matrix.length).parallel().forEach(i->{
        	IntStream.range(0, matrix[0].length).forEach(j->{
        		extend[i + kernel.length - 1][j + kernel[0].length - 1] = matrix[i][j];
        	});
        });
        */
        return validConvolutional(extend, kernel);
    }

    /**
     * validate 卷积
     * 计算方法：
     * * 将卷积核矩阵放到源矩阵左上角上，然后一列一列的往右移动或一行一行向下移动直到边缘。
     * * 每移动一个新位置，即将卷积核的每个元素与覆盖矩阵相应位置上的每个元素乘积，求和后作为卷积结果。
     * <pre>
     *          源矩阵　　　　　　　　　　卷积核　　　　　　　　　卷积结果矩阵
     *     [1.0 1.0 1.0 1.0]                             [2.0 3.0 3.0]
     *     [0.0 0.0 1.0 1.0]         [1.0 1.0]           [1.0 2.0 2.0]
     *     [0.0 1.0 1.0 0.0]         [0.0 1.0]           [2.0 3.0 1.0]
     *     [0.0 1.0 1.0 0.0]
     *
     * </pre>
     *
     * @param doubles
     * @param kernel
     * @return
     */
    public static double[][] validConvolutional(double[][] doubles, double[][] kernel) {
        double[][] convolution = new double[doubles.length - kernel.length + 1][doubles[0].length - kernel[0].length + 1];

        for (int i = 0; i < convolution.length; i++) {
            for (int j = 0; j < convolution[0].length; j++) {
                double sum = 0.0;
                for (int ki = 0; ki < kernel.length; ki++) {
                    for (int kj = 0; kj < kernel[0].length; kj++) {
                        sum += doubles[i + ki][j + kj] * kernel[ki][kj];
                    }
                }
                convolution[i][j] = sum;
            }
        }
        return convolution;
    }
    
    public static int getMaxIndex(double[] out) {
        double max = out[0];
        int index = 0;
        for (int i = 1; i < out.length; i++)
            if (out[i] > max) {
                max = out[i];
                index = i;
            }
        return index;
    }
    
    public static double sum(double[][] error) {
        int m = error.length;
        int n = error[0].length;
        double sum = 0.0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                sum += error[i][j];
            }
        }
        return sum;
    }

    /**
     * 对errors[...][j]元素求和
     *
     * @param errors
     * @param j
     * @return
     */
    public static double[][] sum(double[][][][] errors, int j) {
        int m = errors[0][j].length;
        int n = errors[0][j][0].length;
        double[][] result = new double[m][n];
        for (int mi = 0; mi < m; mi++) {
            for (int nj = 0; nj < n; nj++) {
                double sum = 0;
                for (int i = 0; i < errors.length; i++)
                    sum += errors[i][j][mi][nj];
                result[mi][nj] = sum;
            }
        }
        return result;
    }
    
    public static double[][] scaleMatrix(final double[][] matrix, final Size scale) {
    	int m = matrix.length;
    	int n = matrix[0].length;
    	final int sm = m / scale.getWidth();
    	final int sn = n / scale.getHeight();
    	final double[][] outMatrix = new double[sm][sn];
    	if (sm * scale.getWidth() != m || sn * scale.getHeight() != n) {
    	    throw new RuntimeException("scale不能整除matrix");
    	}
    	final int size = scale.getWidth() * scale.getHeight();

    	for (int i = 0; i < sm; i++) {
    	    for (int j = 0; j < sn; j++) {
    	        double sum = 0.0;
    	        for (int si = i * scale.getWidth(); si < (i + 1) * scale.getWidth(); si++) {
    	            for (int sj = j * scale.getHeight(); sj < (j + 1) * scale.getHeight(); sj++) {
    	                sum += matrix[si][sj];
    	            }
    	        }
    	        outMatrix[i][j] = sum / size;
    	    }
    	}      
    	return outMatrix;
    }
}
