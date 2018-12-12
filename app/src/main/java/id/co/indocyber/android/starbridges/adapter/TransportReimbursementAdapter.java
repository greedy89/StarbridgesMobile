package id.co.indocyber.android.starbridges.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.model.TransportReimbursement.ReturnValue;
import id.co.indocyber.android.starbridges.network.StringConverter;

public class TransportReimbursementAdapter extends ArrayAdapter<ReturnValue> {
    Context context;
    List<id.co.indocyber.android.starbridges.model.TransportReimbursement.ReturnValue> lstorder = new ArrayList<id.co.indocyber.android.starbridges.model.TransportReimbursement.ReturnValue>();
    LayoutInflater inflater;
    private SparseBooleanArray mSelectedItemsIds;
    List<String> lstIdSelected= new ArrayList<>();


    public TransportReimbursementAdapter(@NonNull Context context, int resource, @NonNull List<id.co.indocyber.android.starbridges.model.TransportReimbursement.ReturnValue> objects) {
        super(context, resource, objects);
        mSelectedItemsIds=new SparseBooleanArray();
        this.context = context;
        this.lstorder = objects;
        inflater=LayoutInflater.from(context);
    }

    private class ViewHolder {
        TextView lblDecisionNumberTransportReimbursement, lblTypeTransportReimbursement, lblAmountTransportReimbursement;
        TextView lblApprovedDateTransportReimbursement, lblProcessPeriodTransportReimbursement, lblDescriptionTransportReimbursement;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View itemView = convertView;
        final TransportReimbursementAdapter.ViewHolder holder;

        if(convertView==null)
        {
            holder = new TransportReimbursementAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.lst_transport_reimbursement, null);
            // Locate the TextViews in listview_item.xml
            holder.lblDecisionNumberTransportReimbursement = (TextView) convertView.findViewById(R.id.lblDecisionNumberTransportReimbursement);
            holder.lblTypeTransportReimbursement = (TextView) convertView.findViewById(R.id.lblTypeTransportReimbursement);
            holder.lblAmountTransportReimbursement = (TextView) convertView.findViewById(R.id.lblAmountTransportReimbursement);
            holder.lblApprovedDateTransportReimbursement = (TextView) convertView.findViewById(R.id.lblApprovedDateTransportReimbursement);
            holder.lblProcessPeriodTransportReimbursement = (TextView) convertView.findViewById(R.id.lblProcessPeriodTransportReimbursement);
            holder.lblDescriptionTransportReimbursement = (TextView) convertView.findViewById(R.id.lblDescriptionTransportReimbursement);

            convertView.setTag(holder);
        }
        else
        {
            holder = (TransportReimbursementAdapter.ViewHolder) convertView.getTag();
        }

        StringConverter stringConverter=  new StringConverter();
        holder.lblDecisionNumberTransportReimbursement.setText(lstorder.get(position).getDecisionNumber());
        holder.lblTypeTransportReimbursement.setText(lstorder.get(position).getType());
        holder.lblAmountTransportReimbursement.setText(stringConverter.numberFormat(lstorder.get(position).getAmount()+"")  );
        holder.lblApprovedDateTransportReimbursement.setText("Approve "+ stringConverter.dateFormatDDMMYYYY(lstorder.get(position).getApprovedDate()));
        holder.lblProcessPeriodTransportReimbursement.setText("Process "+ stringConverter.dateFormatDDMMYYYY(lstorder.get(position).getProcessPeriod()));
        holder.lblDescriptionTransportReimbursement.setText( lstorder.get(position).getDescription() );

        return convertView;
    }
}

