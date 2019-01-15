package id.co.indocyber.android.starbridges.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.model.MessageReturn.MessageReturn;
import id.co.indocyber.android.starbridges.model.OLocation.OLocation;
import id.co.indocyber.android.starbridges.model.OLocation.ReturnValue;
import id.co.indocyber.android.starbridges.model.TransportReimbursementDetail.TransportReimbursementDetail;
import id.co.indocyber.android.starbridges.model.WebServiceResponseObject;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.network.StringConverter;
import id.co.indocyber.android.starbridges.utility.DateUtil;
import id.co.indocyber.android.starbridges.utility.GlobalVar;
import id.co.indocyber.android.starbridges.utility.MessageUtil;
import id.co.indocyber.android.starbridges.utility.SessionManagement;
import id.co.indocyber.android.starbridges.utility.SetupUtil;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportReimbursementCreateDetailActivity extends AppCompatActivity {

    public static final String PARAM_ID_HEADER = "idHeader";
    public static final String PARAM_PERIOD = "period";
    public static final String PARAM_TRANSPORT_REIMBURSEMENT_DETAIL = "entertainReimbursementDetail";


    private String idHeader;

    private EditText txtDate, txtNotes, txtAmount;
    private Spinner spnLocation;
    private ImageView imgDatePicker;
    private Button btnCancel, btnSave;

    private APIInterfaceRest apiInterface;
    private ProgressDialog progressDialog;

    final List<ReturnValue> listReturnValue = new ArrayList<>();

    List<ReturnValue> LocItems;
    SessionManagement session;

    TransportReimbursementDetail transportReimbursementDetail;

    private Calendar period;

    Calendar calendarSelectedDate = Calendar.getInstance();
    StringConverter stringConverter = new StringConverter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_reimbursement_create_detail);

        session = new SessionManagement(getApplicationContext());

        initComponent();
        setupDatePicker();
        setupBtnSave();
        setupBtnCancel();
        setupCurrency();
        setupSpinnerLoc();

        initData();
        initSpinnerLoc();
    }

    private void initComponent() {
        txtDate = findViewById(R.id.txtDate);
        spnLocation = findViewById(R.id.spnLocation);
        txtNotes = findViewById(R.id.txtNotes);
        txtAmount = findViewById(R.id.txtAmount);
        imgDatePicker = findViewById(R.id.imgDatePicker);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

    }

    private void setupDatePicker() {
        imgDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*new DatePickerDialog(TransportReimbursementCreateDetailActivity.this, date, calendarSelectedDate
                        .get(Calendar.YEAR), calendarSelectedDate.get(Calendar.MONTH),
                        calendarSelectedDate.get(Calendar.DAY_OF_MONTH)).show();*/
                Calendar maxPeriod = (Calendar) period.clone();
                DateUtil.setLastDayOfMonth(maxPeriod);
                DateUtil.setLastTimeOfDay(maxPeriod);

                Calendar minPeriod = (Calendar) period.clone();
                DateUtil.setFirstDayOfMonth(minPeriod);
                DateUtil.setFirstTimeOfDay(minPeriod);


                DatePickerDialog dpd = new DatePickerDialog(TransportReimbursementCreateDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendarSelectedDate.set(year, month, dayOfMonth);
                        txtDate.setText(stringConverter.dateFormatDDMMYYYY(stringConverter.dateToString(calendarSelectedDate.getTime())));

                    }
                }, calendarSelectedDate.get(Calendar.YEAR), calendarSelectedDate.get(Calendar.MONTH), calendarSelectedDate.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setMinDate(minPeriod.getTimeInMillis());
                dpd.getDatePicker().setMaxDate(maxPeriod.getTimeInMillis());
                dpd.show();
            }
        });
        /*DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendarSelectedDate.set(Calendar.YEAR, i);
                calendarSelectedDate.set(Calendar.MONTH, i1);
                calendarSelectedDate.set(Calendar.DAY_OF_MONTH, i2);
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


                // set text to comp date
                txtDate.setName(sdf.format(calendarSelectedDate.getTime()));



            }
        };*/

    }

    private void setupBtnSave() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtDate.getText().toString()))
                {
                    MessageUtil.showMessage(TransportReimbursementCreateDetailActivity.this,"Warning", "Mohon pilih Date terlebih dahulu");
                    return;
                }
                else if(TextUtils.isEmpty(txtAmount.getText().toString()))
                {
                    MessageUtil.showMessage(TransportReimbursementCreateDetailActivity.this,"Warning", "Mohon isi Amount terlebih dahulu");
                    return;
                }
                else if(TextUtils.isEmpty(txtNotes.getText().toString()))
                {
                    MessageUtil.showMessage(TransportReimbursementCreateDetailActivity.this,"Warning", "Mohon isi Notes terlebih dahulu");
                    return;
                }
                else if(TextUtils.isEmpty (((ReturnValue)spnLocation.getSelectedItem()).getID()))
                {
                    MessageUtil.showMessage(TransportReimbursementCreateDetailActivity.this,"Warning", "Mohon pilih location terlebih dahulu");
                    return;
                }
                saveTransportReimbursementDetail();
            }
        });
    }

    private void setupBtnCancel() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void setupCurrency() {
        SetupUtil.setupCurrency(txtAmount);
    }
    private void initData() {
        idHeader = getIntent().getStringExtra(PARAM_ID_HEADER);
        period = Calendar.getInstance();
        period.setTimeInMillis(getIntent().getLongExtra(PARAM_PERIOD, 0));

        StringConverter stringConverter = new StringConverter();
        transportReimbursementDetail = (TransportReimbursementDetail) getIntent().getSerializableExtra(PARAM_TRANSPORT_REIMBURSEMENT_DETAIL);
        if (transportReimbursementDetail == null) {
            transportReimbursementDetail = new TransportReimbursementDetail();
            //txtAmount.setText(stringConverter.numberFormat("35000"));
            calendarSelectedDate = (Calendar) period.clone();

        } else {

            txtDate.setText(stringConverter.dateFormatDDMMYYYY(transportReimbursementDetail.getDate()));
            txtAmount.setText(stringConverter.numberFormat(transportReimbursementDetail.getAmountDetail() + ""));
            txtNotes.setText(transportReimbursementDetail.getNotes());
            calendarSelectedDate.setTime(stringConverter.dateToDate(transportReimbursementDetail.getDate()));

        }

    }

    public void saveTransportReimbursementDetail() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();


        transportReimbursementDetail.setIdHeader(idHeader);
        transportReimbursementDetail.setId(transportReimbursementDetail.getId());
        transportReimbursementDetail.setDate(stringConverter.dateToString(calendarSelectedDate.getTime()));
        transportReimbursementDetail.setAmountDetail(Integer.parseInt(stringConverter.numberRemoveFormat(txtAmount.getText().toString())));
        ReturnValue location = (ReturnValue) spnLocation.getSelectedItem();
        transportReimbursementDetail.setLocationId(Integer.parseInt(location.getID()));
        transportReimbursementDetail.setLocation(location.getName());
        transportReimbursementDetail.setNotes(txtNotes.getText().toString());


        final APIInterfaceRest apiInterface = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(transportReimbursementDetail).toString());
        Call<MessageReturn> call3 = apiInterface.saveDetailTransportReimbursement(body);

        call3.enqueue(new Callback<MessageReturn>() {
            @Override
            public void onResponse(Call<MessageReturn> call, Response<MessageReturn> response) {
                progressDialog.dismiss();
                MessageReturn data = response.body();
                if (data != null) {
                    Toast.makeText(getApplicationContext(), data.getMessage(), Toast.LENGTH_LONG).show();

                } else
                    Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();

            }

            @Override
            public void onFailure(Call<MessageReturn> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }

    private void setupSpinnerLoc() {
        spnLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(view != null) {
                    ReturnValue returnValue = listReturnValue.get(position);
                    if(returnValue.getID().equalsIgnoreCase(transportReimbursementDetail.getLocationId()+"") == false) {
                        getTransportReimbursementTransportAmount(returnValue.getID());
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getTransportReimbursementTransportAmount(String locationId) {
        progressDialog = new ProgressDialog(TransportReimbursementCreateDetailActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();


        apiInterface = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
        apiInterface.getTransportReimbursementTransportAmount(locationId).enqueue(new Callback<WebServiceResponseObject<String>>() {
            @Override
            public void onResponse(Call<WebServiceResponseObject<String>> call, Response<WebServiceResponseObject<String>> response) {

                if (response.isSuccessful()) {
                    WebServiceResponseObject<String> data = response.body();
                    if(TextUtils.isEmpty( data.getReturnValue()) == false) {
                        txtAmount.setText(stringConverter.numberFormat(data.getReturnValue()));
                    }
                } else {

                    Toast.makeText(TransportReimbursementCreateDetailActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }


                //else mLocationNameView.setName(location);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<WebServiceResponseObject<String>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransportReimbursementCreateDetailActivity.this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void initSpinnerLoc() {

        progressDialog = new ProgressDialog(TransportReimbursementCreateDetailActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ReturnValue returnValue = new ReturnValue();
        returnValue.setID("");
        returnValue.setAddress("");
        returnValue.setCode("");
        returnValue.setDescription("");
        returnValue.setName("--other--");
        listReturnValue.add(returnValue);

        apiInterface = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
        apiInterface.postLocation().enqueue(new Callback<OLocation>() {
            @Override
            public void onResponse(Call<OLocation> call, Response<OLocation> response) {

                if (response.isSuccessful()) {

                    LocItems = response.body().getReturnValue();
                    if (LocItems != null) {
                        listReturnValue.addAll(LocItems);
                    } else {
                        Toast.makeText(TransportReimbursementCreateDetailActivity.this, "spinner Tidak dapat data", Toast.LENGTH_LONG).show();
                    }

                    ArrayAdapter<ReturnValue> adapter = new ArrayAdapter<ReturnValue>(TransportReimbursementCreateDetailActivity.this,
                            android.R.layout.simple_spinner_item, listReturnValue);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnLocation.setAdapter(adapter);
                    if (transportReimbursementDetail != null) {
                        int i = 0;
                        for (ReturnValue returnValue1 : listReturnValue) {
                            if (returnValue1.getID().equalsIgnoreCase(transportReimbursementDetail.getLocationId() + "")) {
                                spnLocation.setSelection(i);
                                break;
                            }
                            i++;
                        }
                    }
                } else {

                    Toast.makeText(TransportReimbursementCreateDetailActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }


                //else mLocationNameView.setName(location);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<OLocation> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransportReimbursementCreateDetailActivity.this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();

            }
        });

    }


}

