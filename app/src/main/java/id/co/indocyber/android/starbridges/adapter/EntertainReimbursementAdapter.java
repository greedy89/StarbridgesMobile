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
import id.co.indocyber.android.starbridges.model.ListEntertainReimbursement.ReturnValue;

public class EntertainReimbursementAdapter extends ArrayAdapter<ReturnValue> {
    private Context ctx;
    private List<ReturnValue>listData;
//    private int mode;
    public EntertainReimbursementAdapter(@NonNull Context context, List<ReturnValue> listData) {
        super(context,R.layout.list_overtime_reimbursement,listData);
        this.ctx= context;
        this.listData = listData;
//        this.mode = mode;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater =(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_entertain_reimbursement,parent,false);
//        TextView lbldecisionNumber = rowView.findViewById(R.id.lblDecisionNumber);
//        TextView lblapprovedDate = rowView.findViewById(R.id.lblApprovedDate);
        TextView decisionNumber = rowView.findViewById(R.id.vDecisionNumberER);
        TextView approvedDate = rowView.findViewById(R.id.vApprovedDateER);
        TextView processPeriod = rowView.findViewById(R.id.vProcessPeriodER);
        TextView type = rowView.findViewById(R.id.vTypeER);
        TextView amount = rowView.findViewById(R.id.vAmoutER);
        final TextView description = rowView.findViewById(R.id.vDescriptionER);

        decisionNumber.setText(listData.get(position).getDecisionNumber());
        approvedDate.setText(listData.get(position).getApprovedDate().substring(0,listData.get(position).getApprovedDate().lastIndexOf(":")));
        String a = listData.get(position).getProcessPeriod();
        if(a==null||a==""){
            a="null";
        }
//        processPeriod.setText(listData.get(position).getProcessPeriod().substring(0,listData.get(position).getProcessPeriod().lastIndexOf(":")));
        processPeriod.setText(a);
        type.setText(listData.get(position).getType());
        amount.setText(String.valueOf(listData.get(position).getAmount()));
        description.setText(listData.get(position).getDescription());

        return rowView;
    }
}
