package com.metao.monitor.monitortoolspringboot.service.impl;

import com.metao.monitor.monitortoolspringboot.config.MovingAverageConfig;
import com.metao.monitor.monitortoolspringboot.model.MovingAverageData;
import com.metao.monitor.monitortoolspringboot.repository.MovingAverageRepository;
import com.metao.monitor.monitortoolspringboot.service.MovingAverageCalculator;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovingAverageCalculatorService implements MovingAverageCalculator {

    private final MovingAverageRepository repository;
    private final MovingAverageConfig averageConfig;

    public double getAverage(int windowSize) {
        if (windowSize == 0) {
            return 0.0;
        }
        if (repository.existsById(windowSize)) {
            return repository.findById(windowSize).getAverage(windowSize);            
        }
        return 0.0;
    }

    @Override
    public void updateAverage(long responseTime) {
        for (int windowSize : averageConfig.getWindowSizes()) {
            final MovingAverageData movingAverage;
            if (repository.existsById(windowSize)) {
                movingAverage = repository.findById(windowSize);
            } else {
                movingAverage = new MovingAverageData(windowSize);
            }
            movingAverage.saveResponseTime(responseTime);
            repository.save(windowSize, movingAverage);
        }
    }
}
