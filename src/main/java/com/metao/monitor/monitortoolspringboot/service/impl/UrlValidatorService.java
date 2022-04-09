package com.metao.monitor.monitortoolspringboot.service.impl;

import java.net.MalformedURLException;

import com.metao.monitor.monitortoolspringboot.service.URLValidator;

import org.springframework.stereotype.Service;

@Service
public class UrlValidatorService implements URLValidator {

    private String urlRegex = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    @Override
    public void validateUrl(String url) throws MalformedURLException {
        if (url == null || url.isEmpty()) { 
            throw new MalformedURLException("URL is empty");
        }
        if (!url.matches(urlRegex)) {
            throw new MalformedURLException(String.format("URL %s is not valid", url));
        }
    }

}
