package com.metao.monitor.monitortoolspringboot.controller;

import javax.validation.constraints.NotNull;

import com.metao.monitor.monitortoolspringboot.model.AverageViewModel;
import com.metao.monitor.monitortoolspringboot.repository.AverageViewRepository;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Controller
@RequestMapping("/average")
@RequiredArgsConstructor
public class AverageResponseTimeController {

    private final AverageViewRepository repository;

    @GetMapping
    @ResponseBody
    public Flux<AverageViewModel> getAverage(@RequestParam("window_size") @NotNull int windowSize) {
        return repository.findByWindowSize(windowSize);
    }
}
