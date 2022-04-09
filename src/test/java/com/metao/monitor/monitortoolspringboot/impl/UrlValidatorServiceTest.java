package com.metao.monitor.monitortoolspringboot.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.MalformedURLException;

import com.metao.monitor.monitortoolspringboot.service.impl.UrlValidatorService;

import org.junit.jupiter.api.Test;

public class UrlValidatorServiceTest {

    @Test
    void testMonitorWhenWrongRemoteHost_thenRaiseException() {
        UrlValidatorService urlValidatorService = new UrlValidatorService();
        assertThrows(MalformedURLException.class, () -> urlValidatorService.validateUrl(null));
        assertThrows(MalformedURLException.class, () -> urlValidatorService.validateUrl("mallformed"));
        assertThrows(MalformedURLException.class, () -> urlValidatorService.validateUrl("www.google.com"));
    }

    @Test
    void testMonitorWhenRightRemoteHost_thenIsOk() {
        UrlValidatorService urlValidatorService = new UrlValidatorService();
        try {
            urlValidatorService.validateUrl("https://www.google.com");
            urlValidatorService.validateUrl("http://www.google.com");
            urlValidatorService.validateUrl("http://example.de");
        } catch (MalformedURLException ex) {
            fail("Should not have thrown any exception but it happened:" + ex.getMessage());
        }

    }
}
