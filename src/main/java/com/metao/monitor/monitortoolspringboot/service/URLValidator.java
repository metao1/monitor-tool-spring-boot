package com.metao.monitor.monitortoolspringboot.service;

import java.net.MalformedURLException;

public interface URLValidator {
    void validateUrl(String url) throws MalformedURLException;
}
