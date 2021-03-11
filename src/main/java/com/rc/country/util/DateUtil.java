package com.rc.country.util;

import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class DateUtil {

    public Date getDateByYear(int year) {
        ZonedDateTime date = ZonedDateTime.of(year, 12, 31, 23, 59, 59, 999, ZoneId.of("UTC"));
        return Date.from(date.toInstant());
    }
}
