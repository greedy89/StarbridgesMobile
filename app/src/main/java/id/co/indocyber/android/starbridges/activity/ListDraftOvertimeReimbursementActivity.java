package id.co.indocyber.android.starbridges.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import id.co.indocyber.android.starbridges.adapter.DraftOvertimeReimbursementAdapter;
import id.co.indocyber.android.starbridges.model.ListOvertimeReimbursement.OvertimeReimbursement;
import id.co.indocyber.android.starbridges.model.ListOvertimeReimbursement.ReturnValue;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.utility.GlobalVar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListDraftOvertimeReimbursementActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listOR;
    private APIInterfaceRest apiInterfaceRest;
    private DraftOvertimeReimbursementAdapter adapter;
    private String selectedId;
    OvertimeReimbursement listData;
    List<String> listSelectedId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_draft_overtime_reimbursement);
        listOR = (ListView) findViewById(R.id.lisDraftOvertimeReimbursementLv);
        getDataList();
    }

    private void getDataList() {
        apiInterfaceRest = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
        final ProgressDialog pg = new ProgressDialog(ListDraftOvertimeReimbursementActivity.this);
        pg.setTitle("Loading");
        pg.show();
        Call<OvertimeReimbursement> overtimeReimbursementCall = apiInterfaceRest.getOvertimeReimbursementDataDraft();
        overtimeReimbursementCall.enqueue(new Callback<OvertimeReimbursement>() {
            @Override
            public void onResponse(Call<OvertimeReimbursement> call, Response<OvertimeReimbursement> response) {
                pg.dismiss();
                listData = response.body();
                if (listData != null && listData.isIsSucceed() == true) {
                    adapter = new DraftOvertimeReimbursementAdapter(ListDraftOvertimeReimbursementActivity.this, R.layout.list_overtime_reimbursement, listData.getReturnValue());
                    listOR.setAdapter(adapter);
                    listOR.setOnItemClickListener(ListDraftOvertimeReimbursementActivity.this);
                    listOR.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                    listOR.setItemsCanFocus(true);
                    listOR.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                        @Override
                        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

                            // Capture total checked items
                            final int checkedCount = listOR.getCheckedItemCount();
                            // Set the CAB title according to total checked items
                            actionMode.setTitle(checkedCount + " Selected");
                            // Calls toggleSelection method from ListViewAdapter Class
                            adapter.toggleSelection(i);


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
                                    AlertDialog.Builder alert = new AlertDialog.Builder(ListDraftOvertimeReimbursementActivity.this);
                                    alert.setTitle("Confirmation");
                                    alert.setTitle("This information will be deleted");

                                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            // Calls getSelectedIds method from ListViewAdapter Class
                                            final SparseBooleanArray selected = adapter
                                                    .getSelectedIds();
//                                idSelected= SharedPreferenceUtils.getSetting(DraftCorrectionListActivity.this, "lstIdSelected", "");
                                            listSelectedId = new ArrayList<>();
                                            // Captures all selected ids with a loop
                                            for (int i2 = (selected.size() - 1); i2 >= 0; i2--) {
                                                if (selected.valueAt(i2)) {
                                                    ReturnValue selecteditem = adapter
                                                            .getItem(selected.keyAt(i2));
                                                    // Remove selected items following the ids
                                                    listSelectedId.add(selecteditem.getID());
                                                    adapter.remove(selecteditem);
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

                            adapter.removeSelection();

                        }
                    });
                } else if (listData.getMessage() != null && listData.getMessage() != "") {
                    Toast.makeText(ListDraftOvertimeReimbursementActivity.this, listData.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(ListDraftOvertimeReimbursementActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(ListDraftOvertimeReimbursementActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<OvertimeReimbursement> call, Throwable t) {
                pg.dismiss();
                Toast.makeText(getApplicationContext(), "Maaf koneksi bermasalah", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }

    private void deleteCheckedDraft(String selectedId) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String index = listData.getReturnValue().get(position).getID();
        Intent pindah = new Intent(ListDraftOvertimeReimbursementActivity.this,OvertimeReimbursementDetailActivity.class);
        pindah.putExtra("id",index);
        startActivity(pindah);
    }
}