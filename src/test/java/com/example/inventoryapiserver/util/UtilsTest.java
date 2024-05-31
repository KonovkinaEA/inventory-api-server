package com.example.inventoryapiserver.util;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsTest {

    @Test
    void testConvertToMillisecondsValidDate() {
        String validDateStr = "25.12.2021";
        long expectedMilliseconds = 0;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date date = dateFormat.parse(validDateStr);
            expectedMilliseconds = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long actualMilliseconds = Utils.convertToMilliseconds(validDateStr);

        assertEquals(expectedMilliseconds, actualMilliseconds);
    }

    @Test
    void testConvertToMillisecondsEmptyString() {
        long actualMilliseconds = Utils.convertToMilliseconds("");
        assertEquals(0, actualMilliseconds);
    }

    @Test
    void testConvertToMillisecondsInvalidDate() {
        long actualMilliseconds = Utils.convertToMilliseconds("invalid-date");
        assertEquals(0, actualMilliseconds);
    }

    @Test
    void testConvertToDateValidMilliseconds() {
        long validMilliseconds = 1639939200000L; // 19.12.2021 in milliseconds
        String actualDate = Utils.convertToDate(validMilliseconds);
        assertEquals("19.12.2021", actualDate);
    }

    @Test
    void testConvertToDateNullMilliseconds() {
        String actualDate = Utils.convertToDate(null);
        assertEquals("", actualDate);
    }

    @Test
    void testConvertToDateZeroMilliseconds() {
        String actualDate = Utils.convertToDate(0L);
        assertEquals("01.01.1970", actualDate);
    }
}
