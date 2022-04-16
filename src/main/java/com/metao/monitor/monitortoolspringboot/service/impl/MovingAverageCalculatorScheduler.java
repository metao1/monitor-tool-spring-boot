package com.metao.monitor.monitortoolspringboot.service.impl;

import java.time.Duration;

import com.metao.monitor.monitortoolspringboot.config.MovingAverageConfig;
import com.metao.monitor.monitortoolspringboot.model.AverageViewModel;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@Profile("!test")
@RequiredArgsConstructor
public class MovingAverageCalculatorScheduler {

    private static final int TIMEOUT = 200;// 200 ms
    private final MovingAverageCalculatorService movingAverageCalculatorService;
    private final MovingAverageConfig config;

    @Scheduled(fixedDelay = 10 * 1000, initialDelay = 10 * 1000) // every 10 seconds by default
    public void runTenSecondAverageCalculator() {
        calculateMovingAverageForWindowSize(10)
                .subscribeOn(Schedulers.boundedElastic())
                .timeout(Duration.ofMillis(TIMEOUT))                
                .onErrorContinue(this::reportError)
                .subscribe();
    }

    @Scheduled(fixedDelay = 60 * 1000, initialDelay = 60 * 1000) // every 60 seconds by default
    public void runOneMinuteAverageCalculator() {
        calculateMovingAverageForWindowSize(60)
                .subscribeOn(Schedulers.boundedElastic())
                .timeout(Duration.ofMillis(TIMEOUT))                
                .onErrorContinue(this::reportError)
                .subscribe();
    }

    @Scheduled(fixedDelay = 3600 * 1000, initialDelay = 3600 * 1000) // every3600 seconds by default
    public void runOneHourAverageCalculator() {
        calculateMovingAverageForWindowSize(3600)
                .subscribeOn(Schedulers.boundedElastic())
                .timeout(Duration.ofMillis(TIMEOUT))                
                .onErrorContinue(this::reportError)
                .subscribe();
    }

    private Mono<AverageViewModel> calculateMovingAverageForWindowSize(int windowSize) {
        assert config != null;
        assert windowSize > 0 : "window size must be greater than 0";
        return movingAverageCalculatorService.calculateMovingAverageForWindowSize(windowSize, config.getInterval(),
                config.getUrl());

    }

    private void reportError(Throwable throwable, Object object2) {
        log.error("error occured: " + throwable.getMessage());
    }

}
