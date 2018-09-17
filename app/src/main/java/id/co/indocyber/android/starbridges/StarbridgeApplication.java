package id.co.indocyber.android.starbridges;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.View;

import com.estimote.coresdk.common.config.Flags;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseHelperListener;
import com.raizlabs.android.dbflow.structure.database.OpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import id.co.indocyber.android.starbridges.activity.CheckInOutActivity;
import id.co.indocyber.android.starbridges.model.BeaconData.ReturnValue_Table;
import id.co.indocyber.android.starbridges.reminder.alarmManager.AlarmGetGeoLocation;
import id.co.indocyber.android.starbridges.reminder.alarmManager.Notification;
import id.co.indocyber.android.starbridges.reminder.notificationchannels.NotificationUtils;
import id.co.indocyber.android.starbridges.service.StarbridgeService;
import id.co.indocyber.android.starbridges.utility.SQLCipherHelperImpl;
import id.co.indocyber.android.starbridges.utility.SharedPreferenceUtils;

@Database(name = StarbridgeApplication.DATABASE_NAME, version = StarbridgeApplication.DATABASE_VERSION)
public class StarbridgeApplication extends Application {

    public static final String DATABASE_NAME = "starbridgeDb";
    public static final int DATABASE_VERSION = 1;

    public static BeaconManager beaconManager;
    public static BeaconRegion region;

    private <T> DatabaseConfig getConfig(Class<T> databaseClazz) {
        return new DatabaseConfig.Builder(databaseClazz)
                .openHelper(new DatabaseConfig.OpenHelperCreator() {
                    @Override
                    public OpenHelper createHelper(DatabaseDefinition databaseDefinition, DatabaseHelperListener helperListener) {
                        return new SQLCipherHelperImpl(databaseDefinition, helperListener);
                    }
                }).build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (android.os.Build.VERSION.SDK_INT < 21) {
            MultiDex.install(this);
            startService(new Intent(this, StarbridgeService.class));
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, StarbridgeService.class));
        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseConfig(getConfig(StarbridgeApplication.class))
                .openDatabasesOnInit(true)
                .build());


////        final StartBeaconScanner task = new StartBeaconScanner(getApplicationContext());
////        task.execute();
//
//        Flags.DISABLE_BATCH_SCANNING.set(true);
//        Flags.DISABLE_HARDWARE_FILTERING.set(true);
//
//        beaconManager = new BeaconManager(StarbridgeApplication.this);
//        region = new BeaconRegion("rangedregion3", null, null, null);
//        // add this below:
//
//        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
//            @Override
//            public void onServiceReady() {
//                beaconManager.startRanging(region);
//            }
//        });
//
//        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
//            @Override
//            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<com.estimote.coresdk.recognition.packets.Beacon> beacons) {
//                if (!beacons.isEmpty()) {
////                            com.estimote.coresdk.recognition.packets.Beacon nearestBeacon = beacons.get(0);
////                            List<String> places = placesNearBeacon(nearestBeacon);
////                            // TODO: update the UI here
////                            Log.d("Airport", "Nearest places: " + places);
//                    List<String> beaconsInfo = getValueBeacon(beacons);
//                    Log.d("beaconInfo", beaconsInfo + "");
//                }
//            }
//        });
//
//
////        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
////            @Override
////            public void onEnteredRegion(BeaconRegion beaconRegion, List<com.estimote.coresdk.recognition.packets.Beacon> beacons) {
////                if (!beacons.isEmpty()) {
//////                            com.estimote.coresdk.recognition.packets.Beacon nearestBeacon = beacons.get(0);
//////                            List<String> places = placesNearBeacon(nearestBeacon);
//////                            // TODO: update the UI here
//////                            Log.d("Airport", "Nearest places: " + places);
////                    List<String> beaconsInfo = getValueBeacon(beacons);
////                    Log.d("beaconInfo", beaconsInfo+"");
////                }
////                else
////                    Log.d("beaconInfo", "no beacon found");
////            }
////
////            @Override
////            public void onExitedRegion(BeaconRegion beaconRegion) {
////                Log.d("beaconInfo", "no beacon found");
////            }
////        });



        //from check in out
