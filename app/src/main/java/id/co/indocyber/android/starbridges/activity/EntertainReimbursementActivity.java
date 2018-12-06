package id.co.indocyber.android.starbridges.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.adapter.EntertainReimbursementAdapter;
import id.co.indocyber.android.starbridges.model.ListEntertainReimbursement.EntertainReimbursement;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.utility.GlobalVar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntertainReimbursementActivity extends AppCompatActivity {
    ListView listER;
    FloatingActionButton fab;
    EntertainReimbursementAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertain_reimbursement);
        setTitle("Entertain Reimburse");
        listER = (ListView) findViewById(R.id.lvEntertainReimburse);
        fab = (FloatingActionButton)findViewById(R.id.fabEntertainReimburse);
        getDataER();
    }

    private void getDataER() {
        APIInterfaceRest interfaceRest = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
        final ProgressDialog pg = new ProgressDialog(EntertainReimbursementActivity.this);
        pg.setTitle("loading");
        pg.show();
        Call<EntertainReimbursement> entertainReimbursementCall = interfaceRest.getEntertainReimbursementData();
        entertainReimbursementCall.enqueue(new Callback<EntertainReimbursement>() {
            @Override
            public void onResponse(Call<EntertainReimbursement> call, Response<EntertainReimbursement> response) {
                pg.dismiss();
                EntertainReimbursement data = response.body();
                if(data!=null&&data.isIsSucceed()==true){
                    adapter = new EntertainReimbursementAdapter(EntertainReimbursementActivity.this,data.getReturnValue());
                    listER.setAdapter(adapter);
                }else if(data.getMessage()!=null&&data.getMessage()!=""){
                    Toast.makeText(EntertainReimbursementActivity.this,data.getMessage(),Toast.LENGTH_LONG).show();
                }else{
                    try {
                        JSONObject json = new JSONObject(response.errorBody().toString());
                        Toast.makeText(EntertainReimbursementActivity.this,json.toString(),Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(EntertainReimbursementActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<EntertainReimbursement> call, Throwable t) {
                pg.dismiss();
                Toast.makeText(EntertainReimbursementActivity.this,"Maaf koneksi bermasalah",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
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
            startActivity(new Intent(EntertainReimbursementActivity.this,ListDraftOvertimeReimbursementActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
