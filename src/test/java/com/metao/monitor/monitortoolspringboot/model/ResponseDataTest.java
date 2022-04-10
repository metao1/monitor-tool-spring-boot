package com.metao.monitor.monitortoolspringboot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

public class ResponseDataTest {
   
    @Test
    void testEquals() {
        var time =  Instant.now().toEpochMilli();
        var rd1 = new ResponseData("1", 200, 1L, time);
        var rd2 = new ResponseData("1", 200, 1L, time);
        assertEquals(rd1, rd2);
    }

    @Test
    void testResponseData_whenAreNotEqual_thenIsNotOk() {
        var time =  Instant.now().toEpochMilli();
        var responseData1 = new ResponseData("url2", 200, 10, time);
        var responseData2 = new ResponseData("url1", 200, 10, time);
        assertNotEquals(responseData1, responseData2);
        assertNotEquals(new ResponseData(null, 200,10, Instant.now().toEpochMilli()),
                new ResponseData("url1", 200, 10, Instant.now().toEpochMilli()));
    }

}