//        final ProgressDialog progressDialog2 = new ProgressDialog(StarbridgeApplication.this);
//        progressDialog2.setTitle("Loading");
//        progressDialog2.setCancelable(false);
//        progressDialog2.show();

        beaconManager = new BeaconManager(this);

        String beaconScanner= SharedPreferenceUtils.getSetting(getApplicationContext(), "beaconScanner","");

        Flags.DISABLE_BATCH_SCANNING.set(true);
        Flags.DISABLE_HARDWARE_FILTERING.set(true);



        region = new BeaconRegion("rangedregion4",null, null, null);
        // add this below:

        beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(5), 0);
        beaconManager.setForegroundScanPeriod(TimeUnit.SECONDS.toMillis(5), 0);

        if(beaconScanner.equals("1"))
        {
            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    beaconManager.startRanging(region);
                }
            });
        }
        else
        {
            stopScanningBeacon();
        }



        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<com.estimote.coresdk.recognition.packets.Beacon> beacons) {
            if (!beacons.isEmpty()) {
//                            com.estimote.coresdk.recognition.packets.Beacon nearestBeacon = beacons.get(0);
//                            List<String> places = placesNearBeacon(nearestBeacon);
//                            // TODO: update the UI here
//                            Log.d("Airport", "Nearest places: " + places);
                List<String> beaconsInfo = getValueBeacon(beacons);


                Log.d("beaconInfo", beaconsInfo+"");

                id.co.indocyber.android.starbridges.model.BeaconData.ReturnValue beaconFinded=new id.co.indocyber.android.starbridges.model.BeaconData.ReturnValue();

                for(com.estimote.coresdk.recognition.packets.Beacon beacon: beacons)
                {
                    beaconFinded= SQLite.select().from(id.co.indocyber.android.starbridges.model.BeaconData.ReturnValue.class)
                            .where(ReturnValue_Table.uUID.is(beacon.getProximityUUID()+"")).querySingle();
                }

                Log.d("beaconInfoFinded", beaconFinded.getLocationAddress()+"");
                Gson gson=new Gson();
                String beaconFindedString=gson.toJson(beaconFinded);
                if(android.os.Build.VERSION.SDK_INT < 26)
                    Notification.showBeaconNotification(getApplicationContext(), "Starbridges", "Look like you're in attendance area, tap to see action", beaconFindedString);
                else
                {
                    new NotificationUtils(getApplicationContext()).showBeaconNotification("Starbridges", "Look like you're in attendance area, tap to see action", beaconFindedString);
                }


//                    stopScanningBeacon();
            }
            }
        });
    }

    public static void startScanningBeacon()
    {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    public static void stopScanningBeacon()
    {
        try{
            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    beaconManager.stopRanging(region);
                }
            });
        } finally {

        }
    }

    private List<String> getValueBeacon(List<com.estimote.coresdk.recognition.packets.Beacon> beacons) {
        List<String> returnBeacon = new ArrayList<>();
        for (com.estimote.coresdk.recognition.packets.Beacon beacon : beacons) {
            returnBeacon.add(beacon.getProximityUUID() + " " + beacon.getMajor() + " " + beacon.getMinor() + " ");
        }
        return returnBeacon;
    }

}

//class StartBeaconScanner extends AsyncTask<Void, Void, Void>
//{
//    BeaconManager beaconManager;
//    private BeaconRegion region;
//
//    private final Context mContext;
//
//    public StartBeaconScanner(final Context context)
//    {
//        mContext=context;
//    }
//
//    @Override
//    protected Void doInBackground(Void... voids) {
//        Flags.DISABLE_BATCH_SCANNING.set(true);
//        Flags.DISABLE_HARDWARE_FILTERING.set(true);
//
//        beaconManager = new BeaconManager(mContext);
//        region = new BeaconRegion("rangedregion3", null, null, null);
//        // add this below:
//
//        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
//            @Override
//            public void onServiceReady() {
//                beaconManager.startRanging(region);
//            }
//        });
//
//        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
//            @Override
//            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<com.estimote.coresdk.recognition.packets.Beacon> beacons) {
//                if (!beacons.isEmpty()) {
////                            com.estimote.coresdk.recognition.packets.Beacon nearestBeacon = beacons.get(0);
////                            List<String> places = placesNearBeacon(nearestBeacon);
////                            // TODO: update the UI here
////                            Log.d("Airport", "Nearest places: " + places);
//                    List<String> beaconsInfo = getValueBeacon(beacons);
//                    Log.d("beaconInfo", beaconsInfo + "");
//                }
//            }
//        });
//
//
////        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
////            @Override
////            public void onEnteredRegion(BeaconRegion beaconRegion, List<com.estimote.coresdk.recognition.packets.Beacon> beacons) {
////                if (!beacons.isEmpty()) {
//////                            com.estimote.coresdk.recognition.packets.Beacon nearestBeacon = beacons.get(0);
//////                            List<String> places = placesNearBeacon(nearestBeacon);
//////                            // TODO: update the UI here
//////                            Log.d("Airport", "Nearest places: " + places);
////                    List<String> beaconsInfo = getValueBeacon(beacons);
////                    Log.d("beaconInfo", beaconsInfo+"");
////                }
////                else
////                    Log.d("beaconInfo", "no beacon found");
////            }
////
////            @Override
////            public void onExitedRegion(BeaconRegion beaconRegion) {
////                Log.d("beaconInfo", "no beacon found");
////            }
////        });
//
//        return null;
//    }
//
//    private List<String> getValueBeacon(List<com.estimote.coresdk.recognition.packets.Beacon> beacons) {
//        List<String> returnBeacon = new ArrayList<>();
//        for (com.estimote.coresdk.recognition.packets.Beacon beacon : beacons) {
//            returnBeacon.add(beacon.getProximityUUID() + " " + beacon.getMajor() + " " + beacon.getMinor() + " ");
//        }
//        return returnBeacon;
//    }
//}
