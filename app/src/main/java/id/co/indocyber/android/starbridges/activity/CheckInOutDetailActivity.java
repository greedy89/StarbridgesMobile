package id.co.indocyber.android.starbridges.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;

import id.co.indocyber.android.starbridges.R;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import id.co.indocyber.android.starbridges.model.Attendence;
import id.co.indocyber.android.starbridges.model.OLocation.OLocation;
import id.co.indocyber.android.starbridges.model.OLocation.ReturnValue;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.utility.GlobalVar;
import id.co.indocyber.android.starbridges.utility.GpsLocationTracker;
import id.co.indocyber.android.starbridges.utility.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInOutDetailActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_ACCESS_LOCATION = 101;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private FusedLocationProviderClient client;
    private LocationManager locationManager;

    private EditText mEventView, mDateView, mTimeView, mLocationNameView, mNotesView;
    private Button mSubmit;
    private String sLocationID, sUsername, sLongitude, sLatitude, sDate, sTime, sLogType, sPhoto;
    private APIInterfaceRest apiInterface;
    private ProgressDialog progressDialog;
    private boolean checkStartDay;
    SessionManagement session;
    int timeZoneOffset;
    String sLocationName;
    String sLocationAddress;
    String attendancePrivilege;
    List<ReturnValue> LocItems;
    final List<ReturnValue> listReturnValue = new ArrayList<>();
    Spinner spnSearchLocation;
    GoogleApiClient mGoogleApiClient;
    Location myCurrentLocation;
    LocationRequest locationRequest;

    @Override
    public void onLocationChanged(Location location) {
        myCurrentLocation= LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_out_detail);

        setTitle("ATTENDANCE");
        mEventView = (EditText) findViewById(R.id.txt_event);
        mDateView = (EditText) findViewById(R.id.txt_date);
        mTimeView = (EditText) findViewById(R.id.txt_time);
        mLocationNameView = (EditText) findViewById(R.id.txt_location_name);
        mNotesView = (EditText) findViewById(R.id.txt_notes);
        spnSearchLocation = (Spinner) findViewById(R.id.spnSearchLocation);

        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String token_sp = user.get(SessionManagement.KEY_TOKEN);
        String loginName = user.get(SessionManagement.KEY_LOGINNAME);
        String employeeId = user.get(SessionManagement.KEY_EMPLOYEE_ID);
        attendancePrivilege = user.get(SessionManagement.KEY_ATTENDANCE_PRIVILEGE);

        GlobalVar.setToken(token_sp);
        GlobalVar.setLoginName(loginName);
        GlobalVar.setEmployeeId(employeeId);


        mGoogleApiClient =new GoogleApiClient.Builder(CheckInOutDetailActivity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

//                new GoogleApiClient
//                .Builder(CheckInOutDetailActivity.this)
//                .enableAutoManage(CheckInOutDetailActivity.this, 34992, this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();

        mSubmit = (Button) findViewById(R.id.btn_submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitData();
            }
        });


        Intent intent = getIntent();
        sDate = intent.getStringExtra("date");
        sTime = intent.getStringExtra("time");
        sUsername = GlobalVar.loginName();
        sLogType = intent.getStringExtra("logType");
        checkStartDay = intent.getBooleanExtra("checkStartDay", false);
        TimeZone timezone = TimeZone.getDefault();
        timeZoneOffset = timezone.getRawOffset() / (60 * 60 * 1000);


        mDateView.setText(sDate);
        mTimeView.setText(sTime);
        initSpinnerLoc();

        spnSearchLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    final ReturnValue returnValue1 = (ReturnValue) spnSearchLocation.getItemAtPosition(i);
                    //Log.d("LocationIdnya", returnValue1.getID());
                    sLocationID = returnValue1.getID();
                    sLocationName = returnValue1.getName();
                    sLocationAddress = returnValue1.getAddress();
                }

                setEnableSpinnerAndEditTextLocation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setEnableSpinnerAndEditTextLocation();
            }
        });


        mLocationNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setEnableSpinnerAndEditTextLocation();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setEnableSpinnerAndEditTextLocation();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setEnableSpinnerAndEditTextLocation();
            }
        });
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        getLocation();

//        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
//        }

    }

//    public void getInternetTime() throws IOException {
//        String timeServer = "time-a.nist.gov";
//
//        NTPUDPClient timeClient = new NTPUDPClient();
//        timeClient.open();
//        timeClient.setSoTimeout(5000);
//        InetAddress inetAddress = InetAddress.getByName(timeServer);
//        TimeInfo timeInfo = timeClient.getTime(inetAddress);
//        long returnTime = timeInfo.getReturnTime();
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis(returnTime);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_ACCESS_LOCATION:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //getLocation();
//                } else {
//                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
//                }
//
//
//            case MY_CAMERA_REQUEST_CODE:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
//
//                } else {
//
//                    Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
//                }
//                break;
//        }

        if (requestCode == REQUEST_ACCESS_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "GPS granted", Toast.LENGTH_SHORT).show();
                //getLocation();
            } else {
                Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                //dispatchTakePictureIntent();

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }

    }


    public void getLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_LOCATION);
        }
