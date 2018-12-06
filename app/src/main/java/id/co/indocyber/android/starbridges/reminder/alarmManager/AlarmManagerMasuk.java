package id.co.indocyber.android.starbridges.reminder.alarmManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import id.co.indocyber.android.starbridges.model.EmployeeShiftSchedule.EmployeeShiftSchedule;
import id.co.indocyber.android.starbridges.model.EmployeeShiftSchedule.ReturnValue;
import id.co.indocyber.android.starbridges.network.StringConverter;
import id.co.indocyber.android.starbridges.utility.SharedPreferenceUtils;

import static android.content.Context.ALARM_SERVICE;


public class AlarmManagerMasuk {
    private static final int NOTIFICATION_ID = 0;
    private static final String ACTION_NOTIFY = "com.example.android.starbridges.ACTION_NOTIFY_MASUK";

    public static void start(Context ctx) {
        final AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);

//        Calendar checkOutTime2 = Calendar.getInstance();
//
//        if (DateFormat.is24HourFormat(ctx)) {
//            checkOutTime2.set(Calendar.HOUR_OF_DAY, 8);
//        } else {
//            checkOutTime2.set(Calendar.HOUR, 8);
//            checkOutTime2.set(Calendar.AM_PM, AM);
//        }
//        checkOutTime2.set(Calendar.MINUTE, 20);
//        checkOutTime2.set(Calendar.SECOND, 10);
//        checkOutTime2.set(Calendar.MILLISECOND, 0);

        Intent notifyIntent = new Intent(ACTION_NOTIFY);

        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (ctx.getApplicationContext(), NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar today = Calendar.getInstance();
        String employeeSchedule = SharedPreferenceUtils.getSetting(ctx,"employeeSchedule","");
        Gson gson=new Gson();
        Log.d("employeeSchedule", employeeSchedule);
        Calendar checkOutTime2 = Calendar.getInstance();
        if(employeeSchedule!=null&&employeeSchedule!="") {
            EmployeeShiftSchedule employeeShiftSchedule = gson.fromJson(employeeSchedule, EmployeeShiftSchedule.class);
            Log.d("employeeShiftSchedule", gson.toJson(employeeShiftSchedule.getReturnValue()));
            Date dateEmployee;
            Date dateToday = new Date();
            ReturnValue employeeShiftScheduleLogin = null;
            for (ReturnValue returnValue : employeeShiftSchedule.getReturnValue()) {
                dateEmployee = new StringConverter().dateToDate(returnValue.getDate());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                if (sdf.format(dateEmployee).equals(sdf.format(dateToday))) {
                    employeeShiftScheduleLogin = returnValue;
                    SharedPreferenceUtils.setSetting(ctx,"employeeShiftScheduleLogin", gson.toJson(employeeShiftScheduleLogin) );
                    break;
                }
                else
                {
                    SharedPreferenceUtils.setSetting(ctx,"employeeShiftScheduleLogin", "" );
                }
            }
//            Calendar checkOutTime2 = Calendar.getInstance();
            if (employeeShiftScheduleLogin != null && employeeShiftScheduleLogin.getLoginTime()!=null) {
                if (DateFormat.is24HourFormat(ctx)) {
                    checkOutTime2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(new StringConverter().getHour(employeeShiftScheduleLogin.getLoginTime())));
                } else {
                    checkOutTime2.set(Calendar.HOUR, Integer.parseInt(new StringConverter().getHour12(employeeShiftScheduleLogin.getLoginTime())));
                    checkOutTime2.set(Calendar.AM_PM, new StringConverter().get12HourFormat(employeeShiftScheduleLogin.getLoginTime()));
                }
                checkOutTime2.set(Calendar.MINUTE, Integer.parseInt(new StringConverter().getMinute(employeeShiftScheduleLogin.getLoginTime())));
                checkOutTime2.set(Calendar.SECOND, 10);
                checkOutTime2.set(Calendar.MILLISECOND, 0);

            }

        }
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, checkOutTime2.getTimeInMillis(), AlarmManager.INTERVAL_DAY, notifyPendingIntent);

//        long start=System.currentTimeMillis();
//        if(checkOutTime2.after(Calendar.getInstance())) {
//            int remain = 0;
//            remain = (int) start + remain * 60 * 1000;// convert it to milisecond and plus it to current time;
//
//            AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
//            Intent intent = new Intent(ctx, MyReceiver.class);
//            PendingIntent pi = PendingIntent.getBroadcast(ctx, 0, intent, 0);
//            am.set(AlarmManager.RTC_WAKEUP, remain, pi);
//        }
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, GlobalVar.jamMasuk(ctx).getTimeInMillis(), AlarmManager.INTERVAL_DAY, notifyPendingIntent);
    }

}
