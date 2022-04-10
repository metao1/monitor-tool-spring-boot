package com.metao.monitor.monitortoolspringboot.model;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import lombok.Getter;

@Getter
public class MovingAverageData {

    private AtomicReference<Double> atomicAverage = new AtomicReference<>(0.0);
    private AtomicInteger offset = new AtomicInteger(0);
    private final int windowSize;

    public MovingAverageData(int windowSize) {
        assert windowSize > 0 : "Window size must be greater than 0";
        this.windowSize = windowSize;
    }

    public void increaseOffset(int delta) {
        offset.addAndGet(delta);
    }

    public void decrementOffset() {
        offset.decrementAndGet();
    }

    public void updateAverage(double average) {
        atomicAverage.lazySet(average);
    }

    public double getAverage() {
        return atomicAverage.getOpaque();
    }

    @Override
    public boolean equals(Object obj) {      
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MovingAverageData other = (MovingAverageData) obj;
        if (windowSize != other.windowSize) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {    
        int hash = 7;
        hash = 31 * hash + windowSize;
        return hash;
    }

}
