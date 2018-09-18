package id.co.indocyber.android.starbridges.activity;

import android.Manifest;
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
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.StarbridgeApplication;
import id.co.indocyber.android.starbridges.adapter.HistoryAdapter;
import id.co.indocyber.android.starbridges.model.Attendence;
import id.co.indocyber.android.starbridges.model.history.History;
import id.co.indocyber.android.starbridges.model.history.ReturnValue;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.utility.AlertDialogManager;
import id.co.indocyber.android.starbridges.utility.GlobalVar;
import id.co.indocyber.android.starbridges.utility.SessionManagement;
import id.co.indocyber.android.starbridges.utility.SharedPreferenceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BeaconDetailActivity extends AppCompatActivity {

    private EditText txtEvent;
    private EditText txtDate;
    private EditText txtTime;
    private EditText txtLocation;
    private EditText txtNotes;
    private Button btnSubmit;
    private APIInterfaceRest apiInterface;
    private ProgressDialog progressDialog;
    private String dateString, dateString2, timeString;
    private String beaconFindedString;
    private id.co.indocyber.android.starbridges.model.BeaconData.ReturnValue beaconFinded;
    private SessionManagement session;
    private FusedLocationProviderClient client;
    String sLocationID, sLocationName, sLocationAddress, sLatitude, sLongitude;
    private String singleChoiceSelected;

    private String[] attendance;

    private GoogleApiClient googleApiClient;

    final static int REQUEST_LOCATION = 199;

    static final int REQUEST_ACCESS_LOCATION = 101;

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    private String sPhoto;

    private String sBeaconID;

    private void assignView()
    {

        txtEvent=(EditText)findViewById(R.id.txt_event_b);
        txtDate=(EditText)findViewById(R.id.txt_date_b);
        txtTime=(EditText)findViewById(R.id.txt_time_b);
        txtLocation=(EditText)findViewById(R.id.txt_location_b);
        txtNotes=(EditText)findViewById(R.id.txt_notes_b);
        btnSubmit=(Button)findViewById(R.id.btn_submit_b);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_detail);
        setTitle("ATTENDANCE");
        Toast.makeText(BeaconDetailActivity.this, "Automatic Beacon Scanner Off", Toast.LENGTH_SHORT).show();
        assignView();

        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm:ss");
        dateString = sdf.format(date);
        dateString2 = sdf2.format(date);
        timeString = sdf3.format(date);

        Gson gson=new Gson();

        beaconFindedString=getIntent().getStringExtra("beaconFindedString");
        try{
            beaconFinded=gson.fromJson(beaconFindedString, id.co.indocyber.android.starbridges.model.BeaconData.ReturnValue.class);
            sLocationID=beaconFinded.getLocationID().toString();
            sLocationName=beaconFinded.getLocationName();
            sLocationAddress=beaconFinded.getLocationAddress();
            sBeaconID=beaconFinded.getBeaconID().toString();
        } finally {

        }


        SharedPreferenceUtils.setSetting(getApplicationContext(),"beaconScanner", "" );
        StarbridgeApplication.stopScanningBeacon();

        assignAction();

        getAttendaceLog(dateString2, dateString2);
    }

    private void assignAction()
    {
        txtDate.setText(dateString);
        txtTime.setText(timeString);
        txtLocation.setText(beaconFinded.getLocationName());
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSingleChoiceDialog();
            }
        });
    }

    public void getAttendaceLog(String DateFrom, String DateTo) {
        apiInterface = APIClient.getHistory(GlobalVar.getToken()).create(APIInterfaceRest.class);
        if(progressDialog==null||!progressDialog.isShowing())
        {
            progressDialog = new ProgressDialog(BeaconDetailActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        // khusus logType di hardcode -> LogType -> Start Day
        Call<History> call3 = apiInterface.getHistory(DateFrom, DateTo);
        call3.enqueue(new Callback<History>() {
            @Override
            public void onResponse(Call<History> call, Response<History> response) {
                progressDialog.dismiss();
                History data = response.body();
                session = new SessionManagement(getApplicationContext());
                HashMap<String, String> user = session.getUserDetails();
                String locationId = user.get(SessionManagement.KEY_LOCATION_ID);
                if (data != null && data.getIsSucceed()) {

                    String lastLogType="";
                    if(data.getReturnValue().size()>0)
                    {
//                        int dataSize=data.getReturnValue().size()-1;
                        lastLogType=data.getReturnValue().get(0).getLogType();

                    }

                    if(lastLogType==null||lastLogType.equals(""))
                    {
                        attendance = new String[]{
                                "Start Day"
                        };
                    }
                    else if(lastLogType.equalsIgnoreCase("start day"))
                    {
                        attendance = new String[]{
                                "Check In", "End Day"
                        };
                    }
                    else if(lastLogType.equalsIgnoreCase("check in"))
                    {
                        attendance = new String[]{
                                "Check Out"
                        };
                    }
                    else if(lastLogType.equalsIgnoreCase("check out"))
                    {
                        attendance = new String[]{
                                "Check In", "End Day"
                        };
                    }
                    else
                    {
                        btnSubmit.setEnabled(false);
                        btnSubmit.setText("End Day");
                        btnSubmit.setBackground(ContextCompat.getDrawable(BeaconDetailActivity.this, R.drawable.rounded_btn_disabled));
                    }

                } else {
                    try {
                        //JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(BeaconDetailActivity.this, "Session expired, and please rescan beacon again", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(BeaconDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    session.logoutUser();
                }

            }

            @Override
            public void onFailure(Call<History> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.error_connection_beacon), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    private void showSingleChoiceDialog() {
//        single_choice_selected = attendance[0];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Attendance");
        builder.setSingleChoiceItems(attendance, -2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                singleChoiceSelected = attendance[i];
            }
        });
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

                final LocationManager manager = (LocationManager) BeaconDetailActivity.this.getSystemService(Context.LOCATION_SERVICE);

                if (!hasGPSDevice(BeaconDetailActivity.this)) {
                    Toast.makeText(BeaconDetailActivity.this, "Gps not Supported", Toast.LENGTH_SHORT).show();
                }

                else if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(BeaconDetailActivity.this)) {
                    Log.e("Starbridges", "Gps already enabled");
                    Toast.makeText(BeaconDetailActivity.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
                    enableLoc();
                } else {
                    Log.e("Starbridges", "Gps already enabled");
                    //Toast.makeText(LoginActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
                }

                if(singleChoiceSelected==null||singleChoiceSelected.equalsIgnoreCase(""))
                {
                    AlertDialogManager alertDialogManager = new AlertDialogManager();
                    alertDialogManager.showAlertDialog(BeaconDetailActivity.this, "Warning","Please select attendance option",false);
                }
                else if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(BeaconDetailActivity.this)) {
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
                        //Toast.makeText(CheckInOutActivity.this,"Terdeteksi",Toast.LENGTH_SHORT).show();
                        AlertDialogManager alertDialogManager = new AlertDialogManager();
                        alertDialogManager.showAlertDialog(BeaconDetailActivity.this, "Warning","Please Uninstall your Fake GPS Apps",false);
                    }
                    else
                        getCoordinate();
                }


                // Todo Location Already on  ... end


//                Toast.makeText(BeaconDetailActivity.this, singleChoiceSelected, Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
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
            public void onSuccess(Location location) {
                if (location != null) {
                    sLatitude = String.valueOf(location.getLatitude());
                    sLongitude = String.valueOf(location.getLongitude());

                    //distance between 2 coordinate
//                    Location locatioNow=new Location("");
//                    locatioNow.setLatitude(Double.parseDouble(sLatitude));
//                    locatioNow.setLongitude(Double.parseDouble(sLongitude));
//
//                    Location locationBeacon=new Location("");
//                    locationBeacon.setLatitude(Double.parseDouble(beaconFinded.getLatitude()));
//                    locationBeacon.setLongitude(Double.parseDouble(beaconFinded.getLongitude()));
//
//                    float distance=locatioNow.distanceTo(locationBeacon);
//
//                    Log.d("distance", distance+"");
//                    sLatitude=null;
//                    sLongitude=null;


                }
                if(sLatitude==null&&sLongitude==null)
                {
                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(BeaconDetailActivity.this);
                    alert.setTitle(getString(R.string.failed_to_process));
                    alert.setMessage(getString(R.string.attention_cant_get_location_attendance));
                    alert.setPositiveButton(getString(R.string.take_photo), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(BeaconDetailActivity.this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
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
                    callInputAbsence();
                }
            }
        });


    }

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
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
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
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public void callInputAbsence()
    {


        String sUsername = GlobalVar.loginName();
        String sEmployeeID = null;
        String sBussinessGroupID = null;

        long date = System.currentTimeMillis();

        String sEvent = null;
        String sNotes = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        String dateString = sdf.format(date);
        String sTime = sdf2.format(date);

        TimeZone timezone = TimeZone.getDefault();
        int timeZoneOffset = timezone.getRawOffset()/(60 * 60 * 1000);


        //        if(!progressDialog.isShowing())
//        {
        final ProgressDialog progressDialog2 = new ProgressDialog(BeaconDetailActivity.this);
        progressDialog2.setTitle("Loading");
        progressDialog2.setCancelable(false);
        progressDialog2.show();
//        }


        Call<Attendence> call3 = apiInterface.inputAbsence(sUsername, sEmployeeID, sBussinessGroupID, dateString, sTime, sBeaconID, sLocationID, sLocationName, sLocationAddress, sLongitude, sLatitude, singleChoiceSelected, sPhoto, sNotes, sEvent, timeZoneOffset);
        call3.enqueue(new Callback<Attendence>() {
            @Override
            public void onResponse(Call<Attendence> call, Response<Attendence> response) {
                Attendence data = response.body();
                progressDialog2.dismiss();

                if (data != null && data.getIsSucceed()) {
                    Toast.makeText(BeaconDetailActivity.this, "Data Submitted", Toast.LENGTH_LONG).show();
                    finish();
//                    startActivity(getIntent());
                    SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
                    String dateString3;
                    long date2 = System.currentTimeMillis();
                    dateString3 = sdf2.format(date2);
                    Intent history = new Intent(BeaconDetailActivity.this, HistoriesActivity.class);
                    history.putExtra("from", dateString3);
                    history.putExtra("to", dateString3);
                    startActivity(history);

                } else if(data != null && data.getMessage() =="Please Check Your Time And Date Settings"){
                    Toast.makeText(BeaconDetailActivity.this, data.getMessage(), Toast.LENGTH_LONG).show();

                }else {
                    try {
                        //JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(BeaconDetailActivity.this, "Failed to Submit", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(BeaconDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(BeaconDetailActivity.this)
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
                                status.startResolutionForResult(BeaconDetailActivity.this, REQUEST_LOCATION);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
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
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}
