package com.metao.monitor.monitortoolspringboot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.time.Duration;

import com.metao.monitor.monitortoolspringboot.model.ResponseData;
import com.metao.monitor.monitortoolspringboot.repository.DataResponseRepositroy;
import com.metao.monitor.monitortoolspringboot.service.impl.DataResponseService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import lombok.SneakyThrows;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RemoteHostMonitorIntegrationTest {

    @Autowired
    private DataResponseService remoteHostMonitor;

    @SpyBean
    private DataResponseRepositroy repository;

    @Test
    @SneakyThrows
    void testMonitor_whenRemoteHostSends301_thenIsOk() {
        var url = "https://google.com";
        StepVerifier
                .create(remoteHostMonitor.monitor(url))
                .consumeNextWith(responseData -> {
                    assertNotNull(responseData);
                    assertEquals(301, responseData.getStatus());
                    assertEquals(url, responseData.getUrl());
                    assertThat(responseData.getResponseTime()).isGreaterThan(0);
                })
                .verifyComplete();
                verify(repository, times(1)).save(any(ResponseData.class));
    }

    @Test
    @SneakyThrows
    void testMonitor_whenRemoteHostSends200_thenIsOk() {
        var url = "http://httpstat.us/200";

        StepVerifier
                .create(remoteHostMonitor.monitor(url))
                .consumeNextWith(responseData -> {
                    assertNotNull(responseData);
                    assertEquals(200, responseData.getStatus());
                    assertEquals(url, responseData.getUrl());
                    assertThat(responseData.getResponseTime()).isGreaterThan(0);
                })
                .verifyComplete();
    }

    @Test
    void testMonitor_whenUrlIsWrong_thenRaiseError() {
        try {
            remoteHostMonitor.monitor("http://ghiolit.com");
        } catch (MalformedURLException ex) {
            assertNotNull(ex);
        } catch (UnknownHostException e) {
            fail();
        }
    }

    @Test
    @SneakyThrows
    void testMonitor_whenRemoteHostSends200_thenSaveIntoRepository() {
        var url = "http://localhost:8080/monitor/all";
        StepVerifier
                .create(remoteHostMonitor.monitor(url))
                .expectTimeout(Duration.ofMillis(10000));
    }
}
