package com.example.android.starbridges.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.starbridges.R;
import com.example.android.starbridges.adapter.CorrectionAdapter;
import com.example.android.starbridges.model.ListAttendaceCorrection.ListAttendanceCorrection;
import com.example.android.starbridges.network.APIClient;
import com.example.android.starbridges.network.APIInterfaceRest;
import com.example.android.starbridges.utility.GlobalVar;

import org.json.JSONObject;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CorrectionActivity extends AppCompatActivity {

    TextView txtNameCorrection, txtNIKCorrection;

    private RecyclerView recyclerView;

    private ProgressDialog progressDialog;
    private CorrectionAdapter viewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correction);
        setTitle("Attendance Correction");
        txtNameCorrection=(TextView)findViewById(R.id.txtNameCorrection);
        txtNIKCorrection=(TextView)findViewById(R.id.txtNIKCorrection);
        recyclerView=(RecyclerView)findViewById(R.id.rcyListCorrection);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        txtNameCorrection.setText(GlobalVar.getFullName());
        txtNIKCorrection.setText(GlobalVar.getNik());

        getAttendaceCorrectionLog("2018-04-01T00:00:00", "2018-04-30T00:00:00");
    }


    public void getAttendaceCorrectionLog(String dateFrom, String dateTo) {

        progressDialog = new ProgressDialog(CorrectionActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();
        final APIInterfaceRest apiInterface = APIClient.getListAttendanceCorrection(GlobalVar.getAccessToken()).create(APIInterfaceRest.class);
        Call<ListAttendanceCorrection> call3 = apiInterface.getListAttendanceCorrection(dateFrom, dateTo);
        call3.enqueue(new Callback<ListAttendanceCorrection>() {
            @Override
            public void onResponse(Call<ListAttendanceCorrection> call, Response<ListAttendanceCorrection> response) {
                progressDialog.dismiss();
                ListAttendanceCorrection data = response.body();
                if (data != null && data.isIsSucceed()) {
                    viewAdapter = new CorrectionAdapter(CorrectionActivity.this, data.getReturnValue());
                    recyclerView.setAdapter(viewAdapter);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(CorrectionActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(CorrectionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ListAttendanceCorrection> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Maaf koneksi bermasalah", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}
