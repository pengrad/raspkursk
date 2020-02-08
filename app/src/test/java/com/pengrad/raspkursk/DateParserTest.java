package com.pengrad.raspkursk;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DateParserTest {

    @Test
    public void timeNoSecs() {
        assertEquals(DateParser.timeNoSecs("22:33"), "22:33");
        assertEquals(DateParser.timeNoSecs("22:31:51"), "22:31");
        assertEquals(DateParser.timeNoSecs("2015-01-01 12:55:44"), "12:55");
    }
}