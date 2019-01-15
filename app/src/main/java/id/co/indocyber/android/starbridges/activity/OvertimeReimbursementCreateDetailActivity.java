package id.co.indocyber.android.starbridges.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.util.Calendar;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.model.MessageReturn.MessageReturn;
import id.co.indocyber.android.starbridges.model.OvertimeReimbursement.OvertimeReimbursementDetail;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.network.StringConverter;
import id.co.indocyber.android.starbridges.utility.Base64Util;
import id.co.indocyber.android.starbridges.utility.DateUtil;
import id.co.indocyber.android.starbridges.utility.FileUtil;
import id.co.indocyber.android.starbridges.utility.GlobalVar;
import id.co.indocyber.android.starbridges.utility.MessageUtil;
import id.co.indocyber.android.starbridges.utility.SessionManagement;
import id.co.indocyber.android.starbridges.utility.SetupUtil;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OvertimeReimbursementCreateDetailActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_PICK_ATTACHMENT = 1000;
    public static final int REQUEST_CODE_PERMISSION_STORAGE = 1001;

    public static final String PARAM_ID_HEADER = "idHeader";
    public static final String PARAM_PERIOD = "period";
    public static final String PARAM_OVERTIME_REIMBURSEMENT_DETAIL = "entertainReimbursementDetail";


    private String idHeader;

    private EditText txtOvertimeDate, txtOvertimeStart, txtOvertimeEnd, txtNotes, txtAmount;
    private TextView txtAttachment;
    private CheckBox cbxIsWeekend;
    private ImageView imgOvertimeDatePicker, imgOvertimeStartPicker, imgDeleteOvertimeStart, imgOvertimeEndPicker, imgDeleteOvertimeEnd;
    private Button btnCancel, btnSave, btnAttachmentUpload;

    private APIInterfaceRest apiInterface;
    private ProgressDialog progressDialog;


    SessionManagement session;

    OvertimeReimbursementDetail overtimeReimbursementDetail;

    private Calendar period;
    private Calendar timeStart;
    private Calendar timeEnd;
    private String base64;


    Calendar calendarSelectedDate = Calendar.getInstance();
    StringConverter stringConverter = new StringConverter();

    private String[] permissions = new String[] {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime_reimbursement_create_detail);

        session = new SessionManagement(getApplicationContext());

        initComponent();

        setupDownloadAttachment();
        checkStoragePermission();
        setupDatePicker();
        setupTimePicker();
        setupBtnSave();
        setupBtnCancel();
        setupBtnAttachment();
        setupCurrency();
        initData();

    }

    public void checkStoragePermission() {
        for(String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION_STORAGE);
            }
        }
    }

    private void setupDownloadAttachment() {
        findViewById(R.id.lblDownloadAttachment).setVisibility(View.GONE);
        findViewById(R.id.layoutAttachment).setVisibility(View.GONE);

        txtAttachment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txtAttachment.getText().toString().isEmpty())
                {
                    findViewById(R.id.lblDownloadAttachment).setVisibility(View.GONE);
                    findViewById(R.id.layoutAttachment).setVisibility(View.GONE);


                }
                else {
                    findViewById(R.id.lblDownloadAttachment).setVisibility(View.VISIBLE);
                    findViewById(R.id.layoutAttachment).setVisibility(View.VISIBLE);


                }

            }
        });
        findViewById(R.id.lblDownloadAttachment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Base64Util.saveBase64ToFile(getApplicationContext(), base64, txtAttachment.getText().toString());
            }
        });
    }


    private void setupCurrency() {
        SetupUtil.setupCurrency(txtAmount);
    }
    private void initComponent() {
        txtOvertimeDate = findViewById(R.id.txtOvertimeDate);
        txtOvertimeStart = findViewById(R.id.txtOvertimeStart);
        txtOvertimeEnd = findViewById(R.id.txtOvertimeEnd);
        cbxIsWeekend = findViewById(R.id.cbxIsWeekend);
        txtNotes = findViewById(R.id.txtNotes);
        txtAmount = findViewById(R.id.txtAmount);
        txtAttachment = findViewById(R.id.txtAttachment);
        imgOvertimeDatePicker = findViewById(R.id.imgOvertimeDatePicker);
        imgOvertimeStartPicker = findViewById(R.id.imgOvertimeStartPicker);
        imgDeleteOvertimeStart = findViewById(R.id.imgDeleteOvertimeStart);
        imgOvertimeEndPicker = findViewById(R.id.imgOvertimeEndPicker);
        imgDeleteOvertimeEnd = findViewById(R.id.imgDeleteOvertimeEnd);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnAttachmentUpload = findViewById(R.id.btnAttachmentUpload);

    }

    private void setupBtnAttachment() {
        btnAttachmentUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("file/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Attachment"), REQUEST_CODE_PICK_ATTACHMENT);
            }
        });
    }

    private void setupDatePicker() {
        imgOvertimeDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar maxPeriod = (Calendar) period.clone();
                DateUtil.setLastDayOfMonth(maxPeriod);
                DateUtil.setLastTimeOfDay(maxPeriod);

                Calendar minPeriod = (Calendar) period.clone();
                DateUtil.setFirstDayOfMonth(minPeriod);
                DateUtil.setFirstTimeOfDay(minPeriod);


                DatePickerDialog dpd = new DatePickerDialog(OvertimeReimbursementCreateDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendarSelectedDate.set(year, month, dayOfMonth);
                        txtOvertimeDate.setText(stringConverter.dateFormatDDMMYYYY(stringConverter.dateToString(calendarSelectedDate.getTime())));

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

    private void setupTimePicker() {
        imgOvertimeStartPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePicker = new TimePickerDialog(OvertimeReimbursementCreateDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            timeStart.set(Calendar.HOUR_OF_DAY, selectedHour);
                            timeStart.set(Calendar.MINUTE, selectedMinute);
                            txtOvertimeStart.setText(stringConverter.timeFormatHHMM(timeStart.getTime()));
                        }
//                    }, Integer.parseInt(startLeave.substring(0, 2)), Integer.parseInt(startLeave.substring(3, 5)), true);
                    }, timeStart.get(Calendar.HOUR_OF_DAY), timeStart.get(Calendar.MINUTE),true);


                timePicker.setTitle("Select Overtime Start");

                timePicker.show();
            }
        });

        imgOvertimeEndPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePicker = new TimePickerDialog(OvertimeReimbursementCreateDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeEnd.set(Calendar.HOUR_OF_DAY, selectedHour);
                        timeEnd.set(Calendar.MINUTE, selectedMinute);
                        txtOvertimeEnd.setText(stringConverter.timeFormatHHMM(timeEnd.getTime()));
                    }
