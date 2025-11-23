package vn.uit.clothesshop.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeConverter {
    public static Instant getInstantFromStartLocalDate(LocalDate localDate) {
        LocalDateTime startTime = localDate.atStartOfDay();
        return getInstantFromLocalDateTime(startTime);
    }
    public static Instant getInstantFromEndLocalDate(LocalDate localDate) {
        LocalDateTime endTime = localDate.atTime(LocalTime.MAX);
        return getInstantFromLocalDateTime(endTime);

    }
    private static Instant getInstantFromLocalDateTime(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return zonedDateTime.toInstant();
    }
}
