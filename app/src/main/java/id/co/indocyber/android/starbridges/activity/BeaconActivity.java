package id.co.indocyber.android.starbridges.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.HashMap;
import java.util.List;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.StarbridgeApplication;
import id.co.indocyber.android.starbridges.model.BeaconData.BeaconData;
import id.co.indocyber.android.starbridges.model.BeaconData.ReturnValue;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.utility.GlobalVar;
import id.co.indocyber.android.starbridges.utility.SessionManagement;
import id.co.indocyber.android.starbridges.utility.SharedPreferenceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BeaconActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private APIInterfaceRest apiInterface;
    private Switch swtBeacon;
    private View parentView;
    private static final int REQUEST_ACCESS_LOCATION = 101;
    private static final int REQUEST_ENABLE_BLUETOOTH=103;
    private SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        swtBeacon=(Switch)findViewById(R.id.swtBeacon);
        parentView=(View)findViewById(android.R.id.content);


//        SharedPreferenceUtils.setSetting(getApplicationContext(),"employeeSchedule", gson.toJson(response.body()) );

//        swtBeacon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Toast.makeText(BeaconActivity.this, "change", Toast.LENGTH_SHORT).show();
////                Snackbar.make(parentView, "chanegd", Snackbar.LENGTH_SHORT).show();
//
//            }
//        });

        String beaconScanner= SharedPreferenceUtils.getSetting(getApplicationContext(), "beaconScanner","");
        if(beaconScanner.equals("1"))
        {
            swtBeacon.setChecked(true);
            if(swtBeacon.isChecked())
            {
                Toast.makeText(BeaconActivity.this, "Automatic Beacon Scanner On", Toast.LENGTH_SHORT).show();
                StarbridgeApplication.startScanningBeacon();
                SharedPreferenceUtils.setSetting(getApplicationContext(),"beaconScanner", "1" );
            }
            else
            {
                Toast.makeText(BeaconActivity.this, "Automatic Beacon Scanner Off", Toast.LENGTH_SHORT).show();
                StarbridgeApplication.stopScanningBeacon();
                SharedPreferenceUtils.setSetting(getApplicationContext(),"beaconScanner", "" );
            }
        }


        swtBeacon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(swtBeacon.isChecked())
                {
                    checkPermissionLocation();
                    checkActivateBluetooth();
                    StarbridgeApplication.startScanningBeacon();
                    SharedPreferenceUtils.setSetting(getApplicationContext(),"beaconScanner", "1" );
                    Toast.makeText(BeaconActivity.this, "Automatic Beacon Scanner On", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    StarbridgeApplication.stopScanningBeacon();
                    SharedPreferenceUtils.setSetting(getApplicationContext(),"beaconScanner", "" );
                    Toast.makeText(BeaconActivity.this, "Automatic Beacon Scanner Off", Toast.LENGTH_SHORT).show();
                }

            }
        });
//            SharedPreferenceUtils.setSetting(getApplicationContext(),"beaconScanner", "0" );



        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String locationId=user.get(SessionManagement.KEY_LOCATION_ID);
        SharedPreferenceUtils.setSetting(getApplicationContext(),"locationId", locationId );

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void checkActivateBluetooth()
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "device does not support bluetooth", Toast.LENGTH_LONG).show();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
//            return false;
        }
//        return true;

    }

    private void checkPermissionLocation()
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_LOCATION);
//            return false;
        }
//        return true;

    }

    public void syncBeacon(View view){
        getBeaconData();
    }

    private void getBeaconData()
    {
        if(progressDialog==null||!progressDialog.isShowing())
        {
            progressDialog = new ProgressDialog(BeaconActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        apiInterface = APIClient.getLocationValue(GlobalVar.getToken()).create(APIInterfaceRest.class);
        apiInterface.getBeaconData().enqueue(new Callback<BeaconData>() {
            @Override
            public void onResponse(Call<BeaconData> call, Response<BeaconData> response) {

                if (response.isSuccessful()) {

                    syncronBeaconData(response.body().getReturnValue());

                } else {

                    Toast.makeText(BeaconActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<BeaconData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(BeaconActivity.this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void syncronBeaconData(List<ReturnValue> beaconDatas)
    {
        try {
//            SQLite.delete().from(id.co.indocyber.android.starbridges.model.BeaconData.ReturnValue.class).execute();
            for(id.co.indocyber.android.starbridges.model.BeaconData.ReturnValue returnValue:beaconDatas)
            {
                returnValue.save();
            }

//            File databaseFile = getDatabasePath(StarbridgeApplication.DATABASE_NAME+".db");
//            databaseFile.mkdirs();
//            databaseFile.delete();
//            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "test123", null);
//            database.execSQL("create table t1(a, b)");
//            database.execSQL("insert into t1(a, b) values(?, ?)", new Object[]{"one for the money",
//                    "two for the show"});

            Toast.makeText(BeaconActivity.this, "Success sync data", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(BeaconActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}
