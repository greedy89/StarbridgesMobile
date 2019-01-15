package id.co.indocyber.android.starbridges.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.util.Calendar;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.model.EntertainReimbursement.EntertainReimbursementDetail;
import id.co.indocyber.android.starbridges.model.MessageReturn.MessageReturn;
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

public class EntertainReimbursementCreateDetailActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_PICK_ATTACHMENT = 1000;
    public static final int REQUEST_CODE_PERMISSION_STORAGE = 1001;

    public static final String PARAM_ID_HEADER = "idHeader";
    public static final String PARAM_PERIOD = "period";
    public static final String PARAM_ENTERTAIN_REIMBURSEMENT_DETAIL = "entertainReimbursementDetail";


    private String idHeader;

    private EditText txtDate, txtEntertainPlace, txtProject, txtRecipientPosition, txtParticipant, txtRecipientCompanyName, txtRecipientBusinessType, txtAmount;
    private TextView txtAttachment;
    private ImageView imgDatePicker;
    private Button btnCancel, btnSave, btnAttachmentUpload;

    private APIInterfaceRest apiInterface;
    private ProgressDialog progressDialog;


    SessionManagement session;

    EntertainReimbursementDetail entertainReimbursementDetail;

    private Calendar period;
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
        setContentView(R.layout.activity_entertain_reimbursement_create_detail);

        session = new SessionManagement(getApplicationContext());

        initComponent();

        setupDownloadAttachment();
        checkStoragePermission();
        setupDatePicker();
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


    private void setupCurrency() {
        SetupUtil.setupCurrency(txtAmount);
    }

    private void initComponent() {
        txtAmount = findViewById(R.id.txtAmount);
        txtAttachment = findViewById(R.id.txtAttachment);
        txtDate = findViewById(R.id.txtDate);
        txtEntertainPlace = findViewById(R.id.txtEntertainPlace);
        txtParticipant = findViewById(R.id.txtParticipant);
        txtProject = findViewById(R.id.txtProject);
        txtRecipientBusinessType = findViewById(R.id.txtRecipientBusinessType);
        txtRecipientCompanyName = findViewById(R.id.txtRecipientCompanyName);
        txtRecipientPosition = findViewById(R.id.txtRecipientPosition);
        imgDatePicker = findViewById(R.id.imgDatePicker);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnAttachmentUpload = findViewById(R.id.btnAttachmentUpload);

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
        imgDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar maxPeriod = (Calendar) period.clone();
                DateUtil.setLastDayOfMonth(maxPeriod);
                DateUtil.setLastTimeOfDay(maxPeriod);
                maxPeriod.add(Calendar.MONTH, 1);


                Calendar minPeriod = (Calendar) period.clone();
                DateUtil.setFirstDayOfMonth(minPeriod);
                DateUtil.setFirstTimeOfDay(minPeriod);
                minPeriod.add(Calendar.MONTH, -1);



                DatePickerDialog dpd = new DatePickerDialog(EntertainReimbursementCreateDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                if (TextUtils.isEmpty(txtDate.getText().toString())) {
                    MessageUtil.showMessage(EntertainReimbursementCreateDetailActivity.this, "Warning", "Mohon pilih Date terlebih dahulu");
                    return;
                }
                else if (TextUtils.isEmpty(txtEntertainPlace.getText().toString())) {
                    MessageUtil.showMessage(EntertainReimbursementCreateDetailActivity.this, "Warning", "Mohon isi Entertain Place terlebih dahulu");
                    return;
                }
                else if (TextUtils.isEmpty(txtProject.getText().toString())) {
                    MessageUtil.showMessage(EntertainReimbursementCreateDetailActivity.this, "Warning", "Mohon isi Project terlebih dahulu");
                    return;
                }
                else if (TextUtils.isEmpty(txtRecipientPosition.getText().toString())) {
                    MessageUtil.showMessage(EntertainReimbursementCreateDetailActivity.this, "Warning", "Mohon isi Recipient Position terlebih dahulu");
                    return;
                }
                else if (TextUtils.isEmpty(txtParticipant.getText().toString())) {
                    MessageUtil.showMessage(EntertainReimbursementCreateDetailActivity.this, "Warning", "Mohon isi Participan terlebih dahulu");
                    return;
                }
                else if (TextUtils.isEmpty(txtRecipientCompanyName.getText().toString())) {
                    MessageUtil.showMessage(EntertainReimbursementCreateDetailActivity.this, "Warning", "Mohon isi Recipient Company Name terlebih dahulu");
                    return;
                }
                else if (TextUtils.isEmpty(txtRecipientBusinessType.getText().toString())) {
                    MessageUtil.showMessage(EntertainReimbursementCreateDetailActivity.this, "Warning", "Mohon isi Recipiet Business Type terlebih dahulu");
                    return;
                }
                else if (TextUtils.isEmpty(txtAmount.getText().toString())) {
                    MessageUtil.showMessage(EntertainReimbursementCreateDetailActivity.this, "Warning", "Mohon isi Amount terlebih dahulu");
                    return;
                }

                saveEntertainReimbursementDetail();
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
        idHeader = getIntent().getStringExtra(PARAM_ID_HEADER);
        period = Calendar.getInstance();
        period.setTimeInMillis(getIntent().getLongExtra(PARAM_PERIOD, 0));
        StringConverter stringConverter = new StringConverter();
        entertainReimbursementDetail = (EntertainReimbursementDetail) getIntent().getSerializableExtra(PARAM_ENTERTAIN_REIMBURSEMENT_DETAIL);
        if (entertainReimbursementDetail == null) {
            entertainReimbursementDetail = new EntertainReimbursementDetail();
            calendarSelectedDate = (Calendar) period.clone();
        } else {
            calendarSelectedDate.setTime(stringConverter.dateToDate(entertainReimbursementDetail.getDate()));
            txtDate.setText(stringConverter.dateFormatDDMMYYYY(entertainReimbursementDetail.getDate()));

            txtAmount.setText(stringConverter.numberFormat(entertainReimbursementDetail.getAmountDetail() + ""));
            txtRecipientPosition.setText(entertainReimbursementDetail.getPosition());
            txtEntertainPlace.setText(entertainReimbursementDetail.getEntertainPlace());
            txtProject.setText(entertainReimbursementDetail.getProject());
            txtParticipant.setText(entertainReimbursementDetail.getParticipant());
            txtRecipientCompanyName.setText(entertainReimbursementDetail.getCompanyName());
            txtRecipientBusinessType.setText(entertainReimbursementDetail.getBusinessType());
            txtAttachment.setText(entertainReimbursementDetail.getAttachmentFile());

        }

    }

    public void saveEntertainReimbursementDetail() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();


        entertainReimbursementDetail.setIdHeader(idHeader);
        entertainReimbursementDetail.setId(entertainReimbursementDetail.getId());
        entertainReimbursementDetail.setDate(stringConverter.dateToString(calendarSelectedDate.getTime()));
        entertainReimbursementDetail.setEntertainPlace(txtEntertainPlace.getText().toString());
        entertainReimbursementDetail.setProject(txtProject.getText().toString());
        entertainReimbursementDetail.setPosition(txtRecipientPosition.getText().toString());
        entertainReimbursementDetail.setParticipant(txtParticipant.getText().toString());
        entertainReimbursementDetail.setCompanyName(txtRecipientCompanyName.getText().toString());
        entertainReimbursementDetail.setBusinessType(txtRecipientBusinessType.getText().toString());
        entertainReimbursementDetail.setAmountDetail(Integer.parseInt(stringConverter.numberRemoveFormat(txtAmount.getText().toString())));
        entertainReimbursementDetail.setAttachmentFile(base64);


        final APIInterfaceRest apiInterface = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(entertainReimbursementDetail).toString());
        Call<MessageReturn> call = apiInterface.saveDetailEntertainReimbursement(body);

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
                String path = FileUtil.getRealPathFromURI(this, uri);// uri.getPath();
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




