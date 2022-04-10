package com.metao.monitor.monitortoolspringboot.controller;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.metao.monitor.monitortoolspringboot.model.DataTransferObject.AverageViewModelDTO;
import com.metao.monitor.monitortoolspringboot.repository.AverageViewRepository;
import com.metao.monitor.monitortoolspringboot.service.impl.AverageViewModelConverter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
@RequestMapping("/average")
public class AverageResponseTimeController {

    private final AverageViewRepository repository;
    private final AverageViewModelConverter converter;

    @GetMapping
    @ResponseBody
    public Flux<AverageViewModelDTO> getAverage(@RequestParam("window_size") @NotNull int windowSize) {
        return repository
                .findByWindowSize(windowSize)
                .filter(Objects::nonNull)
                .map(averageData -> converter.toDto(averageData));
    }
}