//        client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    sLatitude=String.valueOf(location.getLatitude());
//                    sLongitude=String.valueOf(location.getLongitude());
//
////                    if (sLogType.equals("Check In")) {
////                        dispatchTakePictureIntent();
////                    } else {
////                        sPhoto=null;
////                        callInputAbsence();
////                    }
//                }
//            }
//        });

    }

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

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public void SubmitData() {

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_LOCATION);
        }

        if (mLocationNameView.isEnabled()) {
            if (mLocationNameView.getText().toString().matches("")) {
                mLocationNameView.setError("Please fill the location");
            } else {
                capturePhoto();
            }
            sLocationID = null;
            sLocationName = mLocationNameView.getText().toString();
            sLocationAddress = null;
        } else {
            capturePhoto();
        }
    }

    /**
     * Prompt user to enable GPS and Location Services
     * @param mGoogleApiClient
     * @param activity
     */
    public void locationChecker(GoogleApiClient mGoogleApiClient, final Activity activity) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(50 * 1000);
        locationRequest.setFastestInterval(10 * 1000);


//        if(myCurrentLocation==null)
//            PendingResult<LocationSettingsResult> result2=LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, (android.location.LocationListener)this);
        myCurrentLocation= LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        final PendingResult<Status> result2 =LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,locationRequest,(LocationListener) this);
        result2.setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                        Location location = new Location(LocationManager.NETWORK_PROVIDER);

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    activity, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                        Location location = new Location(LocationManager.NETWORK_PROVIDER);

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    activity, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    public void capturePhoto()
    {
        client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    sLatitude = String.valueOf(location.getLatitude());
                    sLongitude = String.valueOf(location.getLongitude());

                    if (sLogType.equals("Check In")) {
                        dispatchTakePictureIntent();
                    } else {
                        sPhoto=null;
                        callInputAbsence();
                    }

                }
                else
                {

//                    GpsLocationTracker mGpsLocationTracker = new GpsLocationTracker(CheckInOutDetailActivity.this);
//
//                    /**
//                     * Set GPS Location fetched address
//                     */
//                    if (mGpsLocationTracker.canGetLocation())
//                    {
//                        sLatitude = mGpsLocationTracker.getLatitude()+"";
//                        sLongitude = mGpsLocationTracker.getLongitude()+"";
//
//
//                    }
//                    else
//                    {
//                        mGpsLocationTracker.showSettingsAlert();
//                    }

//                    locationChecker(mGoogleApiClient, CheckInOutDetailActivity.this);


                    if(sLatitude==null&&sLongitude==null)
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(CheckInOutDetailActivity.this);
                        alert.setTitle(getString(R.string.failed_to_process));
                        alert.setMessage(getString(R.string.attention_cant_get_location));
                        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alert.setNegativeButton(getString(R.string.setting), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        });

                        alert.show();
                    }
                    else
                    {
                        callInputAbsence();
                    }

                }
            }
        });
    }

    public void initSpinnerLoc() {

        progressDialog = new ProgressDialog(CheckInOutDetailActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();

        ReturnValue returnValue=new ReturnValue();
        returnValue.setID("");
        returnValue.setAddress("");
        returnValue.setCode("");
        returnValue.setDescription("");
        returnValue.setName("--other--");
        listReturnValue.add(returnValue);

        apiInterface = APIClient.getLocationValue(GlobalVar.getToken()).create(APIInterfaceRest.class);
        apiInterface.postLocation().enqueue(new Callback<OLocation>() {
            @Override
            public void onResponse(Call<OLocation> call, Response<OLocation> response) {

                if (response.isSuccessful()) {

                    LocItems = response.body().getReturnValue();
                    if (LocItems!= null){
                        listReturnValue.addAll(LocItems);
                    } else{
                        Toast.makeText(CheckInOutDetailActivity.this, "spinner Tidak dapat data",Toast.LENGTH_LONG).show();
                    }

                    ArrayAdapter<ReturnValue> adapter = new ArrayAdapter<ReturnValue>(CheckInOutDetailActivity.this,
                            android.R.layout.simple_spinner_item, listReturnValue);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnSearchLocation.setAdapter(adapter);
                } else {

                    Toast.makeText(CheckInOutDetailActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }

                HashMap<String, String> user = session.getUserDetails();
                String location=user.get(SessionManagement.KEY_LOCATION);
                String locationId=user.get(SessionManagement.KEY_LOCATION_ID);

                if(locationId!="" || locationId != null)
                {
                    setupSpinner(locationId);
                }
                else mLocationNameView.setText(location);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<OLocation> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CheckInOutDetailActivity.this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void setupSpinner(String locationId)
    {
        int counter=0;
        for(ReturnValue decisionNumber:listReturnValue)
        {
            if(locationId.equals(decisionNumber.getID())) break;
            counter++;
        }
        if(counter >= LocItems.size())
            counter=0;

        spnSearchLocation.setSelection(counter);
        progressDialog.dismiss();
    }

    public void callInputAbsence() {

        apiInterface = APIClient.inputAbsence(GlobalVar.getToken()).create(APIInterfaceRest.class);


        long date = System.currentTimeMillis();

        String sEvent = mEventView.getText().toString();
        String sNotes = mNotesView.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = sdf.format(date);
        String sTime = mTimeView.getText().toString();
        String sEmployeeID = null;
        String sBussinessGroupID = null;
        String sBeaconID = null;

        if(mLocationNameView.isEnabled())
        {
            if(mLocationNameView.getText().toString().matches(""))
            {
                mLocationNameView.setError("Please fill the location");
            }
            else
            {
                sLocationName = mLocationNameView.getText().toString();
                sLocationID=null;
                sLocationAddress=null;
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

        }

        if((mLocationNameView.isEnabled()&&!mLocationNameView.getText().toString().matches(""))||!spnSearchLocation.getSelectedItem().toString().matches(""))
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Loading");
            progressDialog.show();

            SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm:ss");
            Calendar todaysTime = new GregorianCalendar();
            if(checkStartDay==false&&!attendancePrivilege.equals("False")&&attendancePrivilege!=null)
            {

                //adding time 1 second for checkin
                try{
                    todaysTime.setTime(timeFmt.parse(sTime));
                    todaysTime.add(Calendar.SECOND,1);

                }catch (Exception e)
                {

                }

                Call<Attendence> call3 = apiInterface.inputAbsence(sUsername, sEmployeeID, sBussinessGroupID, dateString, sTime, sBeaconID, sLocationID, sLocationName, sLocationAddress, sLongitude, sLatitude, "Start Day", null, sNotes, sEvent,timeZoneOffset);
                call3.enqueue(new Callback<Attendence>() {
                    @Override
                    public void onResponse(Call<Attendence> call, Response<Attendence> response) {
                        Attendence data = response.body();

                        if (data != null && data.getIsSucceed()) {
                            Toast.makeText(CheckInOutDetailActivity.this, "Start Day", Toast.LENGTH_LONG).show();
                        }else if(data != null && data.getMessage() =="Please Check Your Time And Date Settings"){
                            Toast.makeText(CheckInOutDetailActivity.this, data.getMessage(), Toast.LENGTH_LONG).show();

                        } else {
                            try {
                                //JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(CheckInOutDetailActivity.this, "Failed to Start Day", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(CheckInOutDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Attendence> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                        call.cancel();
                    }
                });
            }



            sTime=new SimpleDateFormat("HH:mm:ss").format(todaysTime.getTime());


            // khusus logType di hardcode -> LogType -> Start Day
            Call<Attendence> call3 = apiInterface.inputAbsence(sUsername, sEmployeeID, sBussinessGroupID, dateString, sTime, sBeaconID, sLocationID, sLocationName, sLocationAddress, sLongitude, sLatitude, sLogType, sPhoto, sNotes, sEvent, timeZoneOffset);
            call3.enqueue(new Callback<Attendence>() {
                @Override
                public void onResponse(Call<Attendence> call, Response<Attendence> response) {
                    progressDialog.dismiss();
                    Attendence data = response.body();

                    if (data != null && data.getIsSucceed()) {
                        Toast.makeText(CheckInOutDetailActivity.this, "Data Submitted", Toast.LENGTH_LONG).show();
                        finish();
                    } else if(data != null && data.getMessage() =="Please Check Your Time And Date Settings"){
                        Toast.makeText(CheckInOutDetailActivity.this, data.getMessage(), Toast.LENGTH_LONG).show();

                    }else {
                        try {
                            //JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(CheckInOutDetailActivity.this, "Failed to Submit", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(CheckInOutDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<Attendence> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                    call.cancel();
                }
            });
        }

    }


    public void setEnableSpinnerAndEditTextLocation()
    {

        if(spnSearchLocation.getSelectedItem().toString().matches("--other--"))
        {
            mLocationNameView.setEnabled(true);
        }
        else if(!spnSearchLocation.getSelectedItem().toString().matches("--other--"))
        {
            mLocationNameView.setEnabled(false);
        }

        if(mLocationNameView.getText().toString().matches(""))
        {
            spnSearchLocation.setEnabled(true);
        }
        else
            spnSearchLocation.setEnabled(false);

    }

}
