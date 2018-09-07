package id.co.indocyber.android.starbridges.reminder.alarmManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmGetGeoLocation {
    public static void start(Context context)
    {
        Calendar calendar = Calendar.getInstance();
        int m = calendar.get(Calendar.MINUTE);
        long start = System.currentTimeMillis();
        int remain=0;
        if (m<15)
        {
            remain = 15-m;
        }
        else if (m<30)
        {
            remain = 30-m;
        }
        else if (m<45)
        {
            remain = 45-m;
        }
        else
        {
            remain = 60-m;
        }
        remain=(int)start + remain*60*1000;// convert it to milisecond and plus it to current time;

        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, GeoReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP,remain, pi);
    }
}
