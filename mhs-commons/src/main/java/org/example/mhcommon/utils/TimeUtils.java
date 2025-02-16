package org.example.mhcommon.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtils {
    public static long getCurrentTimeLong() {
        return System.currentTimeMillis();
    }
    public static LocalDateTime longtoLocalDatetime(Long time){
        if(time == null) time = getCurrentTimeLong();
        return Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
