package com.metao.monitor.monitortoolspringboot.controller;

import static org.mockito.Mockito.when;

import java.time.Instant;

import com.metao.monitor.monitortoolspringboot.model.ResponseData;
import com.metao.monitor.monitortoolspringboot.repository.DataResponseRepositroy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;

@WebFluxTest(controllers = MonitorController.class)
public class MonitorControllerTest {

    @MockBean
    DataResponseRepositroy repository;

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void testMonitor_whenGetAll_thenReturnAll() {
        var url = "http://httpstat.us/200";
        var responseData = new ResponseData(url, 200, 400, Instant.now().toEpochMilli());
        when(repository.findAllWithLimit(1)).thenReturn(Flux.just(responseData));
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/monitor/all").queryParam("limit", 1).build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].url")
                .isEqualTo(url)
                .jsonPath("$[0].status")
                .isEqualTo(200);
    }
}
