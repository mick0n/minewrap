package com.mistno.minewrap;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

public class TimeUtil {

    private TimeUtil() {}

    public static Seconds getSecondsUntilNextInterval(int intervalInHours) {
        DateTime currentDateTime = DateTime.now();
        int hoursUntilNextInterval = (23 - currentDateTime.getHourOfDay()) % intervalInHours;
        int hour = currentDateTime.getHourOfDay() + hoursUntilNextInterval;
        DateTime scheduledTime = new DateTime(currentDateTime.getYear(), currentDateTime.getMonthOfYear(), currentDateTime.getDayOfMonth(), hour, 55, currentDateTime.getZone());
        return Seconds.secondsBetween(currentDateTime, scheduledTime);
    }

}
