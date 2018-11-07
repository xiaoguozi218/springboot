package com.example.java8;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

/**
 * Java 8 在包java.time下包含了一组全新的时间日期API。
 */
public class DateTest {

    public static void main(String[] args) {

        LocalTime late = LocalTime.of(23, 59, 59);
        System.out.println(late);       // 23:59:59
        DateTimeFormatter germanFormatter =
                DateTimeFormatter
                        .ofLocalizedTime(FormatStyle.SHORT)
                        .withLocale(Locale.GERMAN);
        LocalTime leetTime = LocalTime.parse("13:37", germanFormatter);
        System.out.println(leetTime);   // 13:37

    }
}
