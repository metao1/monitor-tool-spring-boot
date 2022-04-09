package com.metao.monitor.monitortoolspringboot;

import static org.mockito.Mockito.doNothing;

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
        fakeMonitor.monitor(url);
    }
}
