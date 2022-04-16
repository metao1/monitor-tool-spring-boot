package com.metao.monitor.monitortoolspringboot.service.impl;

import java.time.Instant;
import java.util.Objects;

import com.metao.monitor.monitortoolspringboot.model.AverageViewModel;
import com.metao.monitor.monitortoolspringboot.model.MovingAverageData;
import com.metao.monitor.monitortoolspringboot.model.ResponseData;
import com.metao.monitor.monitortoolspringboot.repository.AverageViewRepository;
import com.metao.monitor.monitortoolspringboot.repository.DataResponseRepositroy;
import com.metao.monitor.monitortoolspringboot.repository.MovingAverageRepository;
import com.metao.monitor.monitortoolspringboot.service.MovingAverageCalculator;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovingAverageCalculatorService implements MovingAverageCalculator {

    private final MovingAverageRepository movingAverageRepository;
    private final AverageViewRepository averageViewRepository;
    private final DataResponseRepositroy dataResponseRepository;

    public double getAverage(int windowSize) {
        if (windowSize == 0) {
            return 0.0;
        }
        if (movingAverageRepository.existsById(windowSize)) {
            return movingAverageRepository.findById(windowSize).getAverage();
        }
        return 0.0;
    }

    @Override
    public void updateAverage(double average, int windowSize) {
        final MovingAverageData movingAverage;
        if (movingAverageRepository.existsById(windowSize)) {
            movingAverage = movingAverageRepository.findById(windowSize);
        } else {
            movingAverage = new MovingAverageData(windowSize);
        }
        movingAverage.updateAverage(average);
        movingAverageRepository.save(windowSize, movingAverage);
    }

    public Mono<AverageViewModel> calculateMovingAverageForWindowSize(int windowSize, int interval, String url) {
        return dataResponseRepository.findByUrl(url, windowSize + getOffset(windowSize), getOffset(windowSize)) // query response times for the given url
                .filter(Objects::nonNull) // filter out null response times
                .map(ResponseData::getResponseTime) // get response times
                .reduce(0d, (a, b) -> a + b) // sum response times                
                .map(sum -> sum / windowSize) // calculate average
                .flatMap(average -> {
                    var averageViewModel = new AverageViewModel(Instant.now().toEpochMilli(), windowSize, average); // create average view model
                    try {
                        updateAverage(average, windowSize);// update average                        
                        increaseOffset(windowSize, interval); // increase offset
                        return averageViewRepository.save(averageViewModel); // save view model
                    } catch (Exception e) {
                        log.error("Error saving view model", e);
                        rollback(windowSize, averageViewModel);
                    }
                    return Mono.empty();
                });
    }

    private int getOffset(int windowSize) {
        if (windowSize == 0) {
            return 0;
        }
        if (movingAverageRepository.existsById(windowSize)) {
            return movingAverageRepository.findById(windowSize).getOffset().get();
        }
        return 0;
    }

    private void increaseOffset(int windowSize, int interval) {
        if (windowSize == 0 || interval == 0) {
            return;
        }
        if (movingAverageRepository.existsById(windowSize)) {
            movingAverageRepository.findById(windowSize).increaseOffset(windowSize * 1000 / interval);
        }
    }

    private void decrementOffset(int windowSize) {
        if (windowSize == 0) {
            return;
        }
        if (movingAverageRepository.existsById(windowSize)) {
            movingAverageRepository.findById(windowSize).decrementOffset();
        }
    }

    private void rollback(int windowSize, AverageViewModel averageViewModel) {
        try {
            movingAverageRepository.deleteById(windowSize);
            averageViewRepository.delete(averageViewModel);
            decrementOffset(windowSize);
        } catch (Exception ex) {
            log.error("could not rollback:" + ex.getMessage());
        }

    }
}
