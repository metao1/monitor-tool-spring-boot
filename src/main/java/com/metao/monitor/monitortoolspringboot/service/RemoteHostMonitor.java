package com.metao.monitor.monitortoolspringboot.service;

import java.net.MalformedURLException;
import java.net.UnknownHostException;

import javax.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class RemoteHostMonitor<T> {
    private final URLValidator urlValidator;

    public T monitor(@NotNull String url) throws MalformedURLException, UnknownHostException {
        urlValidator.validateUrl(url);
        return callRemoteHost(url);
    }

    protected abstract T callRemoteHost(@NotNull String url) throws UnknownHostException;
}
