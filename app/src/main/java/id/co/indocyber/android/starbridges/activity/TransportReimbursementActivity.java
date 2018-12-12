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
import id.co.indocyber.android.starbridges.adapter.ReimburseAdapter;
import id.co.indocyber.android.starbridges.adapter.TransportReimbursementAdapter;
import id.co.indocyber.android.starbridges.model.Reimbursement.Reimbursement;
import id.co.indocyber.android.starbridges.model.TransportReimbursement.TransportReimbursement;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.utility.GlobalVar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportReimbursementActivity extends AppCompatActivity {

    private ListView lstTransportReimbursement;
    private FloatingActionButton fabAddTransportReimbursement;
    private TransportReimbursementAdapter viewAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_reimbursement);

        setTitle("Transport Reimbursement");

        lstTransportReimbursement = (ListView)findViewById(R.id.lstTransportReimbursement);
        fabAddTransportReimbursement = (FloatingActionButton)findViewById(R.id.fabAddTransportReimbursement);

        fabAddTransportReimbursement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(TransportReimbursementActivity.this, TransportReimbursementCreateActivity.class);
                startActivity(intent);
            }
        });

        getReimbursementLog();
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
            startActivity(new Intent(TransportReimbursementActivity.this,ListDraftTransportReimbursementActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getReimbursementLog() {

        progressDialog = new ProgressDialog(TransportReimbursementActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final APIInterfaceRest apiInterface = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
        Call<TransportReimbursement> call3 = apiInterface.getTransportReimbursement();
        call3.enqueue(new Callback<TransportReimbursement>() {
            @Override
            public void onResponse(Call<TransportReimbursement> call, Response<TransportReimbursement> response) {
                progressDialog.dismiss();
                TransportReimbursement data = response.body();
                if (data != null && data.getIsSucceed()) {
                    viewAdapter = new TransportReimbursementAdapter(TransportReimbursementActivity.this, R.layout.lst_transport_reimbursement, data.getReturnValue());
                    lstTransportReimbursement.setAdapter(viewAdapter);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(TransportReimbursementActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(TransportReimbursementActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<TransportReimbursement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}
