package com.ayu.ml.ann;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SigmoidTest {
    @Test
    public void DefaultSigmoidTranfer() {
        Sigmoid s = new DefaultSigmoid();
        assertEquals(s.transfer(1), 1);
    }

    @Test
    public void LogSigmoidTranfer() {
        Sigmoid s = new LogSigmoid(0, 1);
        assertTrue(s.transfer(1)!=1);
    }
}
