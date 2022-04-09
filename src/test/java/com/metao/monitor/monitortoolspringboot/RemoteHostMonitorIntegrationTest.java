package com.metao.monitor.monitortoolspringboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.time.Instant;

import com.metao.monitor.monitortoolspringboot.model.ResponseData;
import com.metao.monitor.monitortoolspringboot.repository.DataResponseRepositroy;
import com.metao.monitor.monitortoolspringboot.service.impl.DataResponseService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import lombok.SneakyThrows;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RemoteHostMonitorIntegrationTest {

    @Autowired
    private DataResponseService remoteHostMonitor;

    @SpyBean
    DataResponseRepositroy repository;

    @Test
    void testResponseData_whenAreEqual_thenIsOk() {
        var responseData1 = new ResponseData("url1", 200, 10, Instant.now().toEpochMilli());
        var responseData2 = new ResponseData("url1", 200, 10, Instant.now().toEpochMilli());
        assertEquals(responseData1, responseData2);
    }

    @Test
    void testResponseData_whenAreNotEqual_thenIsNotOk() {
        var responseData1 = new ResponseData("url2", 200, 10, Instant.now().toEpochMilli());
        var responseData2 = new ResponseData("url1", 200, 10, Instant.now().toEpochMilli());
        assertNotEquals(responseData1, responseData2);
        assertNotEquals(new ResponseData(null, 200,10, Instant.now().toEpochMilli()),
                new ResponseData("url1", 200, 10, Instant.now().toEpochMilli()));
    }

    @Test
    @SneakyThrows
    void testMonitor_whenRemoteHostSends301_thenIsOk() {
        var url = "https://google.com";
        var responseData = new ResponseData(url, 301, 10, Instant.now().toEpochMilli());
        StepVerifier
                .create(remoteHostMonitor.monitor(url))
                .expectNext(responseData)
                .verifyComplete();
    }

    @Test
    @SneakyThrows
    void testMonitor_whenRemoteHostSends200_thenIsOk() {
        var url = "http://httpstat.us/200";
        var responseData = new ResponseData(url, 200, 10, Instant.now().toEpochMilli());

        StepVerifier
                .create(remoteHostMonitor.monitor(url))
                .expectNext(responseData)
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
        var url = "http://httpstat.us/200";
        var responseData = new ResponseData(url, 200, 10, Instant.now().toEpochMilli());

        StepVerifier
                .create(remoteHostMonitor.monitor(url))
                .expectNext(responseData)
                .verifyComplete();

        verify(repository, times(1)).save(responseData);
    }
}
