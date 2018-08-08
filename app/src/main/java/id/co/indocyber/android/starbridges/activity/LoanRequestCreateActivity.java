package id.co.indocyber.android.starbridges.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.model.EditDraftLoan.EditDraftLoan;
import id.co.indocyber.android.starbridges.model.LoanPolicy.LoanPolicy;
import id.co.indocyber.android.starbridges.model.LoanPolicy.ReturnValue;
import id.co.indocyber.android.starbridges.model.LoanSettingLimit.LoanSettingLimit;
import id.co.indocyber.android.starbridges.model.MessageReturn.MessageReturn;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.network.StringConverter;
import id.co.indocyber.android.starbridges.utility.GlobalVar;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoanRequestCreateActivity extends AppCompatActivity {

    TextView txtLoanSettingCreate, txtLimitCreate, txtErrorPolicyCreate;
    Spinner spnPolicyCreate;
    EditText txtStartDateCreate, txtAmountCreate, txtCreditAmountCreate, txtDescriptionCreate;
    ImageView imgStartDateCreate;
    Button btnSubmitCreate, btnSaveCreate, btnCancelCreate;

    ProgressDialog progressDialog;
    APIInterfaceRest apiInterface;

    List<ReturnValue> lstLoanPolicies;

    String loanPolicyId, employeeLoanScheduleID, id;

    List<Object> exclusiveFields;

    String creditAmount="";

    id.co.indocyber.android.starbridges.model.LoanSettingLimit.ReturnValue loanLimit;

    id.co.indocyber.android.starbridges.model.EditDraftLoan.ReturnValue editLoan;

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            myCalendar.set(Calendar.YEAR, i);
            myCalendar.set(Calendar.MONTH, i1);
            myCalendar.set(Calendar.DAY_OF_MONTH, i2);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txtStartDateCreate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_create);

        txtLoanSettingCreate=(TextView)findViewById(R.id.txtLoanSettingCreate);
        txtLimitCreate=(TextView)findViewById(R.id.txtLimitCreate);
        spnPolicyCreate=(Spinner)findViewById(R.id.spnPolicyCreate);
        txtStartDateCreate=(EditText)findViewById(R.id.txtStartDateCreate);
        txtAmountCreate=(EditText)findViewById(R.id.txtAmountCreate);
        txtCreditAmountCreate=(EditText)findViewById(R.id.txtCreditAmountCreate);
        txtDescriptionCreate=(EditText)findViewById(R.id.txtDescriptionCreate);
        imgStartDateCreate=(ImageView)findViewById(R.id.imgStartDateCreate);
        btnSubmitCreate=(Button)findViewById(R.id.btnSubmitCreate);
        btnSaveCreate=(Button)findViewById(R.id.btnSaveCreate);
        btnCancelCreate=(Button)findViewById(R.id.btnCancelCreate);
        txtErrorPolicyCreate=(TextView)findViewById(R.id.txtErrorPolicyCreate);

        id=getIntent().getStringExtra("ID");


        getLimitLoan();
        if(id!=null)
        {
            getData();
        }
        else
        {
            initSpinnerLoanPolicy();
        }

        imgStartDateCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog= new DatePickerDialog(LoanRequestCreateActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
//                dialog.getDatePicker();
                dialog.show();

            }
        });

        spnPolicyCreate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ReturnValue loanPolicy=(ReturnValue)spnPolicyCreate.getItemAtPosition(i);
                loanPolicyId=loanPolicy.getID()+"";

                if(loanPolicy.getCreditTypeID()!=null&&loanPolicy.getCreditTypeID()==20)
                {
                    txtCreditAmountCreate.setEnabled(true);
                    txtCreditAmountCreate.setText(creditAmount);
                }
                else
                {
                    txtCreditAmountCreate.setEnabled(false);
                    txtCreditAmountCreate.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSaveCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkValidation())
                {

                }
                else
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoanRequestCreateActivity.this);
                    alert.setTitle("Request Confirmation");
                    alert.setMessage("Policy\n" +
                            "\t"+spnPolicyCreate.getSelectedItem().toString()+"" +
                            "\nStart Date\n" +
                            "\t"+ new StringConverter().dateFormatInput2dMMMMYYYY(txtStartDateCreate.getText().toString()) +
                            "\nAmount\n" +
                            "\t"+new StringConverter().numberFormat(txtAmountCreate.getText().toString()) +
                            "\nCredit Amount\n" +
                            "\t"+new StringConverter().numberFormat(txtCreditAmountCreate.getText().toString())+
                            "\nDescription\n" +
                            "\t"+txtDescriptionCreate.getText().toString() +
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
        btnSubmitCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!checkValidation())
                {

                }
                else
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoanRequestCreateActivity.this);
                    alert.setTitle("Request Confirmation");
                    alert.setMessage("Policy\n" +
                            "\t"+spnPolicyCreate.getSelectedItem().toString()+"" +
                            "\nStart Date\n" +
                            "\t"+ new StringConverter().dateFormatInput2dMMMMYYYY(txtStartDateCreate.getText().toString()) +
                            "\nAmount\n" +
                            "\t"+new StringConverter().numberFormat(txtAmountCreate.getText().toString()) +
                            "\nCredit Amount\n" +
                            "\t"+new StringConverter().numberFormat(txtCreditAmountCreate.getText().toString())+
                            "\nDescription\n" +
                                    "\t"+txtDescriptionCreate.getText().toString() +
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

        btnCancelCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(LoanRequestCreateActivity.this);
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

        txtAmountCreate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b)
                {
                    if(!txtAmountCreate.getText().toString().matches("") &&  Integer.parseInt(txtAmountCreate.getText().toString())>Integer.parseInt(loanLimit.getLimit()))
                    {
                        txtAmountCreate.setError("loan greater than the limit, approval may take longer");

                    }
                }


            }
        });

    }

    public void getLimitLoan()
    {
        if(progressDialog==null||!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(LoanRequestCreateActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.show();
        }
        apiInterface = APIClient.editDraftLeaveCancelation(GlobalVar.getToken()).create(APIInterfaceRest.class);
        apiInterface.getLoanSettingLimit().enqueue(new Callback<LoanSettingLimit>() {
            @Override
            public void onResponse(Call<LoanSettingLimit> call, Response<LoanSettingLimit> response) {


                if (response.body().getIsSucceed()) {
                    loanLimit=response.body().getReturnValue();
                    txtLimitCreate.setText(new StringConverter().numberFormat(response.body().getReturnValue().getLimit()));
                    txtLoanSettingCreate.setText(response.body().getReturnValue().getNameLoanSetting());
                } else {
                    Toast.makeText(LoanRequestCreateActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<LoanSettingLimit> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoanRequestCreateActivity.this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void initSpinnerLoanPolicy()
    {
        if(progressDialog==null||!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(LoanRequestCreateActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.show();
        }

        lstLoanPolicies=new ArrayList<>();
        ReturnValue loanPolicy=new ReturnValue();
        lstLoanPolicies.add(loanPolicy);

        apiInterface = APIClient.editDraftLeaveCancelation(GlobalVar.getToken()).create(APIInterfaceRest.class);
        apiInterface.getLoanPolicy().enqueue(new Callback<LoanPolicy>() {
            @Override
            public void onResponse(Call<LoanPolicy> call, Response<LoanPolicy> response) {


                if (response.body().getIsSucceed()) {
                    lstLoanPolicies.addAll(response.body().getReturnValue());
//                    ReturnValue loanPolicy=new ReturnValue();
//                    loanPolicy.setID(100);
//                    loanPolicy.setName("Car Ownership Program");
//                    lstLoanPolicies.add(loanPolicy);
                    setupSpinnerLoanPolicy();

                } else {
                    Toast.makeText(LoanRequestCreateActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<LoanPolicy> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoanRequestCreateActivity.this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void setupSpinnerLoanPolicy()
    {
        ArrayAdapter<ReturnValue> adapter = new ArrayAdapter<ReturnValue>(LoanRequestCreateActivity.this, android.R.layout.simple_spinner_item, lstLoanPolicies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPolicyCreate.setAdapter(adapter);

        if(editLoan!=null)
        {
            int counter=0;
            for(id.co.indocyber.android.starbridges.model.LoanPolicy.ReturnValue decisionNumber:lstLoanPolicies)
            {
//                String sScheduleId=editLoan.getEmployeeLoanScheduleID()+"";

                if(editLoan.getLoanPolicyID()==decisionNumber.getID()) break;
                counter++;
            }
            if(counter>=lstLoanPolicies.size())
            {
                counter=0;
            }
            spnPolicyCreate.setSelection(counter);
        }
        progressDialog.dismiss();
    }

    public void saveSubmitData(String transactionStatus)
    {

        progressDialog= new ProgressDialog(LoanRequestCreateActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();
        boolean fullAccess=true;
        String accessibilityAttribute="";
        JSONObject paramObject= new JSONObject();
        try {

            paramObject.put("ID",id);
            paramObject.put("EmployeeID", GlobalVar.getEmployeeId());
            paramObject.put("EmployeeLoanBalanceID",  null);
            paramObject.put("DecisionNumber",editLoan==null?null:editLoan.getDecisionNumber());
            paramObject.put("TransactionStatusID",editLoan==null?null:editLoan.getTransactionStatusID());
            paramObject.put("LoanTransactionTypeID",editLoan==null?null:editLoan.getLoanTransactionTypeID());
            paramObject.put("LoanPolicyID", loanPolicyId);



            Date date=new Date();
            String patternSQLServer = "yyyy-MM-dd";
            String patternDate="dd/MM/yyyy";
            SimpleDateFormat formatTimeSQLServer = new SimpleDateFormat(patternSQLServer);
            SimpleDateFormat formatTimeStarbridge=new SimpleDateFormat(patternDate);
            Date dStartDate=new Date();
            String startDate="";
            try
            {
                dStartDate=formatTimeStarbridge.parse(txtStartDateCreate.getText().toString());
                startDate=formatTimeSQLServer.format(dStartDate);
            }
            catch (Exception e)
            {

            }

            paramObject.put("StartNewLoanDate",startDate);
            paramObject.put("CreditAmount",txtCreditAmountCreate.getText().toString());
            paramObject.put("EmployeeLoanScheduleID",employeeLoanScheduleID);
            paramObject.put("Amount", txtAmountCreate.getText().toString());
            paramObject.put("Description", txtDescriptionCreate.getText().toString());
            paramObject.put("LoanSettingName", loanLimit.getNameLoanSetting());
            paramObject.put("Limit", loanLimit.getLimit());
            paramObject.put("FullAccess", fullAccess);
            paramObject.put("ExclusiveFields", editLoan==null?null:editLoan.getExclusionFields());
            paramObject.put("AccessibilityAttribute", editLoan==null?null:editLoan.getAccessibilityAttribute());

        }catch (Exception e)
        {

        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),paramObject.toString());
        apiInterface = APIClient.editDraftLeaveCancelation(GlobalVar.getToken()).create(APIInterfaceRest.class);
        apiInterface.saveLoanRequest(body, transactionStatus).enqueue(new Callback<MessageReturn>() {
            @Override
            public void onResponse(Call<MessageReturn> call, Response<MessageReturn> response) {

                if (response.body().isIsSucceed()) {
                    Toast.makeText(LoanRequestCreateActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(LoanRequestCreateActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                Intent intent=new Intent(LoanRequestCreateActivity.this, LoanTransactionMainActivity.class);
                startActivity(intent);

            }

            @Override
            public void onFailure(Call<MessageReturn> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoanRequestCreateActivity.this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void getData()
    {
        if(progressDialog==null||!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(LoanRequestCreateActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.show();
        }
        apiInterface = APIClient.editDraftLeaveCancelation(GlobalVar.getToken()).create(APIInterfaceRest.class);
        apiInterface.editDraftLoan(id).enqueue(new Callback<EditDraftLoan>() {
            @Override
            public void onResponse(Call<EditDraftLoan> call, Response<EditDraftLoan> response) {

                if (response.body().getIsSucceed()) {
                    editLoan= response.body().getReturnValue();
//                    creditAmount=editLoan.getCreditAmount()+"";
                    txtStartDateCreate.setText(new StringConverter().dateFormatDDMMYYYY(editLoan.getStartNewLoanDate()));
                    txtAmountCreate.setText((editLoan==null||editLoan.getAmount()==null)?0+"":editLoan.getAmount()+"");
                    String tCreditAmount="";
                    if(editLoan!=null)
                    {
                        if(editLoan.getCreditAmount()!=null)
                        {
                            creditAmount=editLoan.getCreditAmount()+"";
                        }
                        else
                        {
                            creditAmount="";
                        }
                    }
                    txtDescriptionCreate.setText(editLoan.getDescription()+"");
                } else {

                    Toast.makeText(LoanRequestCreateActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                initSpinnerLoanPolicy();

            }

            @Override
            public void onFailure(Call<EditDraftLoan> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoanRequestCreateActivity.this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean checkValidation()
    {
        if(loanPolicyId.matches("null"))
        {
            txtErrorPolicyCreate.setError("");
            txtErrorPolicyCreate.setTextColor(Color.RED);//just to highlight that this is an error
            txtErrorPolicyCreate.setText(" Please select policy");//changes the selected item text to this
            return false;
        }
        else if(txtStartDateCreate.getText().toString().matches(""))
        {
            txtStartDateCreate.setError("Please fill start date");
            return false;
        }
        else if(txtAmountCreate.getText().toString().matches("")||txtAmountCreate.getText().toString().matches("\\+")||txtAmountCreate.getText().toString().matches("-"))
        {
            txtAmountCreate.setError("Please fill amount");
            return false;
        }
        else if(txtCreditAmountCreate.isEnabled()&&(txtCreditAmountCreate.getText().toString().matches("")||txtCreditAmountCreate.getText().toString().matches("\\+")||txtCreditAmountCreate.getText().toString().matches("-")))
        {
            txtCreditAmountCreate.setError("Please fill amount");
            return false;
        }
        return true;
    }

}
