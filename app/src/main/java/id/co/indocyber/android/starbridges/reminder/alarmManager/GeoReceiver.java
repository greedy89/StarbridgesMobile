package id.co.indocyber.android.starbridges.reminder.alarmManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import id.co.indocyber.android.starbridges.reminder.utility.SharedPreferenceUtils;

public class GeoReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        getLocation(context);
    }

    private FusedLocationProviderClient client;
    String sLatitude;
    String sLongitude;


    @SuppressLint("MissingPermission")
    public void getLocation(final Context context) {
//        Toast.makeText(context, "run gps location", Toast.LENGTH_LONG).show();
        client = LocationServices.getFusedLocationProviderClient(context);

        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    sLatitude = String.valueOf(location.getLatitude());
                    sLongitude = String.valueOf(location.getLongitude());
                    SharedPreferenceUtils.setSetting(context,"latitude", sLatitude);
                    SharedPreferenceUtils.setSetting(context,"longitude", sLongitude);
                }
//                Toast.makeText(context, sLatitude + "," + sLongitude, Toast.LENGTH_LONG).show();
//                Log.d("locationGPS", sLatitude + " " + sLongitude);
            }
        });
    }
}