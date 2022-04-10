package com.metao.monitor.monitortoolspringboot.service.impl;

import java.net.MalformedURLException;
import java.net.UnknownHostException;

import com.metao.monitor.monitortoolspringboot.config.MovingAverageConfig;
import com.metao.monitor.monitortoolspringboot.model.ResponseData;
import com.metao.monitor.monitortoolspringboot.service.RemoteHostMonitor;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@Profile("!test")
@RequiredArgsConstructor
public class RemoteHostMonitorScheduler {

    private final RemoteHostMonitor<Mono<ResponseData>> hostMonitor;
    private final MovingAverageConfig config;

    @Scheduled(fixedDelayString = "${monitor.interval:10000}") // every 10 seconds by default
    public void saveResponseTime() {
        if (config == null) {
            log.error("url is null, must be configured under monitor.host.url");
            return;
        }
        try {
            hostMonitor
                    .monitor(config.getUrl())
                    .map(ResponseData::getResponseTime)
                    .subscribe();
        } catch (MalformedURLException e) {
            log.error("Mallformed url", e.getMessage());
        } catch (UnknownHostException e) {
            log.error("Unknown host", e.getMessage());
        }
    }
}
