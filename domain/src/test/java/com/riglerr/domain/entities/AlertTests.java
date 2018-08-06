package com.riglerr.domain.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalTime;


public class AlertTests {
    @Test
    public void EqualsTest() {
        LocalTime time = LocalTime.parse("16:30:00");
        long interval = Duration.parse("P7D").getSeconds();
        String message = "@raiders Raid starting in 30 minutes.";
        var obj1 = new Alert(time, interval, message, 1, 1);
        var obj2 = new Alert(time, interval, message, 1, 1);
        var outcome = obj1.equals(obj2);

        assertTrue(outcome);
        assertEquals(obj1, obj2);
    }
}
