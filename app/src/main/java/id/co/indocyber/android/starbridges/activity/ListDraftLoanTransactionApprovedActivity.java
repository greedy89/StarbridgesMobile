package id.co.indocyber.android.starbridges.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.adapter.ListDraftLeaveRequestAdapter;
import id.co.indocyber.android.starbridges.adapter.ListDraftLoanTransactionApprovedAdapter;
import id.co.indocyber.android.starbridges.model.ListDraftTransactionLoanApproved.ListDraftTransactionLoanApproved;
import id.co.indocyber.android.starbridges.model.MessageReturn.MessageReturn;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.reminder.utility.GlobalVar;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListDraftLoanTransactionApprovedActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView lstDraftLoanTransactionMain;

    ListDraftLoanTransactionApprovedAdapter viewAdapter;

    APIInterfaceRest apiInterface;

    ProgressDialog progressDialog;

    ListDraftTransactionLoanApproved data;

    FloatingActionButton fabAddLoanTransactionMain;
    List<String> listSelectedId = new ArrayList<>();
    String selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_draft_transaction_approved_loan);
        setTitle("Draft Loan Transaction");

        fabAddLoanTransactionMain=(FloatingActionButton)findViewById(R.id.fabAddLoanTransactionMain);

        lstDraftLoanTransactionMain=(ListView)findViewById(R.id.lstDraftLoanTransactionMain);

        getDraft();

        fabAddLoanTransactionMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ListDraftLoanTransactionApprovedActivity.this, LoanRequestCreateActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        id.co.indocyber.android.starbridges.model.ListDraftTransactionLoanApproved.ReturnValue data2 = data.getReturnValue().get(position);
        Intent intent = new Intent(ListDraftLoanTransactionApprovedActivity.this, LoanRequestCreateActivity.class);
        intent.putExtra("ID", data2.getID());
        startActivity(intent);
    }

    public void getDraft()
    {
        progressDialog= new ProgressDialog(ListDraftLoanTransactionApprovedActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        apiInterface = APIClient.editDraftLeaveCancelation(GlobalVar.getToken()).create(APIInterfaceRest.class);
        apiInterface.getListDraftLoanTransactionApproved().enqueue(new Callback<ListDraftTransactionLoanApproved>() {
            @Override
            public void onResponse(Call<ListDraftTransactionLoanApproved> call, Response<ListDraftTransactionLoanApproved> response) {

                if (response.body().getIsSucceed()) {
                    data=response.body();
                    viewAdapter = new ListDraftLoanTransactionApprovedAdapter(ListDraftLoanTransactionApprovedActivity.this, response.body().getReturnValue());
                    lstDraftLoanTransactionMain.setAdapter(viewAdapter);
                    lstDraftLoanTransactionMain.setOnItemClickListener(ListDraftLoanTransactionApprovedActivity.this);
                    lstDraftLoanTransactionMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                    lstDraftLoanTransactionMain.setItemsCanFocus(true);
                    lstDraftLoanTransactionMain.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                        @Override
                        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

                            // Capture total checked items
                            final int checkedCount = lstDraftLoanTransactionMain.getCheckedItemCount();
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
                                    AlertDialog.Builder alert = new AlertDialog.Builder(ListDraftLoanTransactionApprovedActivity.this);
                                    alert.setTitle("Confirmation");
                                    alert.setTitle("This information will be deleted");

                                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            // Calls getSelectedIds method from ListViewAdapter Class
                                            final SparseBooleanArray selected = viewAdapter
                                                    .getSelectedIds();
//                                idSelected= SharedPreferenceUtils.getSetting(DraftCorrectionListActivity.this, "lstIdSelected", "");
                                            listSelectedId = new ArrayList<>();
                                            // Captures all selected ids with a loop
                                            for (int i2 = (selected.size() - 1); i2 >= 0; i2--) {
                                                if (selected.valueAt(i2)) {
                                                    id.co.indocyber.android.starbridges.model.ListDraftTransactionLoanApproved.ReturnValue selecteditem = viewAdapter
                                                            .getItem(selected.keyAt(i2));
                                                    // Remove selected items following the ids
                                                    listSelectedId.add(selecteditem.getID());
                                                    viewAdapter.remove(selecteditem);
                                                }
                                            }
                                            selectedId = listSelectedId.toString();
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

//                    lstDraftLoanTransactionMain.setOnItemClickListener(ListDraftLoanTransactionApprovedActivity.this);

                } else {
                    Toast.makeText(ListDraftLoanTransactionApprovedActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ListDraftTransactionLoanApproved> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListDraftLoanTransactionApprovedActivity.this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void deleteCheckedDraft(String listid) {
        apiInterface = APIClient.deleteLeaveRequest(GlobalVar.getToken()).create(APIInterfaceRest.class);
        progressDialog = new ProgressDialog(ListDraftLoanTransactionApprovedActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), listid);
        Call<MessageReturn> call3 = apiInterface.deleteDraftLoan(body);
        call3.enqueue(new Callback<MessageReturn>() {
            @Override
            public void onResponse(Call<MessageReturn> call, Response<MessageReturn> response) {
                progressDialog.dismiss();
                MessageReturn data = response.body();

                if (data != null && data.isIsSucceed()) {
                    Toast.makeText(ListDraftLoanTransactionApprovedActivity.this, data.getMessage(), Toast.LENGTH_LONG).show();

                    // delete list id on checkbox
                    ListDraftLeaveRequestAdapter.listID.clear();

                    // call list draft
                    getDraft();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(ListDraftLoanTransactionApprovedActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(ListDraftLoanTransactionApprovedActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<MessageReturn> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListDraftLoanTransactionApprovedActivity.this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}
