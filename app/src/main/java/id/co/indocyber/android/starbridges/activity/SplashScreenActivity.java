package id.co.indocyber.android.starbridges.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.model.versioning.Versioning;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.reminder.alarmManager.AlarmManagerMasuk;
import id.co.indocyber.android.starbridges.reminder.alarmManager.AlarmManagerPulang;
import id.co.indocyber.android.starbridges.utility.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity{
    private boolean versionEqual;
    private SessionManagement session;
    private int resultVersionCode;
    private int finalVersionCodeI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
        setAlarmMasukPulang(getApplicationContext());
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                session = new SessionManagement(getApplicationContext());
                checkAppVersion();
            }
        }, 900);

    }
    private void checkAppVersion() {
        String versionName = "";
        String versionCode = "";
        int versionCodeI = 0;
//        Boolean versionEqual = false;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = String.valueOf(packageInfo.versionCode);
            versionCodeI =packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        final APIInterfaceRest apiInterface = APIClient.getClient().create(APIInterfaceRest.class);
        final Call<Versioning> call3 = apiInterface.checkAppVerion();
        final String finalVersionCode = versionCode;
        final String finalVersionName = versionName;
        if(finalVersionCodeI==0)
            finalVersionCodeI=versionCodeI;
        call3.enqueue(new Callback<Versioning>() {
            @Override
            public void onResponse(Call<Versioning> call, Response<Versioning> response) {
//                Versioning data = response.body();
                if(response.isSuccessful())
                {
                    resultVersionCode=Integer.parseInt(response.body().getReturnValue().getVersionCode());

                    String versionCodeAPi = response.body().getReturnValue().getVersionCode().toString();
                    String versionNameApi = response.body().getReturnValue().getVersionName().toString();
//                    if (versionCodeAPi.equalsIgnoreCase(String.valueOf(finalVersionCode)) &&
//                            versionNameApi.equalsIgnoreCase(finalVersionName)) {
                    if (finalVersionCodeI >= Integer.parseInt(response.body().getReturnValue().getVersionCode())  ) {
                        versionEqual = true;
                    } else {
                        versionEqual = false;
                    }

                    if (versionEqual != false) {
                        if (session.isLoggedIn()) {
                            startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        }else{
                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        }
                    } else {
                        alertNotif(getString(R.string.update_available), getString(R.string.notify_update));
                    }
                }
                else
                {
                    //handle error api
                    Toast.makeText(getApplicationContext(), "Make sure you are connected to the Internet and try again", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Versioning> call, Throwable t) {
                //handle error connection
//                Toast.makeText(getApplicationContext(), "Something went wrong...Please try again!", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Make sure you are connected to the Internet and try again by click logo", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    private void alertNotif(String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton(getString(R.string.update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri starBridges = Uri.parse("https://play.google.com/store/apps/details?id=id.co.indocyber.android.starbridges");
                Intent intent = new Intent(Intent.ACTION_VIEW, starBridges);
                startActivity(intent);
            }
        });
        alert.setNegativeButton(getString(R.string.dismiss), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finalVersionCodeI=resultVersionCode+1;
            }
        });
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                finish();
                finalVersionCodeI=resultVersionCode+1;
            }
        });
        alert.show();
    }

    public void imgAction(View view) {
        checkAppVersion();
    }

    private void setAlarmMasukPulang(Context context) {
        boolean isAlarmMasuk = (PendingIntent.getBroadcast(context, 0,
                new Intent("id.co.indocyber.android.starbridges.ACTION_NOTIFY_MASUK"),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (isAlarmMasuk) {
            Log.d("myTag", "Alarm Masuk is already active");
//            Toast.makeText(context, "Alarm Masuk is already active", Toast.LENGTH_SHORT).show();
        } else {
            AlarmManagerMasuk.start(context);
            Log.d("myTag", "AlarmMasuk is Created");
//            Toast.makeText(context, "AlarmMasuk is Created", Toast.LENGTH_SHORT).show();
        }

        boolean isAlarmKeluar = (PendingIntent.getBroadcast(context, 1,
                new Intent("id.co.indocyber.android.starbridges.ACTION_NOTIFY_PULANG"),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (isAlarmKeluar) {
            Log.d("myTag", "AlarmPulang is already active");
//            Toast.makeText(context, "Alarm Pulang is already active", Toast.LENGTH_SHORT).show();
        } else {
            AlarmManagerPulang.start(context);
            Log.d("myTag", "AlarmPulang is Created");
//            Toast.makeText(context, "AlarmPulang is Created", Toast.LENGTH_SHORT).show();
        }

    }
}
