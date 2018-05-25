package com.example.android.starbridges.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.starbridges.R;
import com.example.android.starbridges.model.balanceType.BalanceType;
import com.example.android.starbridges.model.editleaverequest.EditLeaveRequest;
import com.example.android.starbridges.model.requestconfirmation.RequestConfirmation;
import com.example.android.starbridges.model.requesttype.RequestType;
import com.example.android.starbridges.model.requesttype.ReturnValue;
import com.example.android.starbridges.model.saveLeaveRequest.SaveLeaveRequest;
import com.example.android.starbridges.network.APIClient;
import com.example.android.starbridges.network.APIInterfaceRest;
import com.example.android.starbridges.utility.GlobalVar;
import com.example.android.starbridges.utility.SessionManagement;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveRequestDetailActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MY_GALLERY_REQUEST_CODE = 100;
    private static final int PICK_IMAGE = 999;

    // declara var instance
    private APIInterfaceRest apiInterface;
    private ProgressDialog progressDialog;
    private EditText startDate, endDate, notes;
    private Spinner spinnerRequestType, spinnerBalanceType;
    private ImageView imageView;

    Calendar myCalendar = Calendar.getInstance();
    Button saveBtn, submitBtn, uploadBtn;
    String saveStr,submitStr;

    Intent intent;

    // declare parameter (balikan dari api)
    String id="";
    Integer employeeID;
    String roster;
    String requestDate;
    String employeeNIK;
    String employeeName;
    Integer leaveRequestRuleID;
    String leaveRequestType;
    String employeeLeaveBalanceUID;
    String currentBalance;
    String balanceExpireDate;
    String totalUnit;
    String totalUnitReduce;
    String startLeave;
    String endLeave;
    String leaveAt;
    String returnAt;
    Boolean minIntervalViolation;
    Boolean unitLimitViolation;
    Boolean occurenceViolation;
    String notesStr;
    String attachmentFile;
    String attachmentID;
    String decisionNumber;
    String transactionStatusID;
    String approveDate;
    Boolean isHalfDay;
    String submitType;
    String message;
    String transactionStatusSaveOrSubmit;
    String photo;
    Boolean fullAccess;
    List<String> exclusionFields;
    String accessibilityAttribute;

    int idRequestType= 0;
    String balanceUID = "";

    // get data from request confirmation
    com.example.android.starbridges.model.requestconfirmation.ReturnValue requestConfirmation;

    // get data from edit leave request
    com.example.android.starbridges.model.editleaverequest.ReturnValue editLeaveRequest;

    // get data from spinner balance type
    List<com.example.android.starbridges.model.balanceType.ReturnValue> LocItems1;
    List<com.example.android.starbridges.model.balanceType.ReturnValue> listReturnValue1;

    // get data from spinner request type
    List<ReturnValue> LocItems;
    List<ReturnValue> listReturnValue;

    // declare session
    SessionManagement session;

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            myCalendar.set(Calendar.YEAR, i);
            myCalendar.set(Calendar.MONTH, i1);
            myCalendar.set(Calendar.DAY_OF_MONTH, i2);
            updateLabel();
        }
    };

    DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            myCalendar.set(Calendar.YEAR, i);
            myCalendar.set(Calendar.MONTH, i1);
            myCalendar.set(Calendar.DAY_OF_MONTH, i2);
            updateLabel2();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request_detail);
        setTitle("Leave Request");

        // get reference to component
        startDate = (EditText) findViewById(R.id.dateStart);
        endDate = (EditText) findViewById(R.id.dateEnd);
        notes = (EditText) findViewById(R.id.notes);
        spinnerRequestType = (Spinner) findViewById(R.id.requestTypeSpinner);
        spinnerBalanceType = (Spinner) findViewById(R.id.balanceTypeSpinner);
        saveBtn = (Button) findViewById(R.id.btnSave);
        submitBtn = (Button) findViewById(R.id.btnSubmit);
        uploadBtn = (Button) findViewById(R.id.btnUpload);
        imageView = (ImageView) findViewById(R.id.imageView);

        // get session
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String employeeID = user.get(SessionManagement.KEY_EMPLOYEE_ID);
        GlobalVar.setEmployeeId(employeeID);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(LeaveRequestDetailActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(LeaveRequestDetailActivity.this, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set val "Save" to variable
                saveStr = "Save";
                submitStr = "";

                // call method
                requestConfirmation();

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(LeaveRequestDetailActivity.this);
                alert.setTitle("Request Confirmation");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // set val "Submit" to variable
                        saveStr = "";
                        submitStr = "Submit";

                        //call method
                        requestConfirmation();
                    }
                });

                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.show();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                //dispatchTakePictureIntent();
            }
        });

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LeaveRequestDetailActivity.this, new String[]{Manifest.permission.CAMERA}, MY_GALLERY_REQUEST_CODE);
        }

        // call load spinner
        initSpinnerRequestType();

        spinnerRequestType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    final ReturnValue returnValue = (ReturnValue) spinnerRequestType.getItemAtPosition(i);
                    leaveRequestRuleID = returnValue.getID();
                    leaveRequestType = returnValue.getName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // call load spinner balance type
        initSpinnerBalanceType();

        spinnerBalanceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0) {
                    final com.example.android.starbridges.model.balanceType.ReturnValue returnValue1 = (com.example.android.starbridges.model.balanceType.ReturnValue) spinnerBalanceType.getItemAtPosition(i);
                    employeeLeaveBalanceUID = returnValue1.getValue().toString();
                    //Toast.makeText(LeaveRequestDetailActivity.this, "UID " + balanceUID, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Get intent from adapter
        intent = getIntent();
        if(intent.getStringExtra("ID") != null){
            id = intent.getStringExtra("ID");
            editLeaveRequest(id);
        }
    }

    public void requestConfirmation(){
        // get token
        apiInterface = APIClient.requestConfirmation(GlobalVar.getToken()).create(APIInterfaceRest.class);
        progressDialog = new ProgressDialog(LeaveRequestDetailActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();

        // create new
        if(intent.getStringExtra("ID") == null) {
            id = "";
            employeeID = Integer.parseInt(GlobalVar.getEmployeeId());
            roster = "";
            requestDate = "";
            employeeNIK = GlobalVar.getEmployeeId();
            employeeName = GlobalVar.getFullname();
            //leaveRequestRuleID = Integer.parseInt(requestTypeID);
            //leaveRequestType = requestTypeName;
            //employeeLeaveBalanceUID = balanceUID;
            currentBalance = "";
            balanceExpireDate = "";
            totalUnit = "";
            totalUnitReduce = "";
            startLeave = startDate.getText().toString();
            endLeave = endDate.getText().toString();
            leaveAt = "";
            returnAt = "";
            minIntervalViolation = true;
            unitLimitViolation = true;
            occurenceViolation = true;
            notesStr = notes.getText().toString();
            attachmentFile = "";
            attachmentID = "";
            decisionNumber = "";
            transactionStatusID = "";
            approveDate = "";
            isHalfDay = true;
            submitType = "";
            message = notes.getText().toString();
            //transactionStatusSaveOrSubmit = "Save"; (sudah diinisialisai di listener btn)
            //photo = "";
            fullAccess = true;
            exclusionFields = new ArrayList<String>();
            accessibilityAttribute = "";
        }else{

            // set text u/ mendapatkan edit terbaru
            startLeave = startDate.getText().toString();
            endLeave = endDate.getText().toString();
            notesStr = notes.getText().toString();
        }

        apiInterface = APIClient.requestConfirmation(GlobalVar.getToken()).create(APIInterfaceRest.class);
        Call<RequestConfirmation> call3;

        if(saveStr != "") {
            call3 = apiInterface.requestConfirmationSave(
                    id, employeeID, roster, requestDate, employeeNIK, employeeName, leaveRequestRuleID, leaveRequestType,
                    employeeLeaveBalanceUID, currentBalance, balanceExpireDate, totalUnit, totalUnitReduce, startLeave,
                    endLeave, leaveAt, returnAt, minIntervalViolation, unitLimitViolation, occurenceViolation, notesStr,
                    attachmentFile, attachmentID, decisionNumber, transactionStatusID, approveDate, isHalfDay, submitType,
                    message, transactionStatusSaveOrSubmit, photo, fullAccess, exclusionFields, accessibilityAttribute
            );
        }else{
            call3 = apiInterface.requestConfirmationSubmit(
                    id, employeeID, roster, requestDate, employeeNIK, employeeName, leaveRequestRuleID, leaveRequestType,
                    employeeLeaveBalanceUID, currentBalance, balanceExpireDate, totalUnit, totalUnitReduce, startLeave,
                    endLeave, leaveAt, returnAt, minIntervalViolation, unitLimitViolation, occurenceViolation, notesStr,
                    attachmentFile, attachmentID, decisionNumber, transactionStatusID, approveDate, isHalfDay, submitType,
                    message, transactionStatusSaveOrSubmit, photo, fullAccess, exclusionFields, accessibilityAttribute
            );
        }

        call3.enqueue(new Callback<RequestConfirmation>() {

            @Override
            public void onResponse(Call<RequestConfirmation> call, Response<RequestConfirmation> response) {
                progressDialog.dismiss();
                RequestConfirmation data = response.body();


                if (data != null && data.getIsSucceed()) {
                    //Toast.makeText(LeaveRequestDetailActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    requestConfirmation = data.getReturnValue();

                    id = requestConfirmation.getID();
                    employeeID = Integer.parseInt(requestConfirmation.getEmployeeID());
                    roster = requestConfirmation.getRoster();
                    requestDate = requestConfirmation.getRequestDate();
                    employeeNIK = requestConfirmation.getEmployeeNIK();
                    employeeName = requestConfirmation.getEmployeeName();
                    leaveRequestRuleID = requestConfirmation.getLeaveRequestRuleID();
                    leaveRequestType = requestConfirmation.getLeaveRequestType();
                    employeeLeaveBalanceUID = requestConfirmation.getEmployeeLeaveBalanceUID();
                    currentBalance = requestConfirmation.getCurrentBalance().toString();
                    balanceExpireDate = requestConfirmation.getBalanceExpireDate();
                    totalUnit = requestConfirmation.getTotalUnit().toString();
                    totalUnitReduce = requestConfirmation.getTotalUnit().toString();
                    //startLeave = startDate.getText().toString();
                    //endLeave = endDate.getText().toString();
                    leaveAt = requestConfirmation.getLeaveAt();
                    returnAt = requestConfirmation.getReturnAt();
                    minIntervalViolation = requestConfirmation.getMinIntervalViolation();
                    unitLimitViolation = requestConfirmation.getUnitLimitViolation();
                    occurenceViolation = requestConfirmation.getOccurenceViolation();
                    notesStr = requestConfirmation.getNotes();
                    attachmentFile = requestConfirmation.getAttachmentFile();
                    attachmentID = requestConfirmation.getAttachmentID();
                    decisionNumber = requestConfirmation.getDecisionNumber();
                    transactionStatusID = requestConfirmation.getTransactionStatusID().toString();
                    approveDate = requestConfirmation.getApprovedDate();
                    isHalfDay = requestConfirmation.getIsHalfDay();
                    submitType = requestConfirmation.getSubmitType();
                    message = requestConfirmation.getMessage();
                    transactionStatusSaveOrSubmit = requestConfirmation.getTransactionStatusSaveOrSubmit(); //(sudah diinisialisasi di btn listener)
                    //photo = requestConfirmation.getPhoto();
                    fullAccess = requestConfirmation.getFullAccess();
                    exclusionFields = requestConfirmation.getExclusionFields();
                    accessibilityAttribute = requestConfirmation.getAccessibilityAttribute();

                    AlertDialog.Builder alert = new AlertDialog.Builder(LeaveRequestDetailActivity.this);
                    alert.setTitle("Request Confirmation");
                    alert.setMessage(
                            "Request Type : \n" + requestConfirmation.getLeaveRequestType() +
                                    "Leave : \n" + requestConfirmation.getLeaveAt() + " - " + requestConfirmation.getReturnAt() +
                                    "Total Unit : \n" + requestConfirmation.getTotalUnit() +
                                    "Unit Reduce : \n" + requestConfirmation.getTotalUnitReduce() +
                                    "Notes : \n" + requestConfirmation.getNotes()
                    );
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveLeaveRequest();
                        }
                    });

                    alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // if this button is clicked, just close the dialog box
                            dialogInterface.cancel();
                        }
                    });

                    alert.show();

                } else {
                    Toast.makeText(LeaveRequestDetailActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RequestConfirmation> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LeaveRequestDetailActivity.this, "Something went wrong...Please try again!", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });

    }

    public void editLeaveRequest(String ids){
        // get token
        apiInterface = APIClient.editLeaveRequest(GlobalVar.getToken()).create(APIInterfaceRest.class);
        progressDialog = new ProgressDialog(LeaveRequestDetailActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();

        //apiInterface = APIClient.editLeaveRequest(GlobalVar.getToken()).create(APIInterfaceRest.class);
        Call<EditLeaveRequest> call3 = apiInterface.editLeaveRequst(ids);
        call3.enqueue(new Callback<EditLeaveRequest>() {

            @Override
            public void onResponse(Call<EditLeaveRequest> call, Response<EditLeaveRequest> response) {
                progressDialog.dismiss();
                EditLeaveRequest data = response.body();

                if (data != null && data.getIsSucceed()) {
                    //com.example.android.starbridges.model.editleaverequest.ReturnValue editLeaveRequest = data.getReturnValue();
                    editLeaveRequest = data.getReturnValue();

                    // used for set text spinner
                    idRequestType= data.getReturnValue().getLeaveRequestRuleID();
                    balanceUID = data.getReturnValue().getEmployeeLeaveBalanceUID();

                    // get data from edit leave request
                    id = editLeaveRequest.getID();
                    employeeID = Integer.parseInt(editLeaveRequest.getEmployeeID());
                    roster = editLeaveRequest.getRoster();
                    requestDate = editLeaveRequest.getRequestDate();
                    employeeNIK = editLeaveRequest.getEmployeeNIK();
                    employeeName = editLeaveRequest.getEmployeeName();
                    leaveRequestRuleID = editLeaveRequest.getLeaveRequestRuleID();
                    leaveRequestType = editLeaveRequest.getLeaveRequestType();
                    employeeLeaveBalanceUID = editLeaveRequest.getEmployeeLeaveBalanceUID();
                    currentBalance = editLeaveRequest.getCurrentBalance().toString();
                    balanceExpireDate = editLeaveRequest.getBalanceExpireDate();
                    totalUnit = editLeaveRequest.getTotalUnit().toString();
                    totalUnitReduce = editLeaveRequest.getTotalUnit().toString();
                    startLeave = editLeaveRequest.getStartLeave();
                    endLeave = editLeaveRequest.getEndLeave();
                    leaveAt = editLeaveRequest.getLeaveAt();
                    returnAt = editLeaveRequest.getReturnAt();
                    minIntervalViolation = editLeaveRequest.getMinIntervalViolation();
                    unitLimitViolation = editLeaveRequest.getUnitLimitViolation();
                    occurenceViolation = editLeaveRequest.getOccurenceViolation();
                    notesStr = editLeaveRequest.getNotes();
                    attachmentFile = editLeaveRequest.getAttachmentFile();
                    attachmentID = editLeaveRequest.getAttachmentID();
                    decisionNumber = editLeaveRequest.getDecisionNumber();
                    transactionStatusID = editLeaveRequest.getTransactionStatusID().toString();
                    approveDate = editLeaveRequest.getApprovedDate();
                    isHalfDay = editLeaveRequest.getIsHalfDay();
                    submitType = editLeaveRequest.getSubmitType();
                    message = editLeaveRequest.getMessage();
                    transactionStatusSaveOrSubmit = editLeaveRequest.getTransactionStatusSaveOrSubmit();
                    photo = editLeaveRequest.getPhoto();
                    fullAccess = editLeaveRequest.getFullAccess();
                    exclusionFields = editLeaveRequest.getExclusionFields();
                    accessibilityAttribute = editLeaveRequest.getAccessibilityAttribute();

                    // start leave
                    //String startDateStr = editLeaveRequest.getStartLeave();
                    startDate.setText(formatDate(startLeave));

                    // end date
                    //String endDateStr = editLeaveRequest.getEndLeave();
                    endDate.setText(formatDate(endLeave));

                    // notes
                    //notes.setText(editLeaveRequest.getNotes());
                    notes.setText(notesStr);

                    // load Image if exist
                    if(photo != null) {
                        byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageView.setImageBitmap(bitmap);
                    }

                } else {
                    Toast.makeText(LeaveRequestDetailActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                    //finish();
                }

                initSpinnerRequestType();
                initSpinnerBalanceType();
            }

            @Override
            public void onFailure(Call<EditLeaveRequest> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LeaveRequestDetailActivity.this, "Something went wrong...Please try again!", Toast.LENGTH_SHORT).show();
                call.cancel();

                initSpinnerRequestType();
                initSpinnerBalanceType();
            }
        });

    }


    public void saveLeaveRequest(){
        // get token
        apiInterface = APIClient.saveLeaveRequest(GlobalVar.getToken()).create(APIInterfaceRest.class);
        progressDialog = new ProgressDialog(LeaveRequestDetailActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();

        apiInterface = APIClient.saveLeaveRequest(GlobalVar.getToken()).create(APIInterfaceRest.class);
        Call<SaveLeaveRequest> call3 = apiInterface.saveLeaveRequest(
                id, employeeID, roster, requestDate, employeeNIK, employeeName, leaveRequestRuleID, leaveRequestType,
                employeeLeaveBalanceUID, currentBalance, balanceExpireDate, totalUnit, totalUnitReduce, startLeave,
                endLeave, leaveAt, returnAt, minIntervalViolation, unitLimitViolation, occurenceViolation, notesStr,
                attachmentFile, attachmentID, decisionNumber, transactionStatusID, approveDate, isHalfDay, submitType,
                message, transactionStatusSaveOrSubmit, photo, fullAccess, exclusionFields, accessibilityAttribute
        );
        call3.enqueue(new Callback<SaveLeaveRequest>() {

            @Override
            public void onResponse(Call<SaveLeaveRequest> call, Response<SaveLeaveRequest> response) {
                progressDialog.dismiss();
                SaveLeaveRequest data = response.body();

                if (data != null && data.getIsSucceed()) {
                    Toast.makeText(LeaveRequestDetailActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(LeaveRequestDetailActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SaveLeaveRequest> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LeaveRequestDetailActivity.this, "Something went wrong...Please try again!", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });

    }

    public void initSpinnerBalanceType() {
        //final List<com.example.android.starbridges.model.balanceType.ReturnValue> listReturnValue1 = new ArrayList<>();
        listReturnValue1 = new ArrayList<>();
        com.example.android.starbridges.model.balanceType.ReturnValue returnValue1 = new com.example.android.starbridges.model.balanceType.ReturnValue();
        returnValue1.setText("");
        returnValue1.setValue("");
        listReturnValue1.add(returnValue1);

        apiInterface = APIClient.getBalanceType(GlobalVar.getToken()).create(APIInterfaceRest.class);
        Call<BalanceType> call3 = apiInterface.getBalanceType(GlobalVar.getEmployeeId());
        call3.enqueue(new Callback<BalanceType>() {

            @Override
            public void onResponse(Call<BalanceType> call, Response<BalanceType> response) {

                BalanceType data = response.body();

                if (data != null) {

                    LocItems1 = response.body().getReturnValue();
                    listReturnValue1.addAll(LocItems1);

                    ArrayAdapter<com.example.android.starbridges.model.balanceType.ReturnValue> adapter = new ArrayAdapter<com.example.android.starbridges.model.balanceType.ReturnValue>(LeaveRequestDetailActivity.this,
                            android.R.layout.simple_spinner_item, listReturnValue1);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerBalanceType.setAdapter(adapter);

                    // set text data while editing
                    setupSpinnerBalance();
                } else {

                    Toast.makeText(LeaveRequestDetailActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BalanceType> call, Throwable t) {
                Toast.makeText(LeaveRequestDetailActivity.this, "Something went wrong...Please try again!", Toast.LENGTH_SHORT).show();
                setupSpinnerBalance();
            }
        });

    }

    public void initSpinnerRequestType() {
        //final List<ReturnValue> listReturnValue = new ArrayList<>();
        listReturnValue = new ArrayList<>();
        ReturnValue returnValue = new ReturnValue();
        returnValue.setID(0);
        returnValue.setName("");
        listReturnValue.add(returnValue);

        apiInterface = APIClient.getRequestType(GlobalVar.getToken()).create(APIInterfaceRest.class);
        //String emp = GlobalVar.getEmployeeId();
        Call<RequestType> call3 = apiInterface.getRequestType(GlobalVar.getEmployeeId());
        call3.enqueue(new Callback<RequestType>() {

            @Override
            public void onResponse(Call<RequestType> call, Response<RequestType> response) {

                RequestType data = response.body();

                if (data != null) {
                    LocItems = response.body().getReturnValue();
                    listReturnValue.addAll(LocItems);

                    ArrayAdapter<ReturnValue> adapter = new ArrayAdapter<ReturnValue>(LeaveRequestDetailActivity.this,
                            android.R.layout.simple_spinner_item, listReturnValue);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerRequestType.setAdapter(adapter);

                    // set text while edit
                    setupSpinner();

                } else {

                    Toast.makeText(LeaveRequestDetailActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RequestType> call, Throwable t) {
                Toast.makeText(LeaveRequestDetailActivity.this, "Something went wrong...Please try again!", Toast.LENGTH_SHORT).show();
                setupSpinner();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel2() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endDate.setText(sdf.format(myCalendar.getTime()));
    }

    private String getDate(String view){
        DateFormat dateFormat;
        // view -> kebutuhan UI, other -> kirim ke server
        if(view.equals("view"))
            dateFormat = new SimpleDateFormat("dd MMM yyyy");
        else
            dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = new Date();
        return dateFormat.format(date);
    }

    private String formatDate(String str){
        int position = str.indexOf("T");
        String dateStr = str.substring(0, position);

        String dateArr[] = dateStr.split("-");

        dateStr = dateArr[1] + "/" + dateArr[2] + "/" + dateArr[0];

        return dateStr;
    }

    public void setupSpinner()
    {
        int spinnerIdSelected=0;

        if(idRequestType > 0)
        {
            for(ReturnValue x: listReturnValue)
            {
                if(x.getID()==idRequestType)
                    break;
                spinnerIdSelected++;
            }
        }

        spinnerRequestType.setSelection(spinnerIdSelected);
    }

    public void setupSpinnerBalance()
    {
        int spinnerIdSelected=0;

        if(balanceUID != "")
        {
            for(com.example.android.starbridges.model.balanceType.ReturnValue x: listReturnValue1)
            {
                if(x.getValue().equals(balanceUID))
                    break;
                spinnerIdSelected++;
            }
        }

        spinnerBalanceType.setSelection(spinnerIdSelected);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
           try {
               final Uri imageUri = data.getData();
               final InputStream imageStream = getContentResolver().openInputStream(imageUri);
               final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

               imageView.setImageBitmap(selectedImage);
               photo = encodeImage(selectedImage);
           }catch (FileNotFoundException e){
               e.printStackTrace();
               Toast.makeText(LeaveRequestDetailActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
           }
        }else{
            Toast.makeText(LeaveRequestDetailActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

}

