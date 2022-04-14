package com.metao.monitor.monitortoolspringboot;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.metao.monitor.monitortoolspringboot.service.impl.RemoteHostFakeMonitor;
import com.metao.monitor.monitortoolspringboot.service.impl.UrlValidatorService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
public class RemoteHostMonitorTest {

    @Mock
    UrlValidatorService urlValidatorService;

    @InjectMocks
    RemoteHostFakeMonitor fakeMonitor;

    @Test
    @SneakyThrows
    void testMonitor_whenRightRemoteHost_thenReturnReponseData() {
        var url = "https://www.google.com";
        doNothing().when(urlValidatorService).validateUrl(url);
        var responseData = fakeMonitor.monitor(url);
        verify(urlValidatorService, times(1)).validateUrl(url); // verify that urlValidatorService was called once
        
        assertAll("responseData",
                () -> assertEquals(url, responseData.getUrl()),
                () -> assertEquals(200, responseData.getStatus()),
                () -> assertTrue(responseData.getResponseTime() > 0));
    }
}