//                    }, Integer.parseInt(startLeave.substring(0, 2)), Integer.parseInt(startLeave.substring(3, 5)), true);
                }, timeEnd.get(Calendar.HOUR_OF_DAY), timeEnd.get(Calendar.MINUTE),true);


                timePicker.setTitle("Select Overtime End");

                timePicker.show();
            }
        });

        imgDeleteOvertimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtOvertimeStart.setText("");
            }
        });

        imgDeleteOvertimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtOvertimeEnd.setText("");
            }
        });
    }

    private void setupBtnSave() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtOvertimeDate.getText().toString()))
                {
                    MessageUtil.showMessage(OvertimeReimbursementCreateDetailActivity.this,"Warning", "Mohon pilih Date terlebih dahulu");
                    return;
                }
                else if(TextUtils.isEmpty(txtAmount.getText().toString()))
                {
                    MessageUtil.showMessage(OvertimeReimbursementCreateDetailActivity.this,"Warning", "Mohon isi Amount terlebih dahulu");
                    return;
                }
                else if(TextUtils.isEmpty(txtNotes.getText().toString()))
                {
                    MessageUtil.showMessage(OvertimeReimbursementCreateDetailActivity.this,"Warning", "Mohon isi Notes terlebih dahulu");
                    return;
                }

                saveOvertimeReimbursementDetail();
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

    private void initData() {
        timeStart = Calendar.getInstance();
        timeEnd = Calendar.getInstance();
        idHeader = getIntent().getStringExtra(PARAM_ID_HEADER);
        period = Calendar.getInstance();
        period.setTimeInMillis(getIntent().getLongExtra(PARAM_PERIOD, 0));
        StringConverter stringConverter = new StringConverter();
        overtimeReimbursementDetail = (OvertimeReimbursementDetail) getIntent().getSerializableExtra(PARAM_OVERTIME_REIMBURSEMENT_DETAIL);
        if (overtimeReimbursementDetail == null) {
            overtimeReimbursementDetail = new OvertimeReimbursementDetail();
            calendarSelectedDate = (Calendar) period.clone();
        } else {
            calendarSelectedDate.setTime(stringConverter.dateToDate(overtimeReimbursementDetail.getDate()));
            txtOvertimeDate.setText(stringConverter.dateFormatDDMMYYYY(overtimeReimbursementDetail.getDate()));
            if(overtimeReimbursementDetail.getOvertimeFrom().equalsIgnoreCase("00:00:00") == false) {
                timeStart.setTime(stringConverter.timeStringToDate(overtimeReimbursementDetail.getOvertimeFrom()));
                txtOvertimeStart.setText(stringConverter.timeFormatHHMM(timeStart.getTime()));
            }
            if(overtimeReimbursementDetail.getOvertimeTo().equalsIgnoreCase("00:00:00") == false) {
                timeEnd.setTime(stringConverter.timeStringToDate(overtimeReimbursementDetail.getOvertimeTo()));
                txtOvertimeEnd.setText(stringConverter.timeFormatHHMM(timeEnd.getTime()));
            }
            cbxIsWeekend.setChecked(overtimeReimbursementDetail.getWeekend());

            txtAmount.setText(stringConverter.numberFormat(overtimeReimbursementDetail.getAmountDetail() + ""));
            txtNotes.setText(overtimeReimbursementDetail.getNotes());
            txtAttachment.setText(overtimeReimbursementDetail.getAttachmentFile());

        }

    }

    public void saveOvertimeReimbursementDetail() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();


        overtimeReimbursementDetail.setIdHeader(idHeader);
        overtimeReimbursementDetail.setId(overtimeReimbursementDetail.getId());
        overtimeReimbursementDetail.setDate(stringConverter.dateToString(calendarSelectedDate.getTime()));
        overtimeReimbursementDetail.setOvertimeFrom(txtOvertimeStart.getText().toString());
        overtimeReimbursementDetail.setOvertimeTo(txtOvertimeEnd.getText().toString());
        overtimeReimbursementDetail.setAmountDetail(Integer.parseInt(stringConverter.numberRemoveFormat(txtAmount.getText().toString())));
        overtimeReimbursementDetail.setNotes(txtNotes.getText().toString());
        overtimeReimbursementDetail.setWeekend(cbxIsWeekend.isChecked());
        overtimeReimbursementDetail.setAttachmentFile(base64);


        final APIInterfaceRest apiInterface = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(overtimeReimbursementDetail).toString());
        Call<MessageReturn> call = apiInterface.saveDetailOvertimeReimbursement(body);

        call.enqueue(new Callback<MessageReturn>() {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_ATTACHMENT && resultCode == RESULT_OK) {
            try {
                final Uri uri = data.getData();
                String path = FileUtil.getRealPathFromURI(this,uri);// uri.getPath();
                base64 = Base64Util.getBase64FromPath(path);

                File file = new File(path);
                txtAttachment.setText(file.getName());
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "You haven't picked Attachment", Toast.LENGTH_LONG).show();
        }
    }
}

