package id.co.indocyber.android.starbridges.reminder.alarmManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.gson.Gson;

import id.co.indocyber.android.starbridges.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import id.co.indocyber.android.starbridges.model.EmployeeShiftSchedule.ReturnValue;
import id.co.indocyber.android.starbridges.network.StringConverter;
import id.co.indocyber.android.starbridges.reminder.notificationchannels.NotificationUtils;
import id.co.indocyber.android.starbridges.utility.SharedPreferenceUtils;

public class MyReceiver extends BroadcastReceiver {

    private void resetAlarmSetting(Context context) {
        Date dateToday = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String todayStringDate = sdf.format(dateToday);

        String dateCheckWakeUp = SharedPreferenceUtils.getSetting(context, "dateCheckWakeUp", "");
        if (dateCheckWakeUp == null || dateCheckWakeUp.equalsIgnoreCase("")) {
            SharedPreferenceUtils.setSetting(context, "dateCheckWakeUp", todayStringDate);
            SharedPreferenceUtils.setSetting(context, "counterAlarmWakeUp", "0");
        }
        else
        {
            if (!dateCheckWakeUp.equalsIgnoreCase(todayStringDate))
            {
                SharedPreferenceUtils.setSetting(context, "dateCheckWakeUp", "");
                SharedPreferenceUtils.setSetting(context, "counterAlarmWakeUp", "0");
            }
            else
            {
                SharedPreferenceUtils.setSetting(context, "dateCheckWakeUp", todayStringDate);
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar today = Calendar.getInstance();
        String employeeShiftScheduleLogin = SharedPreferenceUtils.getSetting(context,"employeeShiftScheduleLogin","");
        Gson gson=new Gson();
        resetAlarmSetting(context);
        Log.d("employeeShiftSchedule", employeeShiftScheduleLogin);
        Calendar checkOutTime2 = Calendar.getInstance();

        if(employeeShiftScheduleLogin!=null&&employeeShiftScheduleLogin!="")
        {
            ReturnValue employeeShiftScheduleToday = gson.fromJson(employeeShiftScheduleLogin, ReturnValue.class);

            if(employeeShiftScheduleToday!=null&&employeeShiftScheduleToday.getLoginTime()!=null)
            {
                if (DateFormat.is24HourFormat(context)) {
                    checkOutTime2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(new StringConverter().getHour(employeeShiftScheduleToday.getLoginTime())) );
                } else {
                    checkOutTime2.set(Calendar.HOUR, Integer.parseInt(new StringConverter().getHour12(employeeShiftScheduleToday.getLoginTime())));
                    checkOutTime2.set(Calendar.AM_PM, new StringConverter().get12HourFormat(employeeShiftScheduleToday.getLoginTime()));
                }
                checkOutTime2.set(Calendar.MINUTE, Integer.parseInt(new StringConverter().getMinute(employeeShiftScheduleToday.getLoginTime())));
                checkOutTime2.set(Calendar.SECOND, 10);
                checkOutTime2.set(Calendar.MILLISECOND, 0);
            }

            if(!employeeShiftScheduleToday.getIsWeekend()&&!employeeShiftScheduleToday.getIsHoliday()&&!employeeShiftScheduleToday.getIsLeave())
            {
                today.add(Calendar.MINUTE,-3);
                Calendar alarm =  checkOutTime2;
                Boolean hasil = today.before(alarm);
                if(hasil==true){
                    String message = context.getString(R.string.reminder_pesan_masuk);
                    String title = context.getString(R.string.reminder_title);
                    String counterAlarmWakeUp = SharedPreferenceUtils.getSetting(context, "counterAlarmWakeUp","");
                    int intCAlarmWakeup;

                    if(counterAlarmWakeUp == null || counterAlarmWakeUp.equalsIgnoreCase(""))
                    {
                        intCAlarmWakeup = 0;
                    }
                    else
                    {
                        intCAlarmWakeup=Integer.parseInt(counterAlarmWakeUp);
                    }

                    if(intCAlarmWakeup==0)
                    {
                        if(android.os.Build.VERSION.SDK_INT < 26)
                        {
                            Notification.deliverNotification(context, title, message);
                            intCAlarmWakeup++;
                        }
                        else
                        {
                            Notification.showNotification(context, title, message);
                            intCAlarmWakeup++;
                        }
                    }

                    SharedPreferenceUtils.getSetting(context, "counterAlarmWakeUp",intCAlarmWakeup+"");

                    Log.d("myTag", "notif AlarmMasuk di jalankan karena jam masih akan datang");
                }else{
                    Log.d("myTag", "notif AlarmMasuk tidak di jalankan karena jam sudah terlewat");
                }
            }
            else {
                Log.d("myTag", "notif AlarmMasuk tidak di jalankan karena bukan hari kerja");
            }
        }

        /*
        Calendar today = Calendar.getInstance();
        String employeeSchedule = SharedPreferenceUtils.getSetting(context,"employeeSchedule","");
        Gson gson=new Gson();
        Log.d("employeeSchedule", employeeSchedule);
        Calendar checkOutTime2 = Calendar.getInstance();
        if(employeeSchedule!=null||employeeSchedule!="") {
            EmployeeShiftSchedule employeeShiftSchedule = gson.fromJson(employeeSchedule, EmployeeShiftSchedule.class);
            Log.d("employeeShiftSchedule", gson.toJson(employeeShiftSchedule.getReturnValue()) );
            Date dateEmployee;
            Date dateToday=new Date();
            ReturnValue employeeShiftSchedule2=null;
            for(ReturnValue returnValue: employeeShiftSchedule.getReturnValue())
            {
                dateEmployee=new StringConverter().dateToDate(returnValue.getDate());
                SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
                if(sdf.format(dateEmployee).equals(sdf.format(dateToday)))
                {
                    employeeShiftSchedule2=returnValue;

                    break;
                }
            }
//            Calendar checkOutTime2 = Calendar.getInstance();
            if(employeeShiftSchedule2!=null)
            {
                if (DateFormat.is24HourFormat(context)) {
                    checkOutTime2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(new StringConverter().getHour(employeeShiftSchedule2.getLoginTime())) );
                } else {
                    checkOutTime2.set(Calendar.HOUR, Integer.parseInt(new StringConverter().getHour12(employeeShiftSchedule2.getLoginTime())));
                    checkOutTime2.set(Calendar.AM_PM, new StringConverter().get12HourFormat(employeeShiftSchedule2.getLoginTime()));
                }
                checkOutTime2.set(Calendar.MINUTE, Integer.parseInt(new StringConverter().getMinute(employeeShiftSchedule2.getLoginTime())));
                checkOutTime2.set(Calendar.SECOND, 10);
                checkOutTime2.set(Calendar.MILLISECOND, 0);

                if(!employeeShiftSchedule2.getIsWeekend()||!employeeShiftSchedule2.getIsHoliday())
                {
                    today.add(Calendar.MINUTE,-3);
                    Calendar alarm =  checkOutTime2;
                    Boolean hasil = today.before(alarm);
                    if(hasil==true){
                        String message = context.getString(R.string.reminder_pesan_masuk);
                        String title = context.getString(R.string.reminder_title);
                        if(android.os.Build.VERSION.SDK_INT < 26)
                            Notification.deliverNotification(context, title, message);
                        else
                        {
                            new NotificationUtils(context).showPMNotification(message, title);
//                    NotificationUtils.showPMNotification("Hey, just received new PM from @user");
                        }

                        Log.d("myTag", "notif AlarmMasuk di jalankan karena jam masih akan datang");
                    }else{
                        Log.d("myTag", "notif AlarmMasuk tidak di jalankan karena jam sudah terlewat");
                    }
                }
                else {
                    Log.d("myTag", "notif AlarmMasuk tidak di jalankan karena bukan hari kerja");
                }
            }
        }
        */

        /*
        int a = today.get(Calendar.DAY_OF_WEEK);
        if (a == 2 || a == 3 || a == 4 || a == 5 || a == 6) {
            today.add(Calendar.MINUTE,-3);
            Calendar alarm =  GlobalVar.jamMasuk(context);
            Boolean hasil = today.before(alarm);
            if(hasil==true){
                String message = context.getString(R.string.reminder_pesan_masuk);
                String title = context.getString(R.string.reminder_title);
                if(android.os.Build.VERSION.SDK_INT < 26)
                    Notification.deliverNotification(context, title, message);
                else
                {
                    new NotificationUtils(context).showPMNotification(message, title);
//                    NotificationUtils.showPMNotification("Hey, just received new PM from @user");
                }

                Log.d("myTag", "notif AlarmMasuk di jalankan karena jam masih akan datang");
            }else{
                Log.d("myTag", "notif AlarmMasuk tidak di jalankan karena jam sudah terlewat");
            }
        } else {
            Log.d("myTag", "notif AlarmMasuk tidak di jalankan karena bukan hari kerja");
        }
        */
    }
}
