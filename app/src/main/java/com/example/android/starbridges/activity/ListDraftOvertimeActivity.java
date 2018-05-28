package com.example.android.starbridges.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.starbridges.R;
import com.example.android.starbridges.adapter.ListDraftOvertimeAdapter;
import com.example.android.starbridges.model.ListDraftOvertime.ListDraftOvertime;
import com.example.android.starbridges.network.APIClient;
import com.example.android.starbridges.network.APIInterfaceRest;
import com.example.android.starbridges.utility.GlobalVar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListDraftOvertimeActivity extends AppCompatActivity {


    // declare komp listView
    ListView listView;

    // declare adapter
    ListDraftOvertimeAdapter adapter;

    APIInterfaceRest apiInterface;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_draft_overtime);

        getListDraftLeaveRequest();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.leaverequest, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // add new leave request
        if(id == R.id.action_item_one ){
            Intent intent = new Intent(ListDraftOvertimeActivity.this, OvertimeDetailActivity.class);
            startActivity(intent);
            return true;
        }

        // delete
        if(id == R.id.action_item_two ){
            // get list data from checkbox
            String listid = ListDraftOvertimeAdapter.listID.toString();

            //Toast.makeText(ListDraftLeaveRequestActivity.this, "List : " + listid, Toast.LENGTH_SHORT).show();
            //deleteCheckedDraft(listid);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
//    public void deleteCheckedDraft(String listid){
//        apiInterface = APIClient.deleteLeaveRequest(GlobalVar.getToken()).create(APIInterfaceRest.class);
//        progressDialog = new ProgressDialog(ListDraftLeaveRequestActivity.this);
//        progressDialog.setTitle("Loading");
//        progressDialog.show();
//
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), listid);
//        Call<DeleteLeaveRequest> call3 = apiInterface.deleteLeaveRequst(body);
//        call3.enqueue(new Callback<DeleteLeaveRequest>() {
//            @Override
//            public void onResponse(Call<DeleteLeaveRequest> call, Response<DeleteLeaveRequest> response) {
//                progressDialog.dismiss();
//                DeleteLeaveRequest data = response.body();
//
//                if (data != null && data.getIsSucceed()) {
//                    Toast.makeText(ListDraftLeaveRequestActivity.this, data.getMessage(), Toast.LENGTH_LONG).show();
//
//                    // delete list id on checkbox
//                    ListDraftLeaveRequestAdapter.listID.clear();
//
//                    // call list draft
//                    getListDraftLeaveRequest();
//                } else {
//                    try {
//                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                        Toast.makeText(ListDraftLeaveRequestActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
//                    } catch (Exception e) {
//                        Toast.makeText(ListDraftLeaveRequestActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<DeleteLeaveRequest> call, Throwable t) {
//                progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(), "Maaf koneksi bermasalah", Toast.LENGTH_LONG).show();
//                call.cancel();
//            }
//        });
//    }

    public void getListDraftLeaveRequest(){
        apiInterface = APIClient.getListDraftOvertime(GlobalVar.getToken()).create(APIInterfaceRest.class);
        progressDialog = new ProgressDialog(ListDraftOvertimeActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();

        //Call<LeaveRequest> call3 = apiInterface.getListLeaveRequest("");
        Call<ListDraftOvertime> call3 = apiInterface.getListDraftOvertime();
        call3.enqueue(new Callback<ListDraftOvertime>() {
            @Override
            public void onResponse(Call<ListDraftOvertime> call, Response<ListDraftOvertime> response) {
                progressDialog.dismiss();
                ListDraftOvertime data = response.body();

                if (data != null) {

                    // pass context and data to the custor adapter
                    adapter = new ListDraftOvertimeAdapter(ListDraftOvertimeActivity.this, data.getReturnValue());

                    // get listView from activity
                    listView = (ListView) findViewById(R.id.listDraftOvertime);

                    // set listadapter
                    listView.setAdapter(adapter);

                } else {
                    try {
                        //JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //Toast.makeText(ListDraftLeaveRequestActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        //Toast.makeText(ListDraftLeaveRequestActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ListDraftOvertime> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Maaf koneksi bermasalah", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}