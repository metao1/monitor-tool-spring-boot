package com.metao.monitor.monitortoolspringboot.service;

public interface MovingAverageCalculator {

    public double getAverage(int windowSize);

    public double updateSum(double sum, int windowSize);

}
