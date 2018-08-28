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
import id.co.indocyber.android.starbridges.utility.GlobalVar;
import id.co.indocyber.android.starbridges.utility.SharedPreferenceUtils;

import static android.content.Context.ALARM_SERVICE;

public class AlarmManagerPulang {
    private static final int NOTIFICATION_ID2 = 1;
    private static final String ACTION_NOTIFY2 = "com.example.android.starbridges.ACTION_NOTIFY_PULANG";

    public static void start(Context ctx) {
        final AlarmManager alarmManager2 = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);

//        Calendar checkOutTime = Calendar.getInstance();
//        if (DateFormat.is24HourFormat(ctx)) {
//            checkOutTime.set(Calendar.HOUR_OF_DAY, 17);
//        } else {
//            checkOutTime.set(Calendar.HOUR, 5);
//            checkOutTime.set(Calendar.AM_PM, PM);
//        }
//        checkOutTime.set(Calendar.MINUTE, 20);
//        checkOutTime.set(Calendar.SECOND, 10);
//        checkOutTime.set(Calendar.MILLISECOND, 0);

        Intent notifyIntent2 = new Intent(ACTION_NOTIFY2);

        final PendingIntent notifyPendingIntent2 = PendingIntent.getBroadcast
                (ctx.getApplicationContext(), NOTIFICATION_ID2, notifyIntent2, PendingIntent.FLAG_UPDATE_CURRENT);

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
            ReturnValue employeeShiftScheduleLogout = null;
            for (ReturnValue returnValue : employeeShiftSchedule.getReturnValue()) {
                dateEmployee = new StringConverter().dateToDate(returnValue.getDate());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                if (sdf.format(dateEmployee).equals(sdf.format(dateToday))) {
                    employeeShiftScheduleLogout = returnValue;
                    SharedPreferenceUtils.setSetting(ctx,"employeeShiftScheduleLogout", gson.toJson(employeeShiftScheduleLogout) );
                    break;
                }
                else
                {
                    SharedPreferenceUtils.setSetting(ctx,"employeeShiftScheduleLogout", "" );
                }
            }
//            Calendar checkOutTime2 = Calendar.getInstance();
            if (employeeShiftScheduleLogout != null&& employeeShiftScheduleLogout.getLogoutTime()!=null) {
                if (DateFormat.is24HourFormat(ctx)) {
                    checkOutTime2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(new StringConverter().getHour(employeeShiftScheduleLogout.getLogoutTime())));
                } else {
                    checkOutTime2.set(Calendar.HOUR, Integer.parseInt(new StringConverter().getHour12(employeeShiftScheduleLogout.getLogoutTime())));
                    checkOutTime2.set(Calendar.AM_PM, new StringConverter().get12HourFormat(employeeShiftScheduleLogout.getLogoutTime()));
                }
                checkOutTime2.set(Calendar.MINUTE, Integer.parseInt(new StringConverter().getMinute(employeeShiftScheduleLogout.getLogoutTime())));
                checkOutTime2.set(Calendar.SECOND, 10);
                checkOutTime2.set(Calendar.MILLISECOND, 0);

            }
        }
        alarmManager2.setInexactRepeating(AlarmManager.RTC_WAKEUP, checkOutTime2.getTimeInMillis(), AlarmManager.INTERVAL_DAY, notifyPendingIntent2);
//        alarmManager2.setInexactRepeating(AlarmManager.RTC_WAKEUP, GlobalVar.jamPulang(ctx).getTimeInMillis(), AlarmManager.INTERVAL_DAY, notifyPendingIntent2);

    }

}
