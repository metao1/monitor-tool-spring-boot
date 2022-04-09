package com.metao.monitor.monitortoolspringboot.service.impl;

import java.time.Duration;
import java.time.Instant;

import com.metao.monitor.monitortoolspringboot.model.AverageViewModel;
import com.metao.monitor.monitortoolspringboot.repository.AverageViewRepository;
import com.metao.monitor.monitortoolspringboot.repository.MovingAverageRepository;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovingAverageCalculatorScheduler {

    private static final int TIMEOUT = 200;// 200 ms
    private final MovingAverageRepository averageRepository;
    private final AverageViewRepository viewRepository;

    @Scheduled(fixedDelay = 10 * 1000, initialDelay = 10 * 1000) // every 10 seconds by default
    public void runTenSecondAverageCalculator() {
        saveMovingAverageCalculator(10)
                .timeout(Duration.ofMillis(TIMEOUT))
                .onErrorContinue(this::reportError)
                .subscribe();
    }

    @Scheduled(fixedDelay = 60 * 1000, initialDelay = 60 * 1000) // every 60 seconds by default
    public void runOneMinuteAverageCalculator() {
        saveMovingAverageCalculator(60)
                .timeout(Duration.ofMillis(TIMEOUT))
                .onErrorContinue(this::reportError)
                .subscribe();
    }

    @Scheduled(fixedDelay = 3600 * 1000, initialDelay = 3600 * 1000) // every 3600 seconds by default
    public void runOneHourAverageCalculator() {
        saveMovingAverageCalculator(3600)
                .timeout(Duration.ofMillis(TIMEOUT))
                .onErrorContinue(this::reportError)
                .subscribe();
    }

    @Async
    private Mono<AverageViewModel> saveMovingAverageCalculator(int windowSize) {
        if (averageRepository.existsById(windowSize)) {
            var movingAverage = averageRepository.findById(windowSize);
            var average = movingAverage.getAverage(windowSize);
            return viewRepository.save(new AverageViewModel(Instant.now().toEpochMilli(), windowSize, average));
        }
        return Mono.empty();
    }

    private void reportError(Throwable throwable, Object object2) {
        log.error("error occured: " + throwable.getMessage());
    }

}
