package id.co.indocyber.android.starbridges.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.adapter.EntertainReimbursementDetailAdapter;
import id.co.indocyber.android.starbridges.interfaces.OnItemClickListener;
import id.co.indocyber.android.starbridges.interfaces.OnItemLongClickListener;
import id.co.indocyber.android.starbridges.library.PeriodPicker;
import id.co.indocyber.android.starbridges.model.EntertainReimbursement.EntertainReimbursementDetail;
import id.co.indocyber.android.starbridges.model.EntertainReimbursement.EntertainReimbursementType;
import id.co.indocyber.android.starbridges.model.EntertainReimbursement.EntertainReimbursementViewModel;
import id.co.indocyber.android.starbridges.model.MessageReturn.MessageReturn;
import id.co.indocyber.android.starbridges.model.WebServiceResponseList;
import id.co.indocyber.android.starbridges.model.WebServiceResponseObject;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.network.StringConverter;
import id.co.indocyber.android.starbridges.utility.DateUtil;
import id.co.indocyber.android.starbridges.utility.GlobalVar;
import id.co.indocyber.android.starbridges.utility.MessageUtil;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntertainReimbursementDetailActivity extends AppCompatActivity {

    String TAG = "EntertainReimbursementDetailActivity";

    public static final String PARAM_HEADER_ID = "headerId";

    public EntertainReimbursementViewModel entertainReimbursementViewModel;

    private EditText txtPeriod, txtTotalAmount, txtDescription;
    private Spinner spnReimbursementType;
    private RecyclerView lstDetail;
    private ImageView imgPeriodPicker;
    private FloatingActionButton fabAddDetail;
    private EntertainReimbursementDetailAdapter viewAdapter;
    private ProgressDialog progressDialog;
    private Button btnSave, btnSubmit, btnCancel;
    private APIInterfaceRest apiInterface;


    StringConverter stringConverter;
    Calendar period;

    List<EntertainReimbursementType> entertainReimbursementTypes;

    private android.support.v7.view.ActionMode actionMode;
    private ActionModeCallback actionModeCallback = new ActionModeCallback();


    //private String headerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertain_reimbursement_detail);

        setTitle("Detail");

        stringConverter = new StringConverter();
        apiInterface = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);

        initComponent();

        setupBtnSave();
        setupBtnSubmit();
        setupBtnCancel();

        setupMonthYearPicker();
        setupFAB();

        //setupClickItemListView();
        //setupMultipleChoiceListView();

        setupSpinnerReimbursementType();
        setupListView();
        //initData();


    }


    private void initComponent() {
        txtPeriod = findViewById(R.id.txtPeriod);
        imgPeriodPicker = findViewById(R.id.imgPeriodPicker);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        txtDescription = findViewById(R.id.txtDescription);
        spnReimbursementType = findViewById(R.id.spnReimbursementType);
        lstDetail = findViewById(R.id.lstDetail);
        fabAddDetail = findViewById(R.id.fabAddDetail);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnSubmit = findViewById(R.id.btnSubmit);


        txtTotalAmount.setEnabled(false);

    }

    private void setupMultipleChoiceListView() {
        viewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onLongItemClick(int position) {
                if (actionMode == null) {
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
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(count + " Selected");
            actionMode.invalidate();
        }
    }

    private void setupListView() {

        lstDetail.setNestedScrollingEnabled(false);
        lstDetail.setItemAnimator(new DefaultItemAnimator());
        lstDetail.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickItemListView() {
        viewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (actionMode != null) {
                    toggleSelection(position);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(EntertainReimbursementCreateDetailActivity.PARAM_ID_HEADER, entertainReimbursementViewModel.getHeaderId());
                    bundle.putLong(EntertainReimbursementCreateDetailActivity.PARAM_PERIOD, period.getTimeInMillis());
                    bundle.putSerializable(EntertainReimbursementCreateDetailActivity.PARAM_ENTERTAIN_REIMBURSEMENT_DETAIL, entertainReimbursementViewModel.getCreateDetail().get(position));
                    Intent intent = new Intent(EntertainReimbursementDetailActivity.this, EntertainReimbursementCreateDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    private void initData() {
        String id = getIntent().getStringExtra(PARAM_HEADER_ID);
        if (id != null) {
            editEntertainReimbursementDraft(id);
            imgPeriodPicker.setEnabled(false);
        } else {
            entertainReimbursementViewModel = new EntertainReimbursementViewModel();
        }
    }

    /*private void setupMultipleChoiceListView() {
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
                        AlertDialog.Builder alert = new AlertDialog.Builder(EntertainReimbursementDetailActivity.this);
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
                                        EntertainReimbursementDetail selecteditem = viewAdapter
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
                AlertDialog.Builder alert = new AlertDialog.Builder(EntertainReimbursementDetailActivity.this);
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


    private void setupFAB() {
        fabAddDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(entertainReimbursementViewModel.getHeaderId())) {
                    saveTransactionGetId("Save");
                } else {
                    openAddDetailActivity();
                }

            }
        });
    }

    private void openAddDetailActivity() {
        Bundle bundle = new Bundle();
        bundle.putString(EntertainReimbursementCreateDetailActivity.PARAM_ID_HEADER, entertainReimbursementViewModel.getHeaderId());
        bundle.putLong(EntertainReimbursementCreateDetailActivity.PARAM_PERIOD, period.getTimeInMillis());
        Intent intent = new Intent(EntertainReimbursementDetailActivity.this, EntertainReimbursementCreateDetailActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void setEntertainReimbursementEditData() {
        if (entertainReimbursementViewModel != null) {
            StringConverter stringConverter = new StringConverter();

            period.setTime(stringConverter.dateToDate(entertainReimbursementViewModel.getProcessPeriod()));

            DateUtil.setFirstDayOfMonth(period);
            DateUtil.setFirstTimeOfDay(period);
            txtPeriod.setText(stringConverter.dateFormatMMMMYYYY(entertainReimbursementViewModel.getProcessPeriod()));

            txtDescription.setText(entertainReimbursementViewModel.getDescription());
            //txtTotalAmount.setName(stringConverter.numberFormat( entertainReimbursementViewModel.getAmount()+""));
            int i = 0;
            for (EntertainReimbursementType item : entertainReimbursementTypes) {
                if (item.getValue().equalsIgnoreCase(entertainReimbursementViewModel.getReimbursementTypeId() + "")) {
                    spnReimbursementType.setSelection(i);
                    break;
                }
                i++;
            }

        }
    }

    private Boolean validatingAndSetViewModel(String transactionStatus) {
        if (TextUtils.isEmpty(txtPeriod.getText().toString())) {
            MessageUtil.showMessage(this, "Warning", "Mohon pilih period terlebih dahulu");
            return false;
        } else if (TextUtils.isEmpty(txtDescription.getText().toString())) {
            MessageUtil.showMessage(this, "Warning", "Mohon isi description terlebih dahulu");
            return false;
        }

        if (entertainReimbursementViewModel == null) {
            entertainReimbursementViewModel = new EntertainReimbursementViewModel();
        }
        entertainReimbursementViewModel.setDescription(txtDescription.getText().toString());
        String amount = txtTotalAmount.getText().toString();
        if (TextUtils.isEmpty(amount)) {
            amount = "0";
        }
        entertainReimbursementViewModel.setAmount(Integer.parseInt(stringConverter.numberRemoveFormat(amount)));

        entertainReimbursementViewModel.setProcessPeriod(stringConverter.dateToString(period.getTime()));
        EntertainReimbursementType reimbursementType = (EntertainReimbursementType) spnReimbursementType.getSelectedItem();
        entertainReimbursementViewModel.setReimbursementTypeId(Integer.parseInt(reimbursementType.getValue()));
        entertainReimbursementViewModel.setEmployeeId(GlobalVar.getEmployeeId());
        entertainReimbursementViewModel.setTransactionStatusSaveOrSubmit(transactionStatus);

        //entertainReimbursementViewModel.setListDetail();
        return true;
    }

    private void saveTransactionGetId(String transactionStatus) {
        if (validatingAndSetViewModel(transactionStatus) == false)
            return;

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(entertainReimbursementViewModel).toString());

        Call<WebServiceResponseObject<String>> call = apiInterface.saveEntertainReimbursement(body);
        call.enqueue(new Callback<WebServiceResponseObject<String>>() {
            @Override
            public void onResponse(Call<WebServiceResponseObject<String>> call, Response<WebServiceResponseObject<String>> response) {
                progressDialog.dismiss();
                WebServiceResponseObject<String> data = response.body();
                if (data != null && data.getIsSucceed()) {
                    entertainReimbursementViewModel.setHeaderId(data.getReturnValue());
                    openAddDetailActivity();
                } else {
                    Toast.makeText(EntertainReimbursementDetailActivity.this, "Failed to save data"
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

    private void requestConfirmation(final String transactionStatus) {

        if (validatingAndSetViewModel(transactionStatus) == false)
            return;

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(entertainReimbursementViewModel).toString());

        Call<WebServiceResponseObject<EntertainReimbursementViewModel>> call = apiInterface.confirmationEntertainReimbursement(transactionStatus, body);
        call.enqueue(new Callback<WebServiceResponseObject<EntertainReimbursementViewModel>>() {
            @Override
            public void onResponse(Call<WebServiceResponseObject<EntertainReimbursementViewModel>> call, Response<WebServiceResponseObject<EntertainReimbursementViewModel>> response) {
                progressDialog.dismiss();
                WebServiceResponseObject<EntertainReimbursementViewModel> data = response.body();
                if (data != null && data.getIsSucceed()) {
                    //entertainReimbursementViewModel = data.getReturnValue();

                    AlertDialog.Builder alert = new AlertDialog.Builder(EntertainReimbursementDetailActivity.this);
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
                        Toast.makeText(EntertainReimbursementDetailActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(EntertainReimbursementDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }


                //entertainReimbursementViewModel.setListDetail(data.getReturnValue());

            }

            @Override
            public void onFailure(Call<WebServiceResponseObject<EntertainReimbursementViewModel>> call, Throwable t) {
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
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(entertainReimbursementViewModel).toString());

        Call<WebServiceResponseObject<String>> call = apiInterface.saveEntertainReimbursement(body);
        call.enqueue(new Callback<WebServiceResponseObject<String>>() {
            @Override
            public void onResponse(Call<WebServiceResponseObject<String>> call, Response<WebServiceResponseObject<String>> response) {
                progressDialog.dismiss();
                WebServiceResponseObject<String> data = response.body();
                if (data != null && data.getIsSucceed()) {
                    Toast.makeText(EntertainReimbursementDetailActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EntertainReimbursementDetailActivity.this, "Failed to save data"
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
        apiInterface.getEntertainReimbursementType().enqueue(new Callback<WebServiceResponseList<EntertainReimbursementType>>() {
            @Override
            public void onResponse(Call<WebServiceResponseList<EntertainReimbursementType>> call, Response<WebServiceResponseList<EntertainReimbursementType>> response) {

                if (response.isSuccessful()) {

                    entertainReimbursementTypes = response.body().getReturnValue();
                    if (entertainReimbursementTypes == null || entertainReimbursementTypes.size() == 0) {
                        Toast.makeText(EntertainReimbursementDetailActivity.this, "spinner Tidak dapat data", Toast.LENGTH_LONG).show();
                    } else {
                        ArrayAdapter<EntertainReimbursementType> adapter = new ArrayAdapter<EntertainReimbursementType>(EntertainReimbursementDetailActivity.this,
                                android.R.layout.simple_spinner_item, entertainReimbursementTypes);

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spnReimbursementType.setAdapter(adapter);
                    }
                    initData();
                } else {

                    Toast.makeText(EntertainReimbursementDetailActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<WebServiceResponseList<EntertainReimbursementType>> call, Throwable t) {
                Toast.makeText(EntertainReimbursementDetailActivity.this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();

            }
        });

/*
        entertainReimbursementTypes = new ArrayList<>();
        EntertainReimbursementType  reimbursementTypeExternal = new EntertainReimbursementType();
        reimbursementTypeExternal.setName("Entertain External");
        reimbursementTypeExternal.setReimbursementEntertainTypeId("1");
        entertainReimbursementTypes.add(reimbursementTypeExternal);

        EntertainReimbursementType  reimbursementType = new EntertainReimbursementType();
        reimbursementType.setName("Entertain Internal");
        reimbursementType.setReimbursementEntertainTypeId("2");
        entertainReimbursementTypes.add(reimbursementType);

        ArrayAdapter<EntertainReimbursementType> adapter = new ArrayAdapter<EntertainReimbursementType>(EntertainReimbursementDetailActivity.this,
                android.R.layout.simple_spinner_item, entertainReimbursementTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnReimbursementType.setAdapter(adapter);
*/
    }

    public void editEntertainReimbursementDraft(String id) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<WebServiceResponseObject<EntertainReimbursementViewModel>> call = apiInterface.editEntertainReimbursementDraft(id);
        call.enqueue(new Callback<WebServiceResponseObject<EntertainReimbursementViewModel>>() {
            @Override
            public void onResponse(Call<WebServiceResponseObject<EntertainReimbursementViewModel>> call, Response<WebServiceResponseObject<EntertainReimbursementViewModel>> response) {
                progressDialog.dismiss();
                WebServiceResponseObject<EntertainReimbursementViewModel> data = response.body();
                if (data != null && data.getIsSucceed()) {
                    entertainReimbursementViewModel = data.getReturnValue();
                    setEntertainReimbursementEditData();
                    getReimbursementDetail(entertainReimbursementViewModel.getHeaderId());


                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(EntertainReimbursementDetailActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(EntertainReimbursementDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<WebServiceResponseObject<EntertainReimbursementViewModel>> call, Throwable t) {
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
        Call<WebServiceResponseList<EntertainReimbursementDetail>> call = apiInterface.getEntertainReimbursementDetail(id);
        call.enqueue(new Callback<WebServiceResponseList<EntertainReimbursementDetail>>() {
            @Override
            public void onResponse(Call<WebServiceResponseList<EntertainReimbursementDetail>> call, Response<WebServiceResponseList<EntertainReimbursementDetail>> response) {
                progressDialog.dismiss();
                WebServiceResponseList<EntertainReimbursementDetail> data = response.body();
                entertainReimbursementViewModel.setCreateDetail(data.getReturnValue());
                if (data != null && data.getIsSucceed()) {
                    viewAdapter = new EntertainReimbursementDetailAdapter(EntertainReimbursementDetailActivity.this, R.layout.list_entertain_reimbursement_detail, entertainReimbursementViewModel.getCreateDetail());
                    setupClickItemListView();
                    setupMultipleChoiceListView();
                    lstDetail.setAdapter(viewAdapter);
                    setTotalAmount(entertainReimbursementViewModel);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(EntertainReimbursementDetailActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(EntertainReimbursementDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<WebServiceResponseList<EntertainReimbursementDetail>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    public void deleteCheckedDraft(String listid) {
        apiInterface = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), listid);
        Call<MessageReturn> call = apiInterface.deleteEntertainReimbursementDetail(body);
        call.enqueue(new Callback<MessageReturn>() {
            @Override
            public void onResponse(Call<MessageReturn> call, Response<MessageReturn> response) {
                progressDialog.dismiss();
                MessageReturn data = response.body();

                if (data != null && data.isIsSucceed()) {
                    Toast.makeText(EntertainReimbursementDetailActivity.this, data.getMessage(), Toast.LENGTH_LONG).show();

                    // delete list id on checkbox
                    //ListDraftLeaveRequestAdapter.listID.clear();

                    // call list draft
                    getReimbursementDetail(entertainReimbursementViewModel.getHeaderId());
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(EntertainReimbursementDetailActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(EntertainReimbursementDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void setTotalAmount(EntertainReimbursementViewModel entertainReimbursementViewModel) {

        StringConverter stringConverter = new StringConverter();
        Integer totalAmount = 0;
        for (EntertainReimbursementDetail entertainReimbursementDetail : entertainReimbursementViewModel.getCreateDetail()) {
            totalAmount += entertainReimbursementDetail.getAmountDetail();
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
                minPeriod.add(Calendar.MONTH, -1);

                PeriodPicker periodPicker = new PeriodPicker(EntertainReimbursementDetailActivity.this, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int month, int dayOfMonth) {
                        period.set(year, month, dayOfMonth);
                        txtPeriod.setText(stringConverter.dateFormatMMMMYYYY(stringConverter.dateToString(period.getTime())));
                    }
                }, period.get(Calendar.YEAR), period.get(Calendar.MONTH));
                periodPicker.setMinDate(minPeriod);
                periodPicker.show();


                /*DatePickerDialog dpd = new DatePickerDialog(EntertainReimbursementDetailActivity.this, AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
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
                dpd.show();*/
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
    protected void onResume() {
        super.onResume();
        if(entertainReimbursementViewModel != null) {
            getReimbursementDetail(entertainReimbursementViewModel.getHeaderId());
        }
    }

    private class ActionModeCallback implements android.support.v7.view.ActionMode.Callback {


        @Override
        public boolean onCreateActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_draft_correction, menu);
            return true;

        }

        @Override
        public boolean onPrepareActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(final android.support.v7.view.ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.deleteDraft:
                    AlertDialog.Builder alert = new AlertDialog.Builder(EntertainReimbursementDetailActivity.this);
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
                                    EntertainReimbursementDetail selecteditem = viewAdapter
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
        public void onDestroyActionMode(android.support.v7.view.ActionMode mode) {
            viewAdapter.removeSelection();
            actionMode = null;
        }
    }

}




