package com.metao.monitor.monitortoolspringboot.controller;

import java.time.Duration;
import java.util.Objects;

import com.metao.monitor.monitortoolspringboot.model.DataTransferObject.ResponseTimeDTO;
import com.metao.monitor.monitortoolspringboot.repository.DataResponseRepositroy;
import com.metao.monitor.monitortoolspringboot.service.impl.ResponseTimeConverter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Controller
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class MonitorController {

    private static final long TIMEOUT = 200;
    private final DataResponseRepositroy repository;
    private final ResponseTimeConverter converter;

    @ResponseBody
    @GetMapping("/responses")
    public Flux<ResponseTimeDTO> getAllResponseDataWithLimit(@RequestParam("limit") int limit) {
        return repository
                .findAllWithLimit(limit)
                .timeout(Duration.ofMillis(TIMEOUT))
                .filter(Objects::nonNull)
                .map(converter::toDto)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
