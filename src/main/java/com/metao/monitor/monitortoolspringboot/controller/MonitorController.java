package com.metao.monitor.monitortoolspringboot.controller;

import com.metao.monitor.monitortoolspringboot.model.ResponseData;
import com.metao.monitor.monitortoolspringboot.repository.DataResponseRepositroy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Controller
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class MonitorController {

    private final DataResponseRepositroy repository;

    @ResponseBody
    @GetMapping("/all")
    public Flux<ResponseData> getAllResponseDataWithLimit(@RequestParam("limit") int limit) {
        return repository.findAllWithLimit(limit);
    }
}
