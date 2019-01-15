package id.co.indocyber.android.starbridges.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.model.OvertimeReimbursement.OvertimeReimbursement;
import id.co.indocyber.android.starbridges.network.StringConverter;

public class OvertimeReimbursementAdapter extends ArrayAdapter<OvertimeReimbursement> {
    Context context;
    List<OvertimeReimbursement> list;
    LayoutInflater inflater;

    public List<OvertimeReimbursement> getList() {
        return list;
    }

    public OvertimeReimbursementAdapter(@NonNull Context context, int resource, @NonNull List<OvertimeReimbursement> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        inflater = LayoutInflater.from(context);
    }

    private class ViewHolder {
        TextView txtDecisionNumber;
        TextView txtApprovedDate;
        TextView txtProcessPeriod;
        TextView txtType;
        TextView txtAmount;
        TextView txtDescription;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final OvertimeReimbursementAdapter.ViewHolder holder;

        if (convertView == null) {
            holder = new OvertimeReimbursementAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.list_overtime_reimbursement, null);
            holder.txtDecisionNumber = convertView.findViewById(R.id.txtDecisionNumber);
            holder.txtApprovedDate = convertView.findViewById(R.id.txtApprovedDate);
            holder.txtProcessPeriod = convertView.findViewById(R.id.txtProcessPeriod);
            holder.txtType = convertView.findViewById(R.id.txtType);
            holder.txtAmount = convertView.findViewById(R.id.txtAmount);
            holder.txtDescription = convertView.findViewById(R.id.txtDescription);

            convertView.setTag(holder);
        } else {
            holder = (OvertimeReimbursementAdapter.ViewHolder) convertView.getTag();
        }

        StringConverter stringConverter = new StringConverter();
        OvertimeReimbursement item = list.get(position);
        holder.txtDecisionNumber.setText(item.getDecisionNumber());
        holder.txtApprovedDate.setText(stringConverter.dateFormatDDMMYYYY(item.getApprovedDate()));
        holder.txtProcessPeriod.setText(stringConverter.dateFormatMMMMYYYY(item.getProcessPeriod()));
        holder.txtType.setText(item.getType());
        holder.txtAmount.setText(stringConverter.numberFormat(item.getAmount() + ""));
        holder.txtDescription.setText(item.getDescription());
        return convertView;
    }
}



/*package id.co.indocyber.android.starbridges.adapter;

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
import id.co.indocyber.android.starbridges.model.OvertimeReimbursement.OvertimeReimbursement;

public class OvertimeReimbursementAdapter extends ArrayAdapter<OvertimeReimbursement> {
    private Context ctx;
    private List<OvertimeReimbursement>listData;
    private int mode;
    public OvertimeReimbursementAdapter(@NonNull Context context, List<OvertimeReimbursement> listData,int mode) {
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
            decisionNumber.setName(listData.get(position).getDecisionNumber());
            approvedDate.setName(listData.get(position).getApprovedDate().substring(0,listData.get(position).getApprovedDate().lastIndexOf(":")));
            processPeriod.setName(listData.get(position).getProcessPeriod().substring(0,listData.get(position).getProcessPeriod().lastIndexOf(":")));
            type.setName(listData.get(position).getType());
            amount.setName(String.valueOf(listData.get(position).getAmount()));
            description.setName(listData.get(position).getDescription());
        }else if(mode ==1 ){

            decisionNumber.setName(listData.get(position).getDecisionNumber());
            approvedDate.setName(listData.get(position).getApprovedDate().substring(0,listData.get(position).getApprovedDate().lastIndexOf(":")));

            lbldecisionNumber.setVisibility(View.GONE);
            lblapprovedDate.setVisibility(View.GONE);
            decisionNumber.setVisibility(View.GONE);
            approvedDate.setVisibility(View.GONE);
            processPeriod.setName(listData.get(position).getProcessPeriod().substring(0,listData.get(position).getProcessPeriod().lastIndexOf(":")));
            type.setName(listData.get(position).getType());
            amount.setName(String.valueOf(listData.get(position).getAmount()));
            description.setName(listData.get(position).getDescription());
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ctx,description.getName(),Toast.LENGTH_SHORT).show();
                }
            });
        }else{

        }
        return rowView;
    }
}
*/