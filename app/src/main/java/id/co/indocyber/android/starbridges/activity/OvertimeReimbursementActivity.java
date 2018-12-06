package id.co.indocyber.android.starbridges.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.adapter.OvertimeReimbursementAdapter;
import id.co.indocyber.android.starbridges.model.ListOvertimeReimbursement.OvertimeReimbursement;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.utility.GlobalVar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OvertimeReimbursementActivity extends AppCompatActivity {
    private ListView listOR;
    private FloatingActionButton fab;
    private OvertimeReimbursementAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime_reimbursement);
        setTitle("Overtime Reimburse");
//        ActionBar ab = getActionBar();
//        ab.setTitle("Overtime");
//        ab.setSubtitle("Reimbursement");
        listOR = (ListView)findViewById(R.id.listOvertimeReimbursement);
        fab = (FloatingActionButton)findViewById(R.id.fabAddOvertimeReimbursement);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OvertimeReimbursementActivity.this,OvertimeReimbursementDetailActivity.class));
            }
        });

        getListOvertimeReimbursement(0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_item_two){
//            listOR.setAdapter(null);
//            getListOvertimeReimbursement(1);
            startActivity(new Intent(OvertimeReimbursementActivity.this,ListDraftOvertimeReimbursementActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getListOvertimeReimbursement(final int mode){
        APIInterfaceRest apiInterfaceRest = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
        final ProgressDialog pg = new ProgressDialog(this);
        pg.setTitle("Loading");
        pg.show();
        if(mode==0) {
            Call<OvertimeReimbursement> overtimeReimbursementCall = apiInterfaceRest.getOvertimeReimbursementData();
            overtimeReimbursementCall.enqueue(new Callback<OvertimeReimbursement>() {
                @Override
                public void onResponse(Call<OvertimeReimbursement> call, Response<OvertimeReimbursement> response) {
                    OvertimeReimbursement data = response.body();
                    pg.dismiss();
                    if (data != null && data.isIsSucceed() == true) {
                        adapter = new OvertimeReimbursementAdapter(OvertimeReimbursementActivity.this, data.getReturnValue(), mode);
                        listOR.setAdapter(adapter);
                    } else if (data.getMessage() != null && data.getMessage() != "") {
                        Toast.makeText(OvertimeReimbursementActivity.this, data.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(OvertimeReimbursementActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(OvertimeReimbursementActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
        }else if(mode==1){
            Call<OvertimeReimbursement> overtimeReimbursementCall = apiInterfaceRest.getOvertimeReimbursementDataDraft();
            overtimeReimbursementCall.enqueue(new Callback<OvertimeReimbursement>() {
                @Override
                public void onResponse(Call<OvertimeReimbursement> call, Response<OvertimeReimbursement> response) {
                    OvertimeReimbursement data = response.body();
                    pg.dismiss();
                    if (data != null && data.isIsSucceed() == true) {
                        adapter = new OvertimeReimbursementAdapter(OvertimeReimbursementActivity.this, data.getReturnValue(), mode);
                        listOR.setAdapter(adapter);
                    } else if (data.getMessage() != null && data.getMessage() != "") {
                        Toast.makeText(OvertimeReimbursementActivity.this, data.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(OvertimeReimbursementActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(OvertimeReimbursementActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
        }else{

        }
    }
}
