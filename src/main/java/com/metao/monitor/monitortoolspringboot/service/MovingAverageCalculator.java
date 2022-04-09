package com.metao.monitor.monitortoolspringboot.service;

public interface MovingAverageCalculator {

    public double getAverage(int windowSize);

    public void updateAverage(long responseTime);

}
