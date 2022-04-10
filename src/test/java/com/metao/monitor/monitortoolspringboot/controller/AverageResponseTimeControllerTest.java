package com.metao.monitor.monitortoolspringboot.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;

import com.metao.monitor.monitortoolspringboot.model.AverageViewModel;
import com.metao.monitor.monitortoolspringboot.model.DataTransferObject.AverageViewModelDTO;
import com.metao.monitor.monitortoolspringboot.repository.AverageViewRepository;
import com.metao.monitor.monitortoolspringboot.service.impl.AverageViewModelConverter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;

@WebFluxTest(controllers = AverageResponseTimeController.class)
public class AverageResponseTimeControllerTest {

    @MockBean
    private AverageViewRepository averageViewRepository;

    @MockBean
    private AverageViewModelConverter averageViewModelConverter;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testGetAverage_withoutParameter_isBadRequest() {
        webTestClient
                .get()
                .uri("/average")
                .exchange()
                .expectStatus()
                .isBadRequest();
    }
    
    @Test
    void testGetAverage_wihParameter_isOK_thenReturnsAverage() {
        var windowSize = 10;
        var average = 10.0;
        var timestamp  = Instant.now().toEpochMilli();
        var averageViewModel = new AverageViewModel(timestamp, windowSize, average);
        when(averageViewRepository.findByWindowSize(windowSize))
                .then((answer) -> Flux.just(averageViewModel));
        when(averageViewModelConverter.toDto(averageViewModel))
                .then((answer) -> new AverageViewModelDTO(
                        averageViewModel.getWindowSize(),
                        averageViewModel.getMovingAverageResponseTime(),
                        averageViewModel.getId()));
        webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/average").queryParam("window_size", windowSize).build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].window_size").isEqualTo(windowSize)
                .jsonPath("$[0].moving_average_response_time").isEqualTo(average)
                .jsonPath("$[0].timestamp").isNotEmpty()
                .jsonPath("$[0].timestamp").isEqualTo(timestamp);

        verify(averageViewModelConverter).toDto(averageViewModel);
    }
}
