package com.metao.monitor.monitortoolspringboot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class MovingAverageDataTest {
   
    @Test
    void testEquals() {        
        var mad1 = new MovingAverageData(10);
        var mad2 = new MovingAverageData(10);
        assertEquals(mad1, mad2);

        var mad3 = new MovingAverageData(20);
        var mad4 = new MovingAverageData(40);
        assertNotEquals(mad3, mad4);
    }
}
