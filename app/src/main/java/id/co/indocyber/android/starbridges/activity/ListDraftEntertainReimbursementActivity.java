package id.co.indocyber.android.starbridges.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.indocyber.android.starbridges.R;

import id.co.indocyber.android.starbridges.adapter.ListDraftEntertainReimbursementAdapter;
import id.co.indocyber.android.starbridges.model.ListDraftEntertainReimbursement.DraftEntertainReimbursement;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;
import id.co.indocyber.android.starbridges.reminder.utility.GlobalVar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListDraftEntertainReimbursementActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listER;
    private APIInterfaceRest apiInterfaceRest;
    private ListDraftEntertainReimbursementAdapter adapter;
    private String selectedId;
    private DraftEntertainReimbursement responseData;
    DraftEntertainReimbursement listData;
    List<String> listSelectedId = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_draft_entertain_reimbursement);
        listER = (ListView)findViewById(R.id.lvDraftEntertainReimbursement);
        setListER();
    }

    private void setListER(){
        apiInterfaceRest = APIClient.getClient(GlobalVar.getToken()).create(APIInterfaceRest.class);
        final ProgressDialog pg = new ProgressDialog(ListDraftEntertainReimbursementActivity.this);
        pg.show();
        Call<DraftEntertainReimbursement> draftEntertainReimbursementCall = apiInterfaceRest.getEntertainReimbursementDataDraft();
        draftEntertainReimbursementCall.enqueue(new Callback<DraftEntertainReimbursement>() {
            @Override
            public void onResponse(Call<DraftEntertainReimbursement> call, Response<DraftEntertainReimbursement> response) {
                pg.dismiss();
                responseData = response.body();
                if(responseData!=null&&responseData.isIsSucceed()==true){
                    adapter = new ListDraftEntertainReimbursementAdapter(ListDraftEntertainReimbursementActivity.this,R.layout.list_draft_entertain_reimbursement,responseData.getReturnValue());
                    listER.setAdapter(adapter);
                    listER.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                } else if (responseData.getMessage() != null && responseData.getMessage() != "") {
                    Toast.makeText(ListDraftEntertainReimbursementActivity.this,responseData.getMessage(),Toast.LENGTH_LONG).show();
                }else{
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(ListDraftEntertainReimbursementActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(ListDraftEntertainReimbursementActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DraftEntertainReimbursement> call, Throwable t) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void deletedCheckedDraft(){

    }
}
