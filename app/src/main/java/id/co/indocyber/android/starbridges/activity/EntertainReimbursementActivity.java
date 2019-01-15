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
import id.co.indocyber.android.starbridges.adapter.EntertainReimbursementAdapter;
import id.co.indocyber.android.starbridges.model.EntertainReimbursement.EntertainReimbursement;
import id.co.indocyber.android.starbridges.model.WebServiceResponseList;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.utility.GlobalVar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntertainReimbursementActivity extends AppCompatActivity {

    private ListView listEntertainReimbursement;
    private FloatingActionButton fabAddEntertainReimbursement;
    private EntertainReimbursementAdapter viewAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertain_reimbursement);

        setTitle("Entertain Reimbursement");

        initComponent();

        setupFAB();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getEntertainReimbursementList();
    }

    private void initComponent() {
        listEntertainReimbursement = findViewById(R.id.listEntertainReimbursement);
        fabAddEntertainReimbursement = findViewById(R.id.fabAddEntertainReimbursement);
    }

    private void setupFAB() {
        fabAddEntertainReimbursement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntertainReimbursementActivity.this, EntertainReimbursementDetailActivity.class);
                startActivity(intent);
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
        if (id == R.id.action_item_two) {
            startActivity(new Intent(EntertainReimbursementActivity.this, ListDraftEntertainReimbursementActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getEntertainReimbursementList() {

        progressDialog = new ProgressDialog(EntertainReimbursementActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final APIInterfaceRest apiInterface = APIClient.getClientWithToken(GlobalVar.getToken(), getApplicationContext()).create(APIInterfaceRest.class);
        Call<WebServiceResponseList<EntertainReimbursement>> call = apiInterface.getEntertainReimbursement();
        call.enqueue(new Callback<WebServiceResponseList<EntertainReimbursement>>() {
            @Override
            public void onResponse(Call<WebServiceResponseList<EntertainReimbursement>> call, Response<WebServiceResponseList<EntertainReimbursement>> response) {
                progressDialog.dismiss();
                WebServiceResponseList<EntertainReimbursement> data = response.body();
                if (data != null && data.getIsSucceed()) {
                    viewAdapter = new EntertainReimbursementAdapter(EntertainReimbursementActivity.this, R.layout.list_entertain_reimbursement, data.getReturnValue());
                    listEntertainReimbursement.setAdapter(viewAdapter);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(EntertainReimbursementActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(EntertainReimbursementActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<WebServiceResponseList<EntertainReimbursement>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}

/*package id.co.indocyber.android.starbridges.activity;

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


}
*/