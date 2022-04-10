package com.metao.monitor.monitortoolspringboot.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.metao.monitor.monitortoolspringboot.config.MovingAverageConfig;
import com.metao.monitor.monitortoolspringboot.model.ResponseData;
import com.metao.monitor.monitortoolspringboot.repository.AverageViewRepository;
import com.metao.monitor.monitortoolspringboot.repository.DataResponseRepositroy;
import com.metao.monitor.monitortoolspringboot.repository.MovingAverageRepository;
import com.metao.monitor.monitortoolspringboot.service.impl.MovingAverageCalculatorService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
public class AverageDataCalculatorServiceTest {

    @Spy
    MovingAverageRepository averageRepository;

    @Mock
    MovingAverageConfig config;

    @Mock
    DataResponseRepositroy dataResponseRepository;

    @Spy
    AverageViewRepository averageViewRepository;

    @InjectMocks
    MovingAverageCalculatorService averageDataCalculatorService;

    @Test
    void testUpdateAverage() {
        var windowSizes = Stream.of(10000, 20000, 40000).toList();
        var url = "htp://example.com";

        Flux.range(1, 3600)
                .map(i -> new ResponseData(url, 200, supplyNumber().get(), Instant.now().toEpochMilli()))
                .subscribe(dataResponseRepository::save);

        var timestamp = Instant.now().getEpochSecond();

        for (int windowSize : windowSizes) {
            when(dataResponseRepository.findByUrl(url, windowSize, 0))
                    .thenReturn(Flux.just(new ResponseData(url, 200, supplyNumber().get(), timestamp)));
            averageDataCalculatorService.calculateMovingAverageForWindowSize(windowSize, windowSize, url);
        }

        verify(averageRepository, times(1)).existsById(10000);
        verify(averageRepository, times(1)).existsById(20000);
        verify(averageRepository, times(1)).existsById(40000);
    }

    private static Supplier<Long> supplyNumber() {
        return () -> 20l;
    }
}
