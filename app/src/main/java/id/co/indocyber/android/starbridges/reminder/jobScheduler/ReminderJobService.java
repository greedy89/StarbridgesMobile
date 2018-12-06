package id.co.indocyber.android.starbridges.reminder.jobScheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import id.co.indocyber.android.starbridges.model.EmployeeShiftSchedule.EmployeeShiftSchedule;
import id.co.indocyber.android.starbridges.model.EmployeeShiftSchedule.ReturnValue;
import id.co.indocyber.android.starbridges.network.StringConverter;
import id.co.indocyber.android.starbridges.utility.SharedPreferenceUtils;

/**
 * reminderJobService to be scheduled by the JobScheduler.
 * start another service
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ReminderJobService extends JobService{

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("PercobaanJobScheduler","success");

        String employeeSchedule = SharedPreferenceUtils.getSetting(getApplicationContext(),"employeeSchedule","");
        Gson gson=new Gson();
        Log.d("employeeSchedule", employeeSchedule);
        Calendar checkOutTime2 = Calendar.getInstance();
        if(employeeSchedule!=null&&!employeeSchedule.equalsIgnoreCase("")) {
            EmployeeShiftSchedule employeeShiftSchedule = gson.fromJson(employeeSchedule, EmployeeShiftSchedule.class);
            Log.d("employeeShiftSchedule", gson.toJson(employeeShiftSchedule.getReturnValue()));
            Date dateEmployee;
            Date dateToday = new Date();
            ReturnValue employeeShiftSchedule2 = null;
            for (ReturnValue returnValue : employeeShiftSchedule.getReturnValue()) {
                dateEmployee = new StringConverter().dateToDate(returnValue.getDate());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                if (sdf.format(dateEmployee).equals(sdf.format(dateToday))) {
                    employeeShiftSchedule2 = returnValue;
                    break;
                }
            }

            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String cTime = sdf.format(currentTime);
            String message ;
            if(cTime.equalsIgnoreCase(new StringConverter().getHourMinute(employeeShiftSchedule2.getLoginTime()))){
                message="jam masuk";
                reminderNotify.deliverNotification(0,getApplicationContext(),"StartBrige",message);

            }else if(cTime.equalsIgnoreCase(new StringConverter().getHourMinute(employeeShiftSchedule2.getLogoutTime()))){
                message="jam pulang";
                reminderNotify.deliverNotification(0,getApplicationContext(),"StartBrige",message);
            }
            message="Test Berhasil";
            reminderNotify.deliverNotification(0,getApplicationContext(),"StartBrige",message);
            ScheduleUtil.scheduleJob(getApplicationContext());//reschedule job
//        Intent service = new Intent(getApplicationContext(), LoginActivity.class);
//        getApplicationContext().startService( reminderNotify.deliverNotification(getApplicationContext()));
            return true;

        }
//        Toast.makeText(this,"success",Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
