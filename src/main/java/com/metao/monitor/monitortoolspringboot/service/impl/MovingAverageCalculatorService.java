package com.metao.monitor.monitortoolspringboot.service.impl;

import java.time.Instant;

import com.metao.monitor.monitortoolspringboot.model.AverageViewModel;
import com.metao.monitor.monitortoolspringboot.model.MovingAverageData;
import com.metao.monitor.monitortoolspringboot.model.ResponseData;
import com.metao.monitor.monitortoolspringboot.repository.AverageViewRepository;
import com.metao.monitor.monitortoolspringboot.repository.DataResponseRepositroy;
import com.metao.monitor.monitortoolspringboot.repository.MovingAverageRepository;
import com.metao.monitor.monitortoolspringboot.service.MovingAverageCalculator;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MovingAverageCalculatorService implements MovingAverageCalculator {

    private final MovingAverageRepository repository;
    private final AverageViewRepository viewRepository;
    private final DataResponseRepositroy dataRepository;

    public double getAverage(int windowSize) {
        if (windowSize == 0) {
            return 0.0;
        }
        if (repository.existsById(windowSize)) {
            return repository.findById(windowSize).getAverage();
        }
        return 0.0;
    }

    public int getOffset(int windowSize) {
        if (windowSize == 0) {
            return 0;
        }
        if (repository.existsById(windowSize)) {
            return repository.findById(windowSize).getOffset().get();
        }
        return 0;
    }

    public void increaseOffset(int windowSize) {
        if (windowSize == 0) {
            return;
        }
        if (repository.existsById(windowSize)) {
            repository.findById(windowSize).increaseOffset();
        }
    }

    @Override
    public double updateSum(double sum, int windowSize) {
        final MovingAverageData movingAverage;
        if (repository.existsById(windowSize)) {
            movingAverage = repository.findById(windowSize);
        } else {
            movingAverage = new MovingAverageData(windowSize);
        }
        movingAverage.updateSum(sum);
        repository.save(windowSize, movingAverage);
        return movingAverage.getSum();
    }

    public Mono<AverageViewModel> calculateMovingAverageForWindowSize(int windowSize, String url) {        
        return dataRepository.findByUrl(url, windowSize, getOffset(windowSize))
                .map(ResponseData::getResponseTime)
                .reduce(0d, (a, b) -> a + b)
                .map(sum -> updateSum(sum, windowSize))
                .map(average -> average / windowSize)
                .map(average -> new AverageViewModel(Instant.now().toEpochMilli(), windowSize, average))
                .flatMap(viewRepository::save)
                .doOnNext(next -> increaseOffset(windowSize));
    }
}
