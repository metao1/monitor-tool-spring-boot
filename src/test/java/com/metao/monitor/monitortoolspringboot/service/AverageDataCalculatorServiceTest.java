package com.metao.monitor.monitortoolspringboot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.metao.monitor.monitortoolspringboot.config.MovingAverageConfig;
import com.metao.monitor.monitortoolspringboot.model.ResponseData;
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

    @InjectMocks
    MovingAverageCalculatorService averageDataCalculatorService;

    @Test
    void testUpdateAverage() {
        var windowSizes = Stream.of(10000, 20000, 40000).toList();
        when(config.getWindowSizes()).thenReturn(windowSizes);

        Flux.range(1, 3600)
                .map(i -> new ResponseData(1 + "", 200, supplyNumber().get(), Instant.now().toEpochMilli()))
                .map(ResponseData::getResponseTime)
                .subscribe(averageDataCalculatorService::updateAverage);

        verify(averageRepository, times(3600)).existsById(10000);
        verify(averageRepository, times(3600)).existsById(20000);
        verify(averageRepository, times(3600)).existsById(40000);

        assertEquals(7.2, averageDataCalculatorService.getAverage(10000));
        assertEquals(3.6, averageDataCalculatorService.getAverage(20000));
        assertEquals(1.8, averageDataCalculatorService.getAverage(40000));
    }

    @Test
    void testGetAverage_thenReturnFromCache() {
        var windowSizes = Stream.of(10).toList();
        when(config.getWindowSizes()).thenReturn(windowSizes);
        Flux.range(1, 10) // 9 times in total
                .map(i -> new ResponseData(1 + "", 200, supplyNumber().get(), Instant.now().toEpochMilli()))
                .map(ResponseData::getResponseTime)
                .subscribe(averageDataCalculatorService::updateAverage);

        assertEquals(20d, averageDataCalculatorService.getAverage(10));

        // findById is called 9 times, plus 2 times for average in this test
        verify(averageRepository, times(11)).existsById(10);
    }

    private static Supplier<Long> supplyNumber() {
        return () -> 20l;
    }
}
