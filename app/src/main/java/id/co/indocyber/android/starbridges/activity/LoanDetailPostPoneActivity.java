package id.co.indocyber.android.starbridges.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.adapter.ListDraftLoanTransactionApprovedAdapter;
import id.co.indocyber.android.starbridges.model.EditDraftLoan.EditDraftLoan;
import id.co.indocyber.android.starbridges.model.EditLeaveCancelation.EditLeaveCancelation;
import id.co.indocyber.android.starbridges.model.ListDraftTransactionLoanApproved.ListDraftTransactionLoanApproved;
import id.co.indocyber.android.starbridges.model.ListLoanSchedule.ListLoanSchedule;
import id.co.indocyber.android.starbridges.model.LoanSchedule.LoanSchedule;
import id.co.indocyber.android.starbridges.model.LoanTransactionType.LoanTransactionType;
import id.co.indocyber.android.starbridges.model.LoanTransactionType.ReturnValue;
import id.co.indocyber.android.starbridges.model.MessageReturn.MessageReturn;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.network.StringConverter;
import id.co.indocyber.android.starbridges.utility.GlobalVar;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoanDetailPostPoneActivity extends AppCompatActivity {


    Spinner spnTransactionTypePostpone, spnSchedullePostpone;
    EditText txtAmountPostpone, txtDescriptionPostpone;
    Button btnSubmitPostpone, btnSavePostpone, btnCancelPostpone;

    TextView txtErrorTransactionTypePostpone, txtErrorSchedulePostpone;

    String loanBalanceID;

    APIInterfaceRest apiInterface;

    ProgressDialog progressDialog;

    List<ReturnValue> lstLoanTransactionType;

    List<id.co.indocyber.android.starbridges.model.ListLoanSchedule.ReturnValue> lstLoanSchedule;

    String transactionStatusID, transactionTypeID, employeeLoanScheduleID;
    String id;
    Integer employeeScheduleAmount;
    String remainingLoan;

    id.co.indocyber.android.starbridges.model.EditDraftLoan.ReturnValue editLoan;

    List<Object> exclusiveFields;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_postpone);

        spnTransactionTypePostpone=(Spinner)findViewById(R.id.spnTransactionTypePostpone);
        spnSchedullePostpone=(Spinner)findViewById(R.id.spnSchedullePostpone);
        txtAmountPostpone=(EditText)findViewById(R.id.txtAmountPostpone);
        txtDescriptionPostpone=(EditText)findViewById(R.id.txtDescriptionPostpone);
        btnSubmitPostpone=(Button)findViewById(R.id.btnSubmitPostpone);
        btnSavePostpone=(Button)findViewById(R.id.btnSavePostpone);
        btnCancelPostpone=(Button)findViewById(R.id.btnCancelPostpone);

        txtErrorTransactionTypePostpone=(TextView)findViewById(R.id.txtErrorTransactionTypePostpone);
        txtErrorSchedulePostpone=(TextView)findViewById(R.id.txtErrorSchedulePostpone);

        loanBalanceID= getIntent().getStringExtra("LoanBalanceId");
        remainingLoan= getIntent().getStringExtra("RemainingLoan");
        id=getIntent().getStringExtra("ID");
        if(id!=null)
        {
            getData();
        }
        else
        {
            initSpinnerTransactionType();

            initSpinnerSchedule();
        }

        spnTransactionTypePostpone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ReturnValue transactionType=(ReturnValue)spnTransactionTypePostpone.getItemAtPosition(i);
                transactionTypeID=transactionType.getId()+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnSchedullePostpone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                id.co.indocyber.android.starbridges.model.ListLoanSchedule.ReturnValue loanSchedule=(id.co.indocyber.android.starbridges.model.ListLoanSchedule.ReturnValue)spnSchedullePostpone.getItemAtPosition(i);
                employeeLoanScheduleID=loanSchedule.getID()+"";
                employeeScheduleAmount=loanSchedule.getAmount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSavePostpone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkValidation())
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoanDetailPostPoneActivity.this);
                    alert.setTitle("Request Confirmation");
                    alert.setMessage("Transaction Type\n" +
                            "\t"+spnTransactionTypePostpone.getSelectedItem().toString()+"" +
                            "\nSchedule\n" +
                            "\t"+ spnSchedullePostpone.getSelectedItem().toString() +
                            "\nAmount\n" +
                            "\t"+new StringConverter().numberFormat(txtAmountPostpone.getText().toString()) +
                            "\nDescription\n" +
                            "\t"+txtDescriptionPostpone.getText().toString() +
                            "\n\n" +
                            "This information will be saved in draft");
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveSubmitData("Save");
                        }
                    });

                    alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alert.show();

                }

            }
        });

        btnSubmitPostpone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkValidation())
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoanDetailPostPoneActivity.this);
                    alert.setTitle("Request Confirmation");
                    alert.setMessage("Transaction Type\n" +
                            "\t"+spnTransactionTypePostpone.getSelectedItem().toString()+"" +
                            "\nSchedule\n" +
                            "\t"+ spnSchedullePostpone.getSelectedItem().toString() +
                            "\nAmount\n" +
                            "\t"+new StringConverter().numberFormat(txtAmountPostpone.getText().toString()) +
                            "\nDescription\n" +
                            "\t"+txtDescriptionPostpone.getText().toString() +
                            "\n");
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveSubmitData("Submit");
                        }
                    });

                    alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alert.show();
                }

            }
        });

        btnCancelPostpone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(LoanDetailPostPoneActivity.this);
                alert.setTitle("This information will not be saved");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
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
    }

    public void initSpinnerTransactionType()
    {
        progressDialog= new ProgressDialog(LoanDetailPostPoneActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();

        lstLoanTransactionType=new ArrayList<>();
        ReturnValue returnValue=new ReturnValue();
        lstLoanTransactionType.add(returnValue);

        apiInterface = APIClient.editDraftLeaveCancelation(GlobalVar.getToken()).create(APIInterfaceRest.class);
        apiInterface.getLoanTransactionType().enqueue(new Callback<LoanTransactionType>() {
            @Override
            public void onResponse(Call<LoanTransactionType> call, Response<LoanTransactionType> response) {

                if (response.body().getIsSucceed()) {
                    lstLoanTransactionType.addAll(response.body().getReturnValue());
                    setupSpinnerTransactionType();
                } else {
                    Toast.makeText(LoanDetailPostPoneActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<LoanTransactionType> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoanDetailPostPoneActivity.this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void setupSpinnerTransactionType()
    {
        ArrayAdapter<ReturnValue> adapter = new ArrayAdapter<ReturnValue>(LoanDetailPostPoneActivity.this,
                android.R.layout.simple_spinner_item, lstLoanTransactionType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTransactionTypePostpone.setAdapter(adapter);


        if(editLoan!=null)
        {
            int counter=0;
            for(ReturnValue decisionNumber:lstLoanTransactionType)
            {
                if(editLoan.getLoanTransactionTypeID().equals(decisionNumber.getId())) break;
                counter++;
            }
            spnTransactionTypePostpone.setSelection(counter);
        }

    }

    public void initSpinnerSchedule()
    {
        if(progressDialog==null||!progressDialog.isShowing())
        {
            progressDialog= new ProgressDialog(LoanDetailPostPoneActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.show();
        }


        lstLoanSchedule=new ArrayList<>();
        final id.co.indocyber.android.starbridges.model.ListLoanSchedule.ReturnValue returnValue=new id.co.indocyber.android.starbridges.model.ListLoanSchedule.ReturnValue();
        lstLoanSchedule.add(returnValue);

        apiInterface = APIClient.editDraftLeaveCancelation(GlobalVar.getToken()).create(APIInterfaceRest.class);
        apiInterface.getListLoanSchedule(loanBalanceID).enqueue(new Callback<ListLoanSchedule>() {
            @Override
            public void onResponse(Call<ListLoanSchedule> call, Response<ListLoanSchedule> response) {

                if (response.body().getIsSucceed()) {
                    for(id.co.indocyber.android.starbridges.model.ListLoanSchedule.ReturnValue returnValue1:response.body().getReturnValue())
                    {
                        if(returnValue1.getIsProcessed()==false)
                        {
                            lstLoanSchedule.add(returnValue1);
                        }
                    }
//                    lstLoanSchedule.addAll(response.body().getReturnValue());
                    setupSpinnerLoanSchedule();
                } else {
                    Toast.makeText(LoanDetailPostPoneActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ListLoanSchedule> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoanDetailPostPoneActivity.this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void setupSpinnerLoanSchedule()
    {
        ArrayAdapter<id.co.indocyber.android.starbridges.model.ListLoanSchedule.ReturnValue> adapter = new ArrayAdapter<id.co.indocyber.android.starbridges.model.ListLoanSchedule.ReturnValue>(LoanDetailPostPoneActivity.this, android.R.layout.simple_spinner_item, lstLoanSchedule);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSchedullePostpone.setAdapter(adapter);

        if(editLoan!=null)
        {
            int counter=0;
            for(id.co.indocyber.android.starbridges.model.ListLoanSchedule.ReturnValue decisionNumber:lstLoanSchedule)
            {
//                String sScheduleId=editLoan.getEmployeeLoanScheduleID()+"";

                if(editLoan.getEmployeeLoanScheduleID().toString().equals(decisionNumber.getID())) break;
                    counter++;
            }
            if(counter>=lstLoanSchedule.size())
            {
                counter=0;
            }
            spnSchedullePostpone.setSelection(counter);
        }

    }

    public void saveSubmitData(String transactionStatus)
    {

        progressDialog= new ProgressDialog(LoanDetailPostPoneActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();
        boolean fullAccess=true;
        String accessibilityAttribute="";
        JSONObject paramObject= new JSONObject();
        try {

            paramObject.put("ID",id);
            paramObject.put("EmployeeID", GlobalVar.getEmployeeId());
            paramObject.put("EmployeeLoanBalanceID",  editLoan==null?loanBalanceID:editLoan.getEmployeeLoanBalanceID());
            paramObject.put("DecisionNumber",editLoan==null?null:editLoan.getDecisionNumber());
            paramObject.put("TransactionStatusID",editLoan==null?null:editLoan.getTransactionStatusID());
            paramObject.put("LoanTransactionTypeID",editLoan==null?transactionTypeID:editLoan.getLoanTransactionTypeID());
            paramObject.put("LoanTransactionType", id==null?null:spnTransactionTypePostpone.getSelectedItem().toString());
            paramObject.put("LoanPolicyID", editLoan==null?null:editLoan.getLoanPolicyID());

//            Date date=new Date();
//            String patternSQLServer = "yyyy-MM-dd'T'HH:mm:ss.sssssZ";
//            SimpleDateFormat formatTimeSQLServer = new SimpleDateFormat(patternSQLServer);

//            paramObject.put("StartNewLoanDate",editLoan==null?formatTimeSQLServer.format(date).toString():editLoan.getStartNewLoanDate()  );
            paramObject.put("StartNewLoanDate",editLoan==null?null:editLoan.getStartNewLoanDate()  );
            paramObject.put("CreditAmount",editLoan==null?null:editLoan.getCreditAmount());
            paramObject.put("EmployeeLoanScheduleID",employeeLoanScheduleID );
            paramObject.put("Amount", txtAmountPostpone.getText().toString());
            paramObject.put("Description", txtDescriptionPostpone.getText().toString());
            paramObject.put("LoanSettingName", editLoan==null?null:editLoan.getLoanSettingName());
            paramObject.put("Limit", editLoan==null?null:editLoan.getLimit());
            paramObject.put("FullAccess", fullAccess);
            paramObject.put("ExclusiveFields", exclusiveFields);
            paramObject.put("AccessibilityAttribute", accessibilityAttribute);

        }catch (Exception e)
        {

        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),paramObject.toString());
        apiInterface = APIClient.editDraftLeaveCancelation(GlobalVar.getToken()).create(APIInterfaceRest.class);
        apiInterface.saveExpeditePostpone(body, transactionStatus).enqueue(new Callback<MessageReturn>() {
            @Override
            public void onResponse(Call<MessageReturn> call, Response<MessageReturn> response) {

                if (response.body().isIsSucceed()) {
                    Toast.makeText(LoanDetailPostPoneActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoanDetailPostPoneActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                Intent intent=new Intent(LoanDetailPostPoneActivity.this, LoanTransactionActivity.class);
                startActivity(intent);

            }

            @Override
            public void onFailure(Call<MessageReturn> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoanDetailPostPoneActivity.this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void getData()
    {
        progressDialog= new ProgressDialog(LoanDetailPostPoneActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();

        apiInterface = APIClient.editDraftLeaveCancelation(GlobalVar.getToken()).create(APIInterfaceRest.class);
        apiInterface.editDraftLoan(id).enqueue(new Callback<EditDraftLoan>() {
            @Override
            public void onResponse(Call<EditDraftLoan> call, Response<EditDraftLoan> response) {

                if (response.body().getIsSucceed()) {
                    editLoan= response.body().getReturnValue();

                    txtAmountPostpone.setText((editLoan==null||editLoan.getAmount()==null)?0+"":editLoan.getAmount()+"");
                    txtDescriptionPostpone.setText(editLoan.getDescription());


                } else {

                    Toast.makeText(LoanDetailPostPoneActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                initSpinnerTransactionType();
                initSpinnerSchedule();
            }

            @Override
            public void onFailure(Call<EditDraftLoan> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoanDetailPostPoneActivity.this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean checkValidation()
    {
        if(spnTransactionTypePostpone.getSelectedItem().toString()==null)
        {
            txtErrorTransactionTypePostpone.setError("");
            txtErrorTransactionTypePostpone.setTextColor(Color.RED);//just to highlight that this is an error
            txtErrorTransactionTypePostpone.setText(" Please select transaction type");//changes the selected item text to this
            return false;
        }
        else if(spnSchedullePostpone.getSelectedItem().toString().matches(""))
        {
            txtErrorSchedulePostpone.setError("");
            txtErrorSchedulePostpone.setTextColor(Color.RED);//just to highlight that this is an error
            txtErrorSchedulePostpone.setText(" Please select schedule");//changes the selected item text to this
            return false;
        }
        else if(txtAmountPostpone.getText().toString().matches("")||txtAmountPostpone.getText().toString().matches("\\+")||txtAmountPostpone.getText().toString().matches("-"))
        {
            txtAmountPostpone.setError("Please fill amount");
            return false;
        }
        else if(transactionTypeID.matches("20")&&(Integer.parseInt(txtAmountPostpone.getText().toString())<=employeeScheduleAmount || Integer.parseInt(txtAmountPostpone.getText().toString())>Integer.parseInt(remainingLoan)))
        {
            txtAmountPostpone.setError("Please make sure amount between "+new StringConverter().numberFormat(employeeScheduleAmount+"") + " and "+new StringConverter().numberFormat(remainingLoan));
            return false;
        }
        else if(transactionTypeID.matches("30")&&Integer.parseInt(txtAmountPostpone.getText().toString())>=employeeScheduleAmount)
        {
            txtAmountPostpone.setError("Please make sure amount lower than or equal "+ new StringConverter().numberFormat(employeeScheduleAmount+"") );
            return false;
        }
        return true;
    }

}
