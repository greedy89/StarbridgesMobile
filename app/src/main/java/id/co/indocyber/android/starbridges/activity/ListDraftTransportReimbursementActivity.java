package id.co.indocyber.android.starbridges.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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
import id.co.indocyber.android.starbridges.adapter.TransportReimbursementDraftAdapter;
import id.co.indocyber.android.starbridges.model.MessageReturn.MessageReturn;
import id.co.indocyber.android.starbridges.model.TransportReimbursement.TransportReimbursement;
import id.co.indocyber.android.starbridges.model.WebServiceResponseList;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.utility.GlobalVar;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListDraftTransportReimbursementActivity extends AppCompatActivity {

    private ListView lstDraftTransportReimbursement;
    private FloatingActionButton fabAddDraftTransportReimbursement;
    private TransportReimbursementDraftAdapter viewAdapter;
    private ProgressDialog progressDialog;


    private APIInterfaceRest apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_draft_transport_reimbursement);

        setTitle("Transport Draft");


        initComponent();
        setupFAB();
        setupClickItemListView();
        setupMultipleSelectedItemListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTransportReimbursementDraftList();


    }

    private void initComponent() {
        fabAddDraftTransportReimbursement = (FloatingActionButton) findViewById(R.id.fabAddDraftTransportReimbursement);
        lstDraftTransportReimbursement = (ListView) findViewById(R.id.lstDraftTransportReimbursement);

    }

    private void setupFAB() {


        fabAddDraftTransportReimbursement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListDraftTransportReimbursementActivity.this, TransportReimbursementCreateActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupClickItemListView() {
        lstDraftTransportReimbursement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TransportReimbursement item = viewAdapter.getList().get(position);
                Bundle bundle = new Bundle();
                bundle.putString(TransportReimbursementCreateActivity.PARAM_HEADER_ID, item.getId());
                //bundle.putSerializable(TransportReimbursementCreateActivity.PARAM_TRANSPORT_REIMBURSEMENT, returnValue);
                Intent intent = new Intent(ListDraftTransportReimbursementActivity.this, TransportReimbursementCreateActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void setupMultipleSelectedItemListView() {
        lstDraftTransportReimbursement.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lstDraftTransportReimbursement.setItemsCanFocus(true);

        lstDraftTransportReimbursement.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

                // Capture total checked items
                final int checkedCount = lstDraftTransportReimbursement.getCheckedItemCount();
                // Set the CAB title according to total checked items
                actionMode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                viewAdapter.toggleSelection(i);


            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.menu_draft_reimbursement, menu);
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
                        AlertDialog.Builder alert = new AlertDialog.Builder(ListDraftTransportReimbursementActivity.this);
                        alert.setTitle("Confirmation");
                        alert.setTitle("This information will be deleted");

                        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // Calls getSelectedIds method from ListViewAdapter Class
                                final SparseBooleanArray selected = viewAdapter.getSelectedIds();
                                List<String> listSelectedId = new ArrayList<>();
                                // Captures all selected ids with a loop
                                for (int i2 = (selected.size() - 1); i2 >= 0; i2--) {
                                    if (selected.valueAt(i2)) {
                                        TransportReimbursement selecteditem = viewAdapter
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


    public void getTransportReimbursementDraftList() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final APIInterfaceRest apiInterface = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
        Call<WebServiceResponseList<TransportReimbursement>> call = apiInterface.getTransportReimbursementDraft();
        call.enqueue(new Callback<WebServiceResponseList<TransportReimbursement>>() {
            @Override
            public void onResponse(Call<WebServiceResponseList<TransportReimbursement>> call, Response<WebServiceResponseList<TransportReimbursement>> response) {
                progressDialog.dismiss();
                WebServiceResponseList<TransportReimbursement> data = response.body();
                if (data != null && data.getIsSucceed()) {
                    viewAdapter = new TransportReimbursementDraftAdapter(ListDraftTransportReimbursementActivity.this, R.layout.lst_transport_reimbursement_draft, data.getReturnValue());
                    lstDraftTransportReimbursement.setAdapter(viewAdapter);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(ListDraftTransportReimbursementActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(ListDraftTransportReimbursementActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<WebServiceResponseList<TransportReimbursement>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void deleteCheckedDraft(String listid) {
        apiInterface = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), listid);
        Call<MessageReturn> call = apiInterface.deleteTransportReimbursementDraft(body);
        call.enqueue(new Callback<MessageReturn>() {
            @Override
            public void onResponse(Call<MessageReturn> call, Response<MessageReturn> response) {
                progressDialog.dismiss();
                MessageReturn data = response.body();

                if (data != null && data.isIsSucceed()) {
                    Toast.makeText(ListDraftTransportReimbursementActivity.this, data.getMessage(), Toast.LENGTH_LONG).show();


                    // call list draft
                    getTransportReimbursementDraftList();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(ListDraftTransportReimbursementActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(ListDraftTransportReimbursementActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
}