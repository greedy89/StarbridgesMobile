package id.co.indocyber.android.starbridges.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import id.co.indocyber.android.starbridges.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import id.co.indocyber.android.starbridges.utility.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartEndDayDetailActivity extends AppCompatActivity {
    static final int REQUEST_ACCESS_LOCATION = 101;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private FusedLocationProviderClient client;

    private EditText mEventView, mDateView, mTimeView, mLocationNameView, mNotesView;
    private Button mSubmit;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private String sLocationID, sUsername, sLongitude, sLatitude, sDate, sTime,sLogType;
    private APIInterfaceRest apiInterface;
    private ProgressDialog progressDialog;
    String sLocationName;
    String sLocationAddress;
    int timeZoneOffset;
    final List<ReturnValue> listReturnValue= new ArrayList<>();
    SessionManagement session;
    Spinner spnSearchLocation;
    String sPhoto = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_end_day_detail);
        setTitle("ATTENDANCE");
        mEventView = (EditText) findViewById(R.id.txt_event_sd);
        mDateView = (EditText) findViewById(R.id.txt_date_sd);
        mTimeView = (EditText) findViewById(R.id.txt_time_sd);
        mLocationNameView = (EditText) findViewById(R.id.txt_location_name_sd);
        mNotesView = (EditText) findViewById(R.id.txt_notes_sd);
        spnSearchLocation=(Spinner)findViewById(R.id.spnSearchLocation);

        mSubmit = (Button) findViewById(R.id.btn_submit_sd);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSubmit.setEnabled(false);

                SubmitData();
            }
        });

        TimeZone timezone = TimeZone.getDefault();
        //String TimeZoneName = timezone.getDisplayName();
        timeZoneOffset = timezone.getRawOffset()/(60 * 60 * 1000);

        Intent intent = getIntent();
        sDate = intent.getStringExtra("date");
        sTime = intent.getStringExtra("time");
        sUsername = GlobalVar.loginName();
        sLogType=intent.getStringExtra("logType");
        client = LocationServices.getFusedLocationProviderClient(this);


        mDateView.setText(sDate);
        mTimeView.setText(sTime);
        initSpinnerLoc();


        spnSearchLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0)
                {
                    final ReturnValue returnValue1=(ReturnValue)spnSearchLocation.getItemAtPosition(i);
                    //Log.d("LocationIdnya", returnValue1.getID());
                    sLocationID=returnValue1.getID();
                    sLocationName=returnValue1.getName();
                    sLocationAddress=returnValue1.getAddress();
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

    }


    void getLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_LOCATION);
        }

        client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    sLatitude = String.valueOf(location.getLatitude());
                    sLongitude = String.valueOf(location.getLongitude());
//                    sLatitude=null;
//                    sLongitude=null;

                }
                if(sLatitude==null&&sLongitude==null)
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(StartEndDayDetailActivity.this);
                    alert.setTitle(getString(R.string.failed_to_process));
                    alert.setMessage(getString(R.string.attention_cant_get_location_attendance));
                    alert.setPositiveButton(getString(R.string.take_photo), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(StartEndDayDetailActivity.this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
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
                    mSubmit.setEnabled(true);
                }
                else
                {
                    callInputAbsence();

                }
            }
        });
    }

    public void SubmitData() {
        if(mLocationNameView.isEnabled())
        {
            if(mLocationNameView.getText().toString().matches(""))
            {
                mLocationNameView.setError("Please fill the location");
                mSubmit.setEnabled(true);
            }
            else
            {
                getLocation();

            }
            sLocationName = mLocationNameView.getText().toString();
            sLocationAddress = null;
            sLocationID=null;

        }
        else
            getLocation();


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


//    public void getLocation() {
//        client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    sLatitude=String.valueOf(location.getLatitude());
//                    sLongitude=String.valueOf(location.getLongitude());
//
//                }
//            }
//        });
//    }


    public void initSpinnerLoc() {

        progressDialog = new ProgressDialog(StartEndDayDetailActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
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

                    List<ReturnValue> LocItems = response.body().getReturnValue();
                    if (LocItems!= null){
                        listReturnValue.addAll(LocItems);
                    } else{
                        Toast.makeText(StartEndDayDetailActivity.this, "spinner Tidak dapat data",Toast.LENGTH_LONG).show();
                    }

                    ArrayAdapter<ReturnValue> adapter = new ArrayAdapter<ReturnValue>(StartEndDayDetailActivity.this,
                            android.R.layout.simple_spinner_item, listReturnValue);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnSearchLocation.setAdapter(adapter);
                } else {

                    Toast.makeText(StartEndDayDetailActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }

                session = new SessionManagement(getApplicationContext());
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
                Toast.makeText(StartEndDayDetailActivity.this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();

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
        if(counter >= listReturnValue.size())
            counter=0;

        spnSearchLocation.setSelection(counter);
        progressDialog.dismiss();
    }

    public void callInputAbsence() {
        // get token
        apiInterface = APIClient.inputAbsence(GlobalVar.getToken()).create(APIInterfaceRest.class);
        progressDialog = new ProgressDialog(StartEndDayDetailActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

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
                mSubmit.setEnabled(true);
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
            // khusus logType di hardcode -> LogType -> Start Day
            Call<Attendence> call3 = apiInterface.inputAbsence(sUsername, sEmployeeID, sBussinessGroupID, dateString, sTime, sBeaconID, sLocationID, sLocationName, sLocationAddress, sLongitude, sLatitude, sLogType, sPhoto, sNotes, sEvent, timeZoneOffset);
            call3.enqueue(new Callback<Attendence>() {
                @Override
                public void onResponse(Call<Attendence> call, Response<Attendence> response) {
                    progressDialog.dismiss();
                    Attendence data = response.body();

                    if (data != null && data.getIsSucceed()) {
                        Toast.makeText(StartEndDayDetailActivity.this, "Data Submitted", Toast.LENGTH_LONG).show();
                        finish();
                    }else if(data != null && data.getMessage() =="Please Check Your Time And Date Settings"){
                        Toast.makeText(StartEndDayDetailActivity.this, data.getMessage(), Toast.LENGTH_LONG).show();

                    } else {
                        try {
                            //JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(StartEndDayDetailActivity.this, data.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(StartEndDayDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    mSubmit.setEnabled(true);

                }

                @Override
                public void onFailure(Call<Attendence> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                    call.cancel();
                    mSubmit.setEnabled(true);
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
