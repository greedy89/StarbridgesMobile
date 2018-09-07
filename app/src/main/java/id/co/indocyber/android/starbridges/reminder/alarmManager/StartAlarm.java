package id.co.indocyber.android.starbridges.reminder.alarmManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmGetGeoLocation.start(context);
    }
}
