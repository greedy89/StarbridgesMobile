package id.co.indocyber.android.starbridges.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.network.APIClient;
import id.co.indocyber.android.starbridges.network.APIInterfaceRest;

public class OvertimeReimbursementDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime_reimbursement_detail);
        String id = getIntent().getStringExtra("id");
        if(id!=null||id!=""){

        }
    }
    public void getDraft(){

    }
}
