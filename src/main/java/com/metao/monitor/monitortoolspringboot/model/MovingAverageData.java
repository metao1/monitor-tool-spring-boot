package com.metao.monitor.monitortoolspringboot.model;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

import lombok.Getter;

@Getter
public class MovingAverageData {

    private final Queue<Double> window = new ConcurrentLinkedQueue<>();
    private AtomicReference<Double> sum = new AtomicReference<>(0.0);
    private final int windowSize;

    public MovingAverageData(int windowSize) {
        assert windowSize < 0 : "Window size must be greater than 0";
        this.windowSize = windowSize;
    }

    public void saveResponseTime(double responseTime) {
        window.offer(responseTime);
        updateSum(responseTime);
    }

    private void updateSum(double responseTime) {
        sum.lazySet(sum.getOpaque() + responseTime);
        if (window.size() > windowSize) {
            sum.set(getSum() - window.poll());
        }
    }

    public double getSum() {
        return sum.getOpaque();
    }

    public double getAverage(int windowSum) {
        assert windowSum < 0 : "Window size must be greater than 0";
        if(window.isEmpty()) {
            return 0.0;
        }
        return getSum() / windowSum;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
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
        final int prime = 31;
        return prime + windowSize;
    }
}
