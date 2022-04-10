package com.metao.monitor.monitortoolspringboot.model;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class MovingAverageData {

    private AtomicReference<Double> atomicSum = new AtomicReference<>(0.0);
    private AtomicInteger offset = new AtomicInteger(0);
    private final int windowSize;

    public MovingAverageData(int windowSize) {
        assert windowSize > 0 : "Window size must be greater than 0";
        this.windowSize = windowSize;
    }

    public void increaseOffset() {
        offset.incrementAndGet();
    }
    
    public void updateSum(double sum) {
        atomicSum.lazySet(sum);
    }

    public double getSum() {
        return atomicSum.getOpaque();
    }

    public double getAverage() {
        return getSum() / windowSize;
    }
}
