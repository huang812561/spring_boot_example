package com.hgq.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-05-24 17:27
 * @since 1.0
 **/
public class DateUtil {

    /**
     *
     * @param date
     * @param intervalSeconds
     * @return
     */
    public static Date addSeconds(Date date ,int intervalSeconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND,intervalSeconds);
        return calendar.getTime();
    }
}
