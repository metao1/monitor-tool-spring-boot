package com.metao.monitor.monitortoolspringboot.service.impl;

import java.net.UnknownHostException;
import java.time.Instant;

import javax.validation.constraints.NotNull;

import com.metao.monitor.monitortoolspringboot.model.ResponseData;
import com.metao.monitor.monitortoolspringboot.service.RemoteHostMonitor;
import com.metao.monitor.monitortoolspringboot.service.URLValidator;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemoteHostFakeMonitor extends RemoteHostMonitor<ResponseData> {

    @Autowired
    public RemoteHostFakeMonitor(URLValidator urlValidator) {
        super(urlValidator);
    }

    @Override
    protected ResponseData callRemoteHost(@NotNull String url) throws UnknownHostException {
        log.info("Fake monitor url {}", url);
        var responseData = new ResponseData(url, 200, 129, Instant.now().toEpochMilli());
        return responseData;
    }

}
