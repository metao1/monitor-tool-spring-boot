package com.metao.monitor.monitortoolspringboot.service.impl;

import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.validation.constraints.NotNull;

import com.metao.monitor.monitortoolspringboot.model.ResponseData;
import com.metao.monitor.monitortoolspringboot.repository.DataResponseRepositroy;
import com.metao.monitor.monitortoolspringboot.service.RemoteHostMonitor;
import com.metao.monitor.monitortoolspringboot.service.URLValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class DataResponseService extends RemoteHostMonitor<Mono<ResponseData>> {

    private final long TIMEOUT; // TIMEOUT can't be more than interval, 10 seconds is the default
    private final DataResponseRepositroy dataResponseRepository;
    private final WebClient webClient;

    @Autowired
    public DataResponseService(
            @Value("${monitor.remote.host.interval:10000}") long timeout,
            URLValidator urlValidator,
            DataResponseRepositroy dataResponseRepository,
            WebClient webClient) {
        super(urlValidator);
        this.TIMEOUT = timeout;
        this.dataResponseRepository = dataResponseRepository;
        this.webClient = webClient;
    }

    @Override
    protected Mono<ResponseData> callRemoteHost(@NotNull String url) throws UnknownHostException {
        return webClient.get()
                .uri(url)
                .retrieve()
                .toBodilessEntity()
                .elapsed() // get the time it took to get the response
                .map(it -> buildResponseData(url, it.getT1(), it.getT2()))
                .filter(Objects::nonNull)
                .flatMap(this::saveResponse)
                .timeout(Duration.ofMillis(TIMEOUT))
                .onErrorContinue(reportError(url))
                .doOnSuccess(reportSuccess());
    }

    protected ResponseData buildResponseData(String url, final long responseTime, ResponseEntity<Void> responseEntity) {
        final ResponseData responseData;
        try {
            responseData = new ResponseData(url, responseEntity.getStatusCodeValue(), responseTime, Instant.now().toEpochMilli());
        } catch (NullPointerException e) {
            log.error("Error while building response data for url {}", url, e);
            return null;
        }
        return responseData;
    }

    private Mono<ResponseData> saveResponse(ResponseData response) {
        return dataResponseRepository.save(response);
    }

    protected Consumer<ResponseData> reportSuccess() {
        return data -> {
            log.debug("data saved: {}", data);
        };
    }

    protected BiConsumer<Throwable, Object> reportError(String url) {
        return (e, s) -> {
            log.error("Error while monitoring {}, message: {}", url, e.getMessage());
        };
    }
}
