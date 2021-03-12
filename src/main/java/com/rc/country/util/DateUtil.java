package com.rc.country.util;

import lombok.experimental.UtilityClass;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@UtilityClass
public class DateUtil {

    public static Date getDateByYear(int year) {
        ZonedDateTime date = ZonedDateTime.of(year, 12, 31, 23, 59, 59, 999, ZoneId.of("UTC"));
        return Date.from(date.toInstant());
    }
}
