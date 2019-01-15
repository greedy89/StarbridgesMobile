package id.co.indocyber.android.starbridges.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.model.EntertainReimbursement.EntertainReimbursement;
import id.co.indocyber.android.starbridges.model.OvertimeReimbursement.OvertimeReimbursement;
import id.co.indocyber.android.starbridges.model.TransportReimbursement.TransportReimbursement;
import id.co.indocyber.android.starbridges.network.StringConverter;

public class EntertainReimbursementAdapter extends ArrayAdapter<EntertainReimbursement> {
    Context context;
    List<EntertainReimbursement> list;
    LayoutInflater inflater;

    public List<EntertainReimbursement> getList() {
        return list;
    }

    public EntertainReimbursementAdapter(@NonNull Context context, int resource, @NonNull List<EntertainReimbursement> objects) {
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
        final EntertainReimbursementAdapter.ViewHolder holder;

        if (convertView == null) {
            holder = new EntertainReimbursementAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.list_entertain_reimbursement, null);

            holder.txtDecisionNumber = convertView.findViewById(R.id.txtDecisionNumber);
            holder.txtApprovedDate = convertView.findViewById(R.id.txtApprovedDate);
            holder.txtProcessPeriod = convertView.findViewById(R.id.txtProcessPeriod);
            holder.txtType = convertView.findViewById(R.id.txtType);
            holder.txtAmount = convertView.findViewById(R.id.txtAmount);
            holder.txtDescription = convertView.findViewById(R.id.txtDescription);

            convertView.setTag(holder);
        } else {
            holder = (EntertainReimbursementAdapter.ViewHolder) convertView.getTag();
        }

        StringConverter stringConverter = new StringConverter();
        EntertainReimbursement item = list.get(position);
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

        decisionNumber.setName(listData.get(position).getDecisionNumber());
        approvedDate.setName(listData.get(position).getApprovedDate().substring(0,listData.get(position).getApprovedDate().lastIndexOf(":")));
        String a = listData.get(position).getProcessPeriod();
        if(a==null||a==""){
            a="null";
        }
//        processPeriod.setName(listData.get(position).getProcessPeriod().substring(0,listData.get(position).getProcessPeriod().lastIndexOf(":")));
        processPeriod.setName(a);
        type.setName(listData.get(position).getType());
        amount.setName(String.valueOf(listData.get(position).getAmount()));
        description.setName(listData.get(position).getDescription());

        return rowView;
    }
}
*/