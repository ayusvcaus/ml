package com.ayu.ml.hmm;

/**
 * 缁寸壒姣旂畻娉�
 * @author hankcs
 */
public class Viterbi {
    /**
     * 求解HMM模型
     * @param obs 观测序列
     * @param states 隐状�?
     * @param start_p 初始概率（隐状�?�）
     * @param trans_p 转移概率（隐状�?�）
     * @param emit_p 发射概率 （隐状�?�表现为显状态的概率�?
     * @return �?可能的序�?
     */
	/*
states = ('Rainy', 'Sunny')
observations = ('walk', 'shop', 'clean')
start_probability = {'Rainy': 0.6, 'Sunny': 0.4}
transition_probability = {
    'Rainy' : {'Rainy': 0.7, 'Sunny': 0.3},
    'Sunny' : {'Rainy': 0.4, 'Sunny': 0.6},
    } 
emission_probability = {
    'Rainy' : {'walk': 0.1, 'shop': 0.4, 'clean': 0.5},
    'Sunny' : {'walk': 0.6, 'shop': 0.3, 'clean': 0.1},
}
	 */
		
    public static int[] compute(int[] obs, int[] states, double[] start_p, double[][] trans_p, double[][] emit_p) {
        double[][] V = new double[obs.length][states.length];
        int[][] path = new int[states.length][obs.length];

        for (int y : states) {
            V[0][y] = start_p[y] * emit_p[y][obs[0]];
            path[y][0] = y;
        }

        for (int t=1; t<obs.length; ++t) {
            int[][] newpath = new int[states.length][obs.length];

            for (int y : states) {
                double prob = -1;
                int state;
                for (int y0 : states) {
                    double nprob = V[t - 1][y0] * trans_p[y0][y] * emit_p[y][obs[t]];
                    if (nprob > prob) {
                        prob = nprob;
                        state = y0;
                        // 记录�?大概�?
                        V[t][y] = prob;
                        // 记录�?大概�?
                        System.arraycopy(path[state], 0, newpath[y], 0, t);
                        newpath[y][t] = y;
                    }
                }
            }

            path = newpath;
        }

        double prob = -1;
        int state = 0;
        for (int y : states) {
            if (V[obs.length - 1][y] > prob) {
                prob = V[obs.length - 1][y];
                state = y;
            }
        }

        return path[state];
    }
}
