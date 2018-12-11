package id.co.indocyber.android.starbridges.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.adapter.OvertimeReimbursementAdapter;

public class TransportReimbursementActivity extends AppCompatActivity {

    private ListView lstTransportReimbursement;
    private FloatingActionButton fabAddTransportReimbursement;
    private OvertimeReimbursementAdapter adapter;

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
}
