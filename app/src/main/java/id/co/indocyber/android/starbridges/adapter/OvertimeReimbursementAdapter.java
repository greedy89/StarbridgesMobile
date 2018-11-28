package id.co.indocyber.android.starbridges.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.model.ListOvertimeReimbursement.ReturnValue;

public class OvertimeReimbursementAdapter extends ArrayAdapter<ReturnValue> {
    private Context ctx;
    private List<ReturnValue>listData;
    private int mode;
    public OvertimeReimbursementAdapter(@NonNull Context context, List<ReturnValue> listData,int mode) {
        super(context,R.layout.list_overtime_reimbursement,listData);
        this.ctx= context;
        this.listData = listData;
        this.mode = mode;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater =(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_overtime_reimbursement,parent,false);
        TextView lbldecisionNumber = rowView.findViewById(R.id.lblDecisionNumber);
        TextView lblapprovedDate = rowView.findViewById(R.id.lblApprovedDate);
        TextView decisionNumber = rowView.findViewById(R.id.vDecisionNumber);
        TextView approvedDate = rowView.findViewById(R.id.vApprovedDate);
        TextView processPeriod = rowView.findViewById(R.id.vProcessPeriod);
        TextView type = rowView.findViewById(R.id.vType);
        TextView amount = rowView.findViewById(R.id.vAmout);
        final TextView description = rowView.findViewById(R.id.vDescription);


        if(mode==0){
            decisionNumber.setText(listData.get(position).getDecisionNumber());
            approvedDate.setText(listData.get(position).getApprovedDate().substring(0,listData.get(position).getApprovedDate().lastIndexOf(":")));
            processPeriod.setText(listData.get(position).getProcessPeriod().substring(0,listData.get(position).getProcessPeriod().lastIndexOf(":")));
            type.setText(listData.get(position).getType());
            amount.setText(String.valueOf(listData.get(position).getAmount()));
            description.setText(listData.get(position).getDescription());
        }else if(mode ==1 ){
            /*
            decisionNumber.setText(listData.get(position).getDecisionNumber());
            approvedDate.setText(listData.get(position).getApprovedDate().substring(0,listData.get(position).getApprovedDate().lastIndexOf(":")));
            */
            lbldecisionNumber.setVisibility(View.GONE);
            lblapprovedDate.setVisibility(View.GONE);
            decisionNumber.setVisibility(View.GONE);
            approvedDate.setVisibility(View.GONE);
            processPeriod.setText(listData.get(position).getProcessPeriod().substring(0,listData.get(position).getProcessPeriod().lastIndexOf(":")));
            type.setText(listData.get(position).getType());
            amount.setText(String.valueOf(listData.get(position).getAmount()));
            description.setText(listData.get(position).getDescription());
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ctx,description.getText(),Toast.LENGTH_SHORT).show();
                }
            });
        }else{

        }
        return rowView;
    }
}
