package id.co.indocyber.android.starbridges.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import id.co.indocyber.android.starbridges.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import id.co.indocyber.android.starbridges.adapter.HistoryAdapter;
import id.co.indocyber.android.starbridges.model.Attendence;
import id.co.indocyber.android.starbridges.model.OLocation.OLocation;
import id.co.indocyber.android.starbridges.model.history.History;
import id.co.indocyber.android.starbridges.model.history.ReturnValue;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.utility.AlertDialogManager;
import id.co.indocyber.android.starbridges.utility.DialogAdapter;
import id.co.indocyber.android.starbridges.utility.GlobalVar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartEndDayActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView mDateView, mTimeView;
    private Button mShowDetail;
    private RecyclerView recyclerView;
    private APIInterfaceRest apiInterface;
    private ProgressDialog progressDialog;
    private List<ReturnValue> value;
    private HistoryAdapter viewAdapter;
    private String dateString, dateString2, sPhoto;
    static final int REQUEST_ACCESS_LOCATION = 101;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_FILE_PHOTO_LOCATION = 110;

    private ReturnValue latestReturnValue;
    List<id.co.indocyber.android.starbridges.model.OLocation.ReturnValue> listReturnValueLocation = new ArrayList<>();;
    String sLocationID, sLocationName, sLocationAddress, sLatitude, sLongitude;
    private FusedLocationProviderClient client;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE=99;
    private GoogleMap mMap;
    private History data;
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT < 17) {
            setContentView(R.layout.activity_start_end_day_41);
            mTimeView = (TextView) findViewById(R.id.txt_time);
            DateFormat df = new SimpleDateFormat("HH:mm:ss");
            Date date2 = new Date();
            try {
                mTimeView.setText(df.format(date2));
            } catch (Exception e) {

            }
        }
        else
        {
            setContentView(R.layout.activity_start_end_day);
            mTimeView = (TextClock) findViewById(R.id.txt_time);

        }
        this.setTitle("Attendance");
        long date = System.currentTimeMillis();
        mDateView = (TextView) findViewById(R.id.txt_date);

        checkStoragePermission();
        enableGoogleMaps();

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
        dateString = sdf.format(date);
        dateString2 = sdf2.format(date);
        mDateView.setText(dateString);

        recyclerView = (RecyclerView) findViewById(R.id.StartDayList);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = getPackageManager().queryIntentActivities( mainIntent, 0);

        mShowDetail = (Button) findViewById(R.id.btn_show_detail);
        mShowDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                    return;
                }

                lastClickTime = SystemClock.elapsedRealtime();
                final LocationManager manager = (LocationManager) StartEndDayActivity.this.getSystemService(Context.LOCATION_SERVICE);
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(StartEndDayActivity.this)) {
//            Toast.makeText(LoginActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
                    List<ResolveInfo> pkgAppsList = getPackageManager().queryIntentActivities( mainIntent, 0);

                    if (isAppInstalled("com.lexa.fakegps")
                            || isAppInstalled("com.theappninjas.gpsjosystick")
                            ||isAppInstalled("com.incorporateapps.fakegps.fre")
                            ||isAppInstalled("com.divi.fakeGPS")
                            ||isAppInstalled("com.fakegps.mock")
                            ||isAppInstalled("com.frastan.fakegps")
                            ||isAppInstalled("com.gsmartstudio.fakegps")
                            ||isAppInstalled("com.lkr.fakelocation")
                            ||isAppInstalled("com.ltp.pro.fakelocation")
                            ||isAppInstalled("com.pe.fakegpsrun")
                            ||isAppInstalled("com.perfect.apps.fakegps.flygps.fake.location.changer.fake.gps")
                            ||isAppInstalled("com.usefullapps.fakegpslocationpro")
                            ||isAppInstalled("com.fake.gps.location")
                            ||isAppInstalled("org.hola.gpslocation")
                            ){
                        //Toast.makeText(StartEndDayActivity.this,"Terdeteksi",Toast.LENGTH_SHORT).show();
                        AlertDialogManager alertDialogManager = new AlertDialogManager();
                        alertDialogManager.showAlertDialog(StartEndDayActivity.this, "Warning","Please Uninstall your Fake GPS Apps",false);
                    }
                    else if(mShowDetail.getText().toString().matches("End Day"))
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(StartEndDayActivity.this);
                        alert.setTitle("Are you sure want to end day?");
                        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getLocation();
                            }
                        });
                        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        alert.show();


                    }
                    else
                        showDetail();
                }
                // Todo Location Already on  ... end

                if (!hasGPSDevice(StartEndDayActivity.this)) {
                    Toast.makeText(StartEndDayActivity.this, "Gps not Supported", Toast.LENGTH_SHORT).show();
                }

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(StartEndDayActivity.this)) {
                    Log.e("Starbridges", "Gps already enabled");
                    Toast.makeText(StartEndDayActivity.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
                    enableLoc();
                } else {
                    Log.e("Starbridges", "Gps already enabled");
                    //Toast.makeText(LoginActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
                }



            }
        });

        //getAttendaceLog(dateString2, dateString2);
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            setToolbar();
        }
    }

    private void setToolbar()
    {
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(getColor(R.color.colorPrimary));
    }

    private void enableGoogleMaps()
    {
        checkPermissionLocation();
        getCoordinateForGoogleMaps();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        if(data!=null)
        {
            for(ReturnValue history: data.getReturnValue())
            {
                if(history.getLatitude()!=null&&history.getLongitude()!=null)
                {
                    LatLng coordinateActivity = new LatLng(Double.parseDouble(history.getLatitude()), Double.parseDouble(history.getLongitude()));
                    String title=history.getLogType()+"\nat "+history.getLocationName()+"\non "+history.getDisplayTime();
                    mMap.addMarker(new MarkerOptions().position(coordinateActivity).title(title));
                }

            }
        }

//        LatLng sydney = new LatLng(-6, 106);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(14.4f));
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                Toast.makeText(getApplicationContext(),
//                        latLng.latitude + ", " + latLng.longitude,
//                        Toast.LENGTH_SHORT).show();
//            }
//        });



        LatLng coordinate = new LatLng( -6.176288299702181, 106.82628370821476);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 10);
        mMap.animateCamera(yourLocation);

//        if(sLongitude==null&&sLatitude==null)
//        {
//            LatLng coordinate = new LatLng( -6.176288299702181, 106.82628370821476);
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
//            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 10);
//            mMap.animateCamera(yourLocation);
//        }
//        else
//        {
//            LatLng coordinate = new LatLng(Double.parseDouble(sLatitude), Double.parseDouble(sLongitude));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
//            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 14.4f);
//            mMap.animateCamera(yourLocation);
//        }

        mMap.setMyLocationEnabled(true);
    }

    public void getCoordinateForGoogleMaps() {
        checkPermissionLocation();

        client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    sLatitude = String.valueOf(location.getLatitude());
                    sLongitude = String.valueOf(location.getLongitude());
//                    sLatitude=null;
//                    sLongitude=null;


                }

            }
        });
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(StartEndDayActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(StartEndDayActivity.this, REQUEST_LOCATION);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }

    public void getLocation() {

        progressDialog = new ProgressDialog(StartEndDayActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        id.co.indocyber.android.starbridges.model.OLocation.ReturnValue returnValue=new id.co.indocyber.android.starbridges.model.OLocation.ReturnValue();
        returnValue.setID("");
        returnValue.setAddress("");
        returnValue.setCode("");
        returnValue.setDescription("");
        returnValue.setName("");
        listReturnValueLocation.add(returnValue);

        apiInterface = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
        apiInterface.postLocation().enqueue(new Callback<OLocation>() {
            @Override
            public void onResponse(Call<OLocation> call, Response<OLocation> response) {

                if (response.isSuccessful()) {

                    listReturnValueLocation.addAll(response.body().getReturnValue());

                } else {

                    Toast.makeText(StartEndDayActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }


                progressDialog.dismiss();

                checkLatestLocation();
            }

            @Override
            public void onFailure(Call<OLocation> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(StartEndDayActivity.this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void checkPermissionLocation()
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_LOCATION);
        }
    }

    public void getCoordinate()
    {
        checkPermissionLocation();

        client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null) {
                    sLatitude = String.valueOf(location.getLatitude());
                    sLongitude = String.valueOf(location.getLongitude());
//                    sLatitude = null;
//                    sLongitude = null;
                }
                if(sLatitude==null&&sLongitude==null)
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(StartEndDayActivity.this);
                    alert.setTitle(getString(R.string.failed_to_process));
                    alert.setMessage(getString(R.string.attention_cant_get_location_attendance));
                    alert.setPositiveButton(getString(R.string.take_photo), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(StartEndDayActivity.this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                            }
                            else
                                dispatchTakePictureIntent();
                        }
                    });
                    alert.setNegativeButton(getString(R.string.setting), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });

                    alert.setNeutralButton(getString(R.string.dismiss), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alert.show();
                }
                else
                {
                    if (android.os.Build.VERSION.SDK_INT >= 18) {
                        if(location.isFromMockProvider())
                        {
                            if (location.isFromMockProvider()) {
                                DialogAdapter.showDialogTwoBtn(StartEndDayActivity.this, null,
                                        getString(R.string.disable_fake_gps), getString(R.string.out), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finishAffinity();
                                            }
                                        },
                                        "Google Maps", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Uri gmmIntentUri = Uri.parse("geo:"+location.getLatitude()+","+location.getLongitude());
                                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                                mapIntent.setPackage("com.google.android.apps.maps");
                                                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                                    startActivity(mapIntent);
                                                }
                                            }
                                        }
                                );
                            }
                        }
                        else
                        {
                            callInputAbsence();
                        }
                    }
                    else
                    {
                        callInputAbsence();
                    }
                }
            }
        });
    }

    public void callInputAbsence()
    {

        String sUsername = GlobalVar.loginName();
        String sEmployeeID = null;
        String sBussinessGroupID = null;
        String sBeaconID = null;

        long date = System.currentTimeMillis();

        String sEvent = null;
        String sNotes = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = sdf.format(date);
        String sTime = mTimeView.getText().toString();

        TimeZone timezone = TimeZone.getDefault();
        int timeZoneOffset = timezone.getRawOffset()/(60 * 60 * 1000);


        if(sLocationID==null)
        {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try{
                addresses = geocoder.getFromLocation(Double.parseDouble(sLatitude),Double.parseDouble(sLongitude) , 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                sLocationAddress = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            }catch (Exception e)
            {

            }
        }

        final ProgressDialog progressDialog2 = new ProgressDialog(StartEndDayActivity.this);
        progressDialog2.setTitle("Loading");
        progressDialog2.setCancelable(false);
        progressDialog2.show();

        Call<Attendence> call3 = apiInterface.inputAbsence(sUsername, sEmployeeID, sBussinessGroupID, dateString, sTime, sBeaconID, sLocationID, sLocationName, sLocationAddress, sLongitude, sLatitude, "End Day", sPhoto, sNotes, sEvent, timeZoneOffset);
        call3.enqueue(new Callback<Attendence>() {
            @Override
            public void onResponse(Call<Attendence> call, Response<Attendence> response) {
                Attendence data = response.body();
                progressDialog2.dismiss();

                if (data != null && data.getIsSucceed()) {
                    Toast.makeText(StartEndDayActivity.this, "Data Submitted", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(getIntent());
                } else if(data != null && data.getMessage() != null){
                    Toast.makeText(StartEndDayActivity.this, data.getMessage(), Toast.LENGTH_LONG).show();

                }else {
                    try {
                        //JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(StartEndDayActivity.this, "Failed to Submit", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(StartEndDayActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Attendence> call, Throwable t) {
                progressDialog2.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void checkLatestLocation()
    {
        for(id.co.indocyber.android.starbridges.model.OLocation.ReturnValue location:listReturnValueLocation)
        {
            if(location.getName().equals(latestReturnValue.getLocationName()+""))
            {
                sLocationID=location.getID();
            }
        }
        getCoordinate();
    }

    public boolean isAppInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAttendaceLog(dateString2, dateString2);
    }

    public void showDetail() {
        String dateValue;
        String timeValue="";
        String logType;

        if (android.os.Build.VERSION.SDK_INT < 17) {
            DateFormat df = new SimpleDateFormat("hh:mm:ss");
            Date date = new Date();
            try {
                timeValue = df.format(date);
            } catch (Exception e) {

            }
//            timeValue
        }
        else
            timeValue = mTimeView.getText().toString();
        dateValue = mDateView.getText().toString();
        logType = mShowDetail.getText().toString();

        Intent intent = new Intent(this, StartEndDayDetailActivity.class);

        intent.putExtra("time", timeValue);
        intent.putExtra("date", dateValue);
        intent.putExtra("logType", logType);
        startActivity(intent);
    }

    public void getAttendaceLog(String DateFrom, String DateTo) {
        apiInterface = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
        if(progressDialog==null)
        {
            progressDialog = new ProgressDialog(StartEndDayActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
        // khusus logType di hardcode -> LogType -> Start Day
        Call<History> call3 = apiInterface.getHistory(DateFrom, DateTo);
        call3.enqueue(new Callback<History>() {
            @Override
            public void onResponse(Call<History> call, Response<History> response) {
                progressDialog.dismiss();
                data = response.body();
                if (data != null && data.getIsSucceed()) {
                    if(data.getReturnValue().size()>0)
                    {
                        latestReturnValue=data.getReturnValue().get(0);
                        sLocationName=latestReturnValue.getLocationName().toString();
                        sLocationAddress=latestReturnValue.getLocationAddress();
                    }

                    for(ReturnValue x: data.getReturnValue())
                    {
                        if(x.getLogType().equals("End Day"))
                        {
                            mShowDetail.setEnabled(false);
                            mShowDetail.setBackground(ContextCompat.getDrawable(StartEndDayActivity.this, R.drawable.rounded_btn_disabled));
                        }else if (x.getLogType().equals("Start Day")){
                            mShowDetail.setText("End Day");

                            //mShowDetail.setEnabled(false);
                        }
                    }

//                    String lastLogType="";
//                    if(data.getReturnValue().size()>0)
//                    {
////                        int dataSize=data.getReturnValue().size()-1;
//                        lastLogType=data.getReturnValue().get(0).getLogType();
//                    }
//                    if (lastLogType.equals("Start Day7") ) {
//                        mShowDetail.setName("End Day");
//                    } else if (lastLogType.equals("End Day")) {
//                        mShowDetail.setEnabled(false);
//                    } else {
//                        mShowDetail.setName("Start Day");
//                    }
                    viewAdapter = new HistoryAdapter(StartEndDayActivity.this, data.getReturnValue());
                    recyclerView.setAdapter(viewAdapter);
                } else {
                    try {
                        //JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(StartEndDayActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(StartEndDayActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                enableGoogleMaps();
            }

            @Override
            public void onFailure(Call<History> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
                }
                break;
            case MY_CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                    //dispatchTakePictureIntent();

                } else {

                    Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /*
    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //mImageView.setImageBitmap(imageBitmap);
            //final Uri imageUri = data.getData();
            //final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            //final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            sPhoto = encodeImage(imageBitmap);
            callInputAbsence();
        }
    }
    */

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
//        String timeStamp = new SimpleDateFormat("starbridges").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
        String imageFileName = "forStarBridges";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image =new File(storageDir, imageFileName + ".jpg");
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1 ||
                android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            // only for gingerbread and newer versions
            Intent cameraEvent = new Intent(StartEndDayActivity.this, CameraActivity.class);
            startActivityForResult(cameraEvent, REQUEST_FILE_PHOTO_LOCATION);
        }
        else if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //mImageView.setImageBitmap(imageBitmap);
            //final Uri imageUri = data.getData();
            //final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            //final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            File imgFile = new  File(mCurrentPhotoPath);
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            sPhoto = encodeImage(myBitmap);
            callInputAbsence();
        }
        else if(requestCode == REQUEST_FILE_PHOTO_LOCATION && resultCode == RESULT_OK)
        {
            File imgFile = new  File(data.getStringExtra("filePath"));
            Bitmap imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            sPhoto = encodeImage(imageBitmap);
            callInputAbsence();
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            Toast.makeText(this, "Storage permission granted", Toast.LENGTH_LONG).show();
        }
    }
}
