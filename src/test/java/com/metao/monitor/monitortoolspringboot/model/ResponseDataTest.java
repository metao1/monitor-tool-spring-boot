package com.metao.monitor.monitortoolspringboot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
