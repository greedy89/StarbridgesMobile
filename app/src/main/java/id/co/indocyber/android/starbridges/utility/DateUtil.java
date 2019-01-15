package id.co.indocyber.android.starbridges.utility;

import java.util.Calendar;

public class DateUtil {
    public static void setFirstTimeOfDay(Calendar calendar) {
        if (calendar != null) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }
    }

    public static void setLastTimeOfDay(Calendar calendar)
    {
        if (calendar != null) {
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
        }
    }
    public static void setFirstDayOfMonth(Calendar calendar)
    {
        if (calendar != null) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        }
    }

    public static void setLastDayOfMonth(Calendar calendar)
    {
        if (calendar != null) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH,1);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
    }
}
