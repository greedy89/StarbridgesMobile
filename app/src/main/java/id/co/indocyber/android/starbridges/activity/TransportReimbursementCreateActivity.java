package id.co.indocyber.android.starbridges.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.co.indocyber.android.starbridges.Manifest;
import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.adapter.TransportReimbursementDetailAdapter;
import id.co.indocyber.android.starbridges.interfaces.OnItemClickListener;
import id.co.indocyber.android.starbridges.interfaces.OnItemLongClickListener;
import id.co.indocyber.android.starbridges.library.PeriodPicker;
import id.co.indocyber.android.starbridges.model.MessageReturn.MessageReturn;
import id.co.indocyber.android.starbridges.model.TransportReimbursement.TransportReimbursementViewModel;
import id.co.indocyber.android.starbridges.model.TransportReimbursementDetail.TransportReimbursementDetail;
import id.co.indocyber.android.starbridges.model.TransportReimbursementType.TransportReimbursementType;
import id.co.indocyber.android.starbridges.model.WebServiceResponseList;
import id.co.indocyber.android.starbridges.model.WebServiceResponseObject;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.network.StringConverter;
import id.co.indocyber.android.starbridges.utility.Base64Util;
import id.co.indocyber.android.starbridges.utility.DateUtil;
import id.co.indocyber.android.starbridges.utility.GlobalVar;
import id.co.indocyber.android.starbridges.utility.MessageUtil;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportReimbursementCreateActivity extends AppCompatActivity {

    String TAG = "TransportReimbursementCreateActivity";

    public static final String PARAM_HEADER_ID = "headerId";
    public static final int REQUEST_CODE_PICK_ATTACHMENT = 1000;
    public static final int REQUEST_CODE_PERMISSION_STORAGE = 1001;
    public static final String PARAM_TRANSPORT_REIMBURSEMENT = "transportReimbursement";

    public TransportReimbursementViewModel transportReimbursementViewModel;

    private EditText txtTotalAmount, txtDescription, txtPeriod ;
    private TextView txtAttachment;
    private Spinner spnReimbursementType;
    private RecyclerView lstDetail;
    private FloatingActionButton fabAddDetail;
    private ImageView imgPeriodPicker;
    private TransportReimbursementDetailAdapter viewAdapter;
    private ProgressDialog progressDialog;
    private Button btnAttachmentUpload, btnSave, btnSubmit, btnGenerate, btnCancel;
    private APIInterfaceRest apiInterface;


    StringConverter stringConverter;
    private String base64;
    Calendar period;

    List<TransportReimbursementType> transportReimbursementTypes;

    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback = new ActionModeCallback();

    private String[] permissions = new String[] {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };
    //private String headerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_reimbursement_create);

        setTitle("Detail");
        transportReimbursementViewModel = new TransportReimbursementViewModel();
        stringConverter = new StringConverter();
        apiInterface = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);

        initComponent();
        setupDownloadAttachment();
        checkStoragePermission();
        setupBtnGenerate();
        setupBtnSave();
        setupBtnSubmit();
        setupBtnCancel();
        setupBtnAttachment();

        setupMonthYearPicker();
        setupFAB();
        setupListView();

        //setupMultipleChoiceListView();

        setupSpinnerReimbursementType();

        //initData();



    }

    public void checkStoragePermission() {
        for(String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION_STORAGE);
            }
        }
    }


    private void setupListView() {

        lstDetail.setNestedScrollingEnabled(false);
        lstDetail.setItemAnimator(new DefaultItemAnimator());
        lstDetail.setLayoutManager(new LinearLayoutManager(this));
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




    private void initComponent() {
        txtPeriod = findViewById(R.id.txtPeriod);
        imgPeriodPicker = findViewById(R.id.imgPeriodPicker);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        txtDescription = findViewById(R.id.txtDescription);
        txtAttachment = findViewById(R.id.txtAttachment);
        spnReimbursementType = findViewById(R.id.spnReimbursementType);
        lstDetail = findViewById(R.id.lstDetail);
        fabAddDetail = findViewById(R.id.fabAddDetail);
        btnAttachmentUpload = findViewById(R.id.btnAttachmentUpload);
        btnGenerate = findViewById(R.id.btnGenerate);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnSubmit = findViewById(R.id.btnSubmit);


        txtTotalAmount.setEnabled(false);
        spnReimbursementType.setEnabled(false);
        txtAttachment.setEnabled(false);

    }

    private void setupClickItemListView() {
        viewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(actionMode != null) {
                    toggleSelection(position);
                }
                else {
                    Bundle bundle = new Bundle();
                    bundle.putString(TransportReimbursementCreateDetailActivity.PARAM_ID_HEADER, transportReimbursementViewModel.getHeaderID());
                    bundle.putLong(TransportReimbursementCreateDetailActivity.PARAM_PERIOD, period.getTimeInMillis());
                    bundle.putSerializable(TransportReimbursementCreateDetailActivity.PARAM_TRANSPORT_REIMBURSEMENT_DETAIL, transportReimbursementViewModel.getListDetail().get(position));
                    Intent intent = new Intent(TransportReimbursementCreateActivity.this, TransportReimbursementCreateDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    private void initData() {
        String id = getIntent().getStringExtra(PARAM_HEADER_ID);
        if (id != null) {
            editTransportReimbursementDraft(id);
            imgPeriodPicker.setEnabled(false);
        }
    }
    private void setupMultipleChoiceListView() {
        viewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onLongItemClick(int position) {
                if(actionMode == null) {
                    actionMode = startSupportActionMode(actionModeCallback);
                }
                toggleSelection(position);
                return true;
            }
        });
    }

    private void toggleSelection(int position) {
        viewAdapter.toggleSelection(position);
        int count = viewAdapter.getSelectedCount();
        if(count == 0) {
            actionMode.finish();
        }
        else  {
            actionMode.setTitle(count + " Selected");
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {


        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_draft_correction, menu);
            return true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.deleteDraft:
                    AlertDialog.Builder alert = new AlertDialog.Builder(TransportReimbursementCreateActivity.this);
                    alert.setTitle("Confirmation");
                    alert.setTitle("This information will be deleted");

                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // Calls getSelectedIds method from ListViewAdapter Class
                            final SparseBooleanArray selected = viewAdapter
                                    .getSelectedIds();
//                                idSelected= SharedPreferenceUtils.getSetting(DraftCorrectionListActivity.this, "lstIdSelected", "");
                            List<String> listSelectedId = new ArrayList<>();
                            // Captures all selected ids with a loop
                            for (int i2 = (selected.size() - 1); i2 >= 0; i2--) {
                                if (selected.valueAt(i2)) {
                                    TransportReimbursementDetail selecteditem = viewAdapter
                                            .getItem(selected.keyAt(i2));
                                    // Remove selected items following the ids
                                    listSelectedId.add(selecteditem.getId());
                                    ///viewAdapter.remove(selecteditem);
                                }
                            }
                            String selectedId = listSelectedId.toString();
                            Log.d("lstIdSelected", selectedId);
                            deleteCheckedDraft(selectedId);
                            // Close CAB
                            mode.finish();

                        }
                    });

                    alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    alert.show();


                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            viewAdapter.removeSelection();
            actionMode = null;
        }
    }

    /*private void setupMultipleChoiceListView() {
        ViewCompat.setNestedScrollingEnabled(lstDetail, true);
        lstDetail.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lstDetail.setItemsCanFocus(true);
        lstDetail.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

                // Capture total checked items
                final int checkedCount = lstDetail.getCheckedItemCount();
                // Set the CAB title according to total checked items
                actionMode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                viewAdapter.toggleSelection(i);


            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.menu_draft_correction, menu);
                return true;

            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;

            }

            @Override
            public boolean onActionItemClicked(final ActionMode actionMode, MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.deleteDraft:
                        AlertDialog.Builder alert = new AlertDialog.Builder(TransportReimbursementCreateActivity.this);
                        alert.setTitle("Confirmation");
                        alert.setTitle("This information will be deleted");

                        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // Calls getSelectedIds method from ListViewAdapter Class
                                final SparseBooleanArray selected = viewAdapter
                                        .getSelectedIds();
//                                idSelected= SharedPreferenceUtils.getSetting(DraftCorrectionListActivity.this, "lstIdSelected", "");
                                List<String> listSelectedId = new ArrayList<>();
                                // Captures all selected ids with a loop
                                for (int i2 = (selected.size() - 1); i2 >= 0; i2--) {
                                    if (selected.valueAt(i2)) {
                                        TransportReimbursementDetail selecteditem = viewAdapter
                                                .getItem(selected.keyAt(i2));
                                        // Remove selected items following the ids
                                        listSelectedId.add(selecteditem.getId());
                                        viewAdapter.remove(selecteditem);
                                    }
                                }
                                String selectedId = listSelectedId.toString();
                                Log.d("lstIdSelected", selectedId);
                                deleteCheckedDraft(selectedId);
                                // Close CAB
                                actionMode.finish();

                            }
                        });

                        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        alert.show();


                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

                viewAdapter.removeSelection();

            }
        });

    }
*/
    private void setupBtnGenerate() {
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( TextUtils.isEmpty(transportReimbursementViewModel.getHeaderID())) {
                    saveTransactionGetId("Save", "GENERATE");
                }
                else {
                    generateClaimTransportReimbursement();
                }
            }
        });
    }

    private void generateClaimTransportReimbursement() {
        if(validaingAndSetViewModel() == false)
            return;
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(transportReimbursementViewModel).toString());

        Call<WebServiceResponseObject<String>> call = apiInterface.generateClaimransportReimbursement(body);
        call.enqueue(new Callback<WebServiceResponseObject<String>>() {
            @Override
            public void onResponse(Call<WebServiceResponseObject<String>> call, Response<WebServiceResponseObject<String>> response) {
                progressDialog.dismiss();
                WebServiceResponseObject<String> data = response.body();
                if (data != null && data.getIsSucceed()) {
                    transportReimbursementViewModel.setHeaderID(data.getReturnValue());
                    getReimbursementDetail(transportReimbursementViewModel.getHeaderID());
                } else {
                    Toast.makeText(TransportReimbursementCreateActivity.this, "Failed to save data"
                            , Toast.LENGTH_LONG).show();

                }
                //getReimbursementDetail(transportReimbursementViewModel.getHeaderID());



            }

            @Override
            public void onFailure(Call<WebServiceResponseObject<String>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    private void setupBtnSave() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestConfirmation("Save");

            }
        });

    }

    private void setupBtnSubmit() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestConfirmation("Submit");

            }
        });


    }

    private void setupBtnCancel() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(TransportReimbursementCreateActivity.this);
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

    private void setupFAB() {
        fabAddDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( TextUtils.isEmpty(transportReimbursementViewModel.getHeaderID())) {
                    saveTransactionGetId("Save", "DETAIL");
                }
                else {
                    openAddDetailActivity();
                }

            }
        });
    }

    private void openAddDetailActivity() {
        Bundle bundle = new Bundle();
        bundle.putString(TransportReimbursementCreateDetailActivity.PARAM_ID_HEADER, transportReimbursementViewModel.getHeaderID());
        bundle.putLong(TransportReimbursementCreateDetailActivity.PARAM_PERIOD, period.getTimeInMillis());
        Intent intent = new Intent(TransportReimbursementCreateActivity.this, TransportReimbursementCreateDetailActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void setTransportReimbursementEditData() {
        if (transportReimbursementViewModel != null) {
            StringConverter stringConverter = new StringConverter();

            period.setTime(stringConverter.dateToDate(transportReimbursementViewModel.getPeriod()));
            base64 = transportReimbursementViewModel.getAttachmentFile();
            if(TextUtils.isEmpty(base64) == false) {
                txtAttachment.setText(transportReimbursementViewModel.getHeaderID()+".png");
            }
            transportReimbursementViewModel.setAttachmentFile(null);

            DateUtil.setFirstDayOfMonth(period);
            DateUtil.setFirstTimeOfDay(period);
            txtPeriod.setText(stringConverter.dateFormatMMMMYYYY(transportReimbursementViewModel.getPeriod()));

            txtDescription.setText(transportReimbursementViewModel.getDescription());
            //txtTotalAmount.setName(stringConverter.numberFormat( entertainReimbursementViewModel.getAmount()+""));
            int i = 0;
            for (TransportReimbursementType item : transportReimbursementTypes) {
                if (item.getValue().equalsIgnoreCase(transportReimbursementViewModel.getReimbursementTypeID() + "")) {
                    spnReimbursementType.setSelection(i);
                    break;
                }
                i++;
            }

        }
    }

    private Boolean validaingAndSetViewModel() {
        if(TextUtils.isEmpty (txtPeriod.getText().toString()))
        {
            MessageUtil.showMessage(this,"Warning", "Mohon pilih period terlebih dahulu");
            return false;
        }
        else if(TextUtils.isEmpty (txtDescription.getText().toString()))
        {
            MessageUtil.showMessage(this,"Warning", "Mohon isi description terlebih dahulu");
            return false;
        }

        if (transportReimbursementViewModel == null) {
            transportReimbursementViewModel = new TransportReimbursementViewModel();
        }
        transportReimbursementViewModel.setDescription(txtDescription.getText().toString());
        String amount = txtTotalAmount.getText().toString();
        if (TextUtils.isEmpty(amount)) {
            amount = "0";
        }
        transportReimbursementViewModel.setAmount(Integer.parseInt(stringConverter.numberRemoveFormat(amount)));

        transportReimbursementViewModel.setPeriod(stringConverter.dateToString(period.getTime()));
        TransportReimbursementType transportReimbursementType = (TransportReimbursementType) spnReimbursementType.getSelectedItem();
        transportReimbursementViewModel.setReimbursementType(transportReimbursementType.getText());
        transportReimbursementViewModel.setReimbursementTypeID(Integer.parseInt(transportReimbursementType.getValue()));
        transportReimbursementViewModel.setEmployeeID(GlobalVar.getEmployeeId());
        //entertainReimbursementViewModel.setListDetail();

        return true;
    }

    private void requestConfirmation(final String transactionStatus) {

        if(validaingAndSetViewModel() == false)
            return;

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        transportReimbursementViewModel.setAttachmentFile(null);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(transportReimbursementViewModel).toString());

        Call<WebServiceResponseObject<TransportReimbursementViewModel>> call = apiInterface.confirmationTransportReimbursement(transactionStatus, body);
        call.enqueue(new Callback<WebServiceResponseObject<TransportReimbursementViewModel>>() {
            @Override
            public void onResponse(Call<WebServiceResponseObject<TransportReimbursementViewModel>> call, Response<WebServiceResponseObject<TransportReimbursementViewModel>> response) {
                progressDialog.dismiss();
                WebServiceResponseObject<TransportReimbursementViewModel> data = response.body();
                if (data != null && data.getIsSucceed()) {
                    transportReimbursementViewModel = data.getReturnValue();

                    AlertDialog.Builder alert = new AlertDialog.Builder(TransportReimbursementCreateActivity.this);
                    alert.setTitle("Request Confirmation");
                    alert.setMessage(
                            ""
                    );
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveTransaction(transactionStatus);
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
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(TransportReimbursementCreateActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(TransportReimbursementCreateActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }


                //entertainReimbursementViewModel.setListDetail(data.getReturnValue());

            }

            @Override
            public void onFailure(Call<WebServiceResponseObject<TransportReimbursementViewModel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    private void saveTransaction(final String transactionStatus) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        transportReimbursementViewModel.setAttachmentFile(base64);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(transportReimbursementViewModel).toString());

        Call<WebServiceResponseObject<String>> call = apiInterface.saveTransportReimbursement(body);
        call.enqueue(new Callback<WebServiceResponseObject<String>>() {
            @Override
            public void onResponse(Call<WebServiceResponseObject<String>> call, Response<WebServiceResponseObject<String>> response) {
                progressDialog.dismiss();
                WebServiceResponseObject<String> data = response.body();
                if (data != null && data.getIsSucceed()) {
                    Toast.makeText(TransportReimbursementCreateActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(TransportReimbursementCreateActivity.this, "Failed to save data"
                            , Toast.LENGTH_LONG).show();

                }



            }

            @Override
            public void onFailure(Call<WebServiceResponseObject<String>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
    private void saveTransactionGetId(final String transactionStatus, final String nextStep) {
        if(validaingAndSetViewModel() == false)
            return;
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(transportReimbursementViewModel).toString());

    Call<WebServiceResponseObject<String>> call = apiInterface.saveTransportReimbursement(body);
        call.enqueue(new Callback<WebServiceResponseObject<String>>() {
        @Override
        public void onResponse(Call<WebServiceResponseObject<String>> call, Response<WebServiceResponseObject<String>> response) {
            progressDialog.dismiss();
            WebServiceResponseObject<String> data = response.body();
            if (data != null && data.getIsSucceed()) {
                transportReimbursementViewModel.setHeaderID(data.getReturnValue());
                if(nextStep.equalsIgnoreCase("DETAIL"))  {
                    openAddDetailActivity();
                }
                else if(nextStep.equalsIgnoreCase("GENERATE")) {
                    generateClaimTransportReimbursement();
                }
            } else {
                Toast.makeText(TransportReimbursementCreateActivity.this, "Failed to save data"
                        , Toast.LENGTH_LONG).show();

            }



        }

        @Override
        public void onFailure(Call<WebServiceResponseObject<String>> call, Throwable t) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
            call.cancel();
        }
    });
}

    private void setupSpinnerReimbursementType() {
        apiInterface = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
        apiInterface.getTransportReimbursementType().enqueue(new Callback<WebServiceResponseList<TransportReimbursementType>>() {
            @Override
            public void onResponse(Call<WebServiceResponseList<TransportReimbursementType>> call, Response<WebServiceResponseList<TransportReimbursementType>> response) {

                if (response.isSuccessful()) {

                    transportReimbursementTypes = response.body().getReturnValue();
                    if(transportReimbursementTypes == null || transportReimbursementTypes.size() == 0)
                    {
                        Toast.makeText(TransportReimbursementCreateActivity.this, "spinner Tidak dapat data", Toast.LENGTH_LONG).show();
                    }
                    else {
                        ArrayAdapter<TransportReimbursementType> adapter = new ArrayAdapter<TransportReimbursementType>(TransportReimbursementCreateActivity.this,
                                android.R.layout.simple_spinner_item, transportReimbursementTypes);

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spnReimbursementType.setAdapter(adapter);
                    }
                    initData();
                } else {

                    Toast.makeText(TransportReimbursementCreateActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<WebServiceResponseList<TransportReimbursementType>> call, Throwable t) {
                Toast.makeText(TransportReimbursementCreateActivity.this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void editTransportReimbursementDraft(String id) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<WebServiceResponseObject<TransportReimbursementViewModel>> call = apiInterface.editTransportReimbursementDraft(id);
        call.enqueue(new Callback<WebServiceResponseObject<TransportReimbursementViewModel>>() {
            @Override
            public void onResponse(Call<WebServiceResponseObject<TransportReimbursementViewModel>> call, Response<WebServiceResponseObject<TransportReimbursementViewModel>> response) {
                progressDialog.dismiss();
                WebServiceResponseObject<TransportReimbursementViewModel> data = response.body();
                if (data != null && data.getIsSucceed()) {
                    transportReimbursementViewModel = data.getReturnValue();
                    setTransportReimbursementEditData();
                    getReimbursementDetail(transportReimbursementViewModel.getHeaderID());


                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(TransportReimbursementCreateActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(TransportReimbursementCreateActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }



            }

            @Override
            public void onFailure(Call<WebServiceResponseObject<TransportReimbursementViewModel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void getReimbursementDetail(String id) {
        if(TextUtils.isEmpty(id))
            return;

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<WebServiceResponseList<TransportReimbursementDetail>> call = apiInterface.getTransportReimbursementDetail(id);
        call.enqueue(new Callback<WebServiceResponseList<TransportReimbursementDetail>>() {
            @Override
            public void onResponse(Call<WebServiceResponseList<TransportReimbursementDetail>> call, Response<WebServiceResponseList<TransportReimbursementDetail>> response) {
                progressDialog.dismiss();
                WebServiceResponseList<TransportReimbursementDetail> data = response.body();
                transportReimbursementViewModel.setListDetail(data.getReturnValue());
                if (data != null && data.getIsSucceed()) {
                    viewAdapter = new TransportReimbursementDetailAdapter(TransportReimbursementCreateActivity.this, R.layout.lst_transport_reimbursement_detail, transportReimbursementViewModel.getListDetail());
                    setupClickItemListView();
                    setupMultipleChoiceListView();
                    lstDetail.setAdapter(viewAdapter);
                    setTotalAmount(transportReimbursementViewModel);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(TransportReimbursementCreateActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(TransportReimbursementCreateActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<WebServiceResponseList<TransportReimbursementDetail>> call, Throwable t) {
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
                String path = getRealPathFromURI(uri);// uri.getPath();
                base64 = Base64Util.getBase64FromPath(path);

                File file = new File(path);
                /*RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("attachment", file.getName(), requestBody);
                final InputStream imageStream = getContentResolver().openInputStream(uri);

                txtDescription.setName(path);
                */
                txtAttachment.setText(file.getName());
                /*final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                imageView.setImageBitmap(selectedImage);
                if(selectedImage.getHeight()>600&&selectedImage.getWidth()>600)
                {
                    int newWidth=0;
                    int newHeight=0;
                    int maxPixel=600;
                    if(selectedImage.getWidth()>selectedImage.getHeight())
                    {
                        newHeight=maxPixel;
                        newWidth=maxPixel*selectedImage.getWidth()/selectedImage.getHeight();
                    }
                    else
                    {
                        newWidth=maxPixel;
                        newHeight=maxPixel*selectedImage.getHeight()/selectedImage.getWidth();

                    }
//                    selectedImage2.createScaledBitmap(selectedImage, newWidth, newHeight, false);
                    Bitmap selectedImage2 = Bitmap.createScaledBitmap(selectedImage, newWidth, newHeight, false);
                    attachmentFile = encodeImage(selectedImage2);

                }
                else
                    attachmentFile = encodeImage(selectedImage);
                    */
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(TransportReimbursementCreateActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(TransportReimbursementCreateActivity.this, "You haven't picked Attachment", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(transportReimbursementViewModel != null) {
            getReimbursementDetail(transportReimbursementViewModel.getHeaderID());
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String result;
        try {
            String[] proj = {MediaStore.Files.FileColumns.DATA};
            CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            cursor.close();
        } catch (Exception ex) {
            result = uri.getPath();
        }
        return result;
    }

    public void deleteCheckedDraft(String listid) {
        apiInterface = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), listid);
        Call<MessageReturn> call3 = apiInterface.deleteTransportReimbursementDetail(body);
        call3.enqueue(new Callback<MessageReturn>() {
            @Override
            public void onResponse(Call<MessageReturn> call, Response<MessageReturn> response) {
                progressDialog.dismiss();
                MessageReturn data = response.body();

                if (data != null && data.isIsSucceed()) {
                    Toast.makeText(TransportReimbursementCreateActivity.this, data.getMessage(), Toast.LENGTH_LONG).show();

                    // delete list id on checkbox
                    //ListDraftLeaveRequestAdapter.listID.clear();

                    // call list draft
                    getReimbursementDetail(transportReimbursementViewModel.getHeaderID());
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(TransportReimbursementCreateActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(TransportReimbursementCreateActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<MessageReturn> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    private void setTotalAmount(TransportReimbursementViewModel transportReimbursementViewModel) {

        StringConverter stringConverter = new StringConverter();
        Integer totalAmount = 0;
        for (TransportReimbursementDetail transportReimbursementDetail : transportReimbursementViewModel.getListDetail()) {
            totalAmount += transportReimbursementDetail.getAmountDetail();
        }
        txtTotalAmount.setText(stringConverter.numberFormat(totalAmount + ""));
    }

    private void setupMonthYearPicker() {

        period = Calendar.getInstance();
        DateUtil.setFirstDayOfMonth(period);
        DateUtil.setFirstTimeOfDay(period);

        imgPeriodPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar maxPeriod = Calendar.getInstance();

                DateUtil.setFirstDayOfMonth(maxPeriod);
                DateUtil.setFirstTimeOfDay(maxPeriod);
                Calendar minPeriod = Calendar.getInstance();
                DateUtil.setFirstDayOfMonth(minPeriod);
                DateUtil.setFirstTimeOfDay(minPeriod);
                minPeriod.add(Calendar.MONTH, -2);

                PeriodPicker periodPicker = new PeriodPicker(TransportReimbursementCreateActivity.this, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int month, int dayOfMonth) {
                        period.set(year, month, dayOfMonth);
                        txtPeriod.setText(stringConverter.dateFormatMMMMYYYY(stringConverter.dateToString(period.getTime())));
                    }
                }, period.get(Calendar.YEAR), period.get(Calendar.MONTH));
                periodPicker.setMinDate(minPeriod);
                periodPicker.show();


                /*DatePickerDialog dpd = new DatePickerDialog(TransportReimbursementCreateActivity.this, AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        period.set(year, month, dayOfMonth);
                        txtPeriod.setText(stringConverter.dateFormatMMMMYYYY(stringConverter.dateToString(period.getTime())));

                    }
                }, period.get(Calendar.YEAR), period.get(Calendar.MONTH), period.get(Calendar.DAY_OF_MONTH)) {
                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        int day = getContext().getResources().getIdentifier("android:id/day", null, null);
                        if (day != 0) {
                            View dayPicker = findViewById(day);
                            if (dayPicker != null) {
                                dayPicker.setVisibility(View.GONE);
                            }
                        }
                    }
                };
                dpd.getDatePicker().setMinDate(minPeriod.getTimeInMillis());
                //dpd.getDatePicker().setMaxDate(maxPeriod.getTimeInMillis());
                dpd.show();
                */
/*
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(TransportReimbursementCreateActivity.this, new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        Log.d(TAG, "selectedMonth : " + selectedMonth + " selectedYear : " + selectedYear);
                        //Toast.makeText(TransportReimbursementCreateActivity.this, "Date set with month" + selectedMonth + " year " + selectedYear, Toast.LENGTH_SHORT).show();

                        period.set(Calendar.YEAR, selectedYear);
                        period.set(Calendar.MONTH, selectedMonth);
                        //entertainReimbursementViewModel.setPeriod(stringConverter.dateToString(period.getTime()));
                        txtPeriod.setName(stringConverter.dateFormatMMMMYYYY(stringConverter.dateToString(period.getTime())));


                    }
                }, period.get(Calendar.YEAR), period.get(Calendar.MONTH));

                builder//.setActivatedMonth(Calendar.JULY)
                        .setMinYear(minPeriod.get(Calendar.YEAR))
                        //.setActivatedYear(2017)
                        .setMaxYear(maxPeriod.get(Calendar.YEAR))
                        .setMinMonth(minPeriod.get(Calendar.MONTH))
                        .setTitle("Select Period")
                        //.setMonthRange(Calendar.FEBRUARY, Calendar.NOVEMBER)
                         .setMaxMonth(maxPeriod.get(Calendar.MONTH))
                        // .setYearRange(1890, 1890)
                        // .setMonthAndYearRange(Calendar.FEBRUARY, Calendar.OCTOBER, 1890, 1890)
                        //.showMonthOnly()
                        // .showYearOnly()
                        .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
                            @Override
                            public void onMonthChanged(int selectedMonth) {
                                Log.d(TAG, "Selected month : " + selectedMonth);

                                // Toast.makeText(MainActivity.this, " Selected month : " + selectedMonth, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                            @Override
                            public void onYearChanged(int selectedYear) {
                                Log.d(TAG, "Selected year : " + selectedYear);
                                // Toast.makeText(MainActivity.this, " Selected year : " + selectedYear, Toast.LENGTH_SHORT).show();
                            }
                        })

                        .build()
                        .show();
*/

            }
        });

        /*
        final YearMonthPickerDialogCustom yearMonthPickerDialog = new YearMonthPickerDialogCustom(this, new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -2);
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 01);
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, 01);
                selectedCalendar.set(Calendar.HOUR, 0);
                selectedCalendar.set(Calendar.HOUR_OF_DAY, 0);
                selectedCalendar.set(Calendar.MINUTE, 0);
                selectedCalendar.set(Calendar.SECOND, 0);
                selectedCalendar.set(Calendar.MILLISECOND, 1);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
                if (selectedCalendar.after(calendar))
                    txtPeriod.setName(dateFormat.format(selectedCalendar.getTime()));

            }
        });*/


        /*imgPeriodPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearMonthPickerDialog.show();
            }
        });
*/

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_STORAGE:

                break;

        }
    }

}
