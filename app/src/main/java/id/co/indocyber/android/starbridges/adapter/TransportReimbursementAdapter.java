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
import id.co.indocyber.android.starbridges.model.TransportReimbursement.TransportReimbursement;
import id.co.indocyber.android.starbridges.network.StringConverter;

public class TransportReimbursementAdapter extends ArrayAdapter<TransportReimbursement> {
    Context context;
    List<TransportReimbursement> list;
    LayoutInflater inflater;

    public List<TransportReimbursement> getList() {
        return list;
    }

    public TransportReimbursementAdapter(@NonNull Context context, int resource, @NonNull List<TransportReimbursement> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        inflater=LayoutInflater.from(context);
    }

    private class ViewHolder {
        TextView txtDecisionNumber, txtType, txtAmount;
        TextView txtApprovedDate, txtProcessPeriod, txtDescription;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final TransportReimbursementAdapter.ViewHolder holder;

        if(convertView==null)
        {
            holder = new TransportReimbursementAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.lst_transport_reimbursement, null);

            holder.txtDecisionNumber =  convertView.findViewById(R.id.txtDecisionNumber);
            holder.txtType = convertView.findViewById(R.id.txtType);
            holder.txtAmount = convertView.findViewById(R.id.txtAmount);
            holder.txtApprovedDate = convertView.findViewById(R.id.txtApprovedDate);
            holder.txtProcessPeriod = convertView.findViewById(R.id.txtProcessPeriod);
            holder.txtDescription = convertView.findViewById(R.id.txtDescription);

            convertView.setTag(holder);
        }
        else
        {
            holder = (TransportReimbursementAdapter.ViewHolder) convertView.getTag();
        }

        StringConverter stringConverter=  new StringConverter();
        TransportReimbursement item = list.get(position);
        holder.txtDecisionNumber.setText(item.getDecisionNumber());
        holder.txtType.setText(item.getType());
        holder.txtAmount.setText(stringConverter.numberFormat(item.getAmount()+"")  );
        holder.txtApprovedDate.setText(stringConverter.dateFormatDDMMYYYY(item.getApprovedDate()));
        //Calendar calendar = Calendar.getInstance();
        //calendar.set(list.get(position).getYear(), list.get(position).getMonth()-1, 1);
        //holder.txtProcessPeriod.setName("Process "+ stringConverter.dateFormatDDMMYYYY(list.get(position).getProcessPeriod()));
        //holder.txtProcessPeriod.setName("Process "+ stringConverter.dateFormatMMMMYYYY( stringConverter.dateToString(calendar.getTime())));
        holder.txtProcessPeriod.setText(stringConverter.dateFormatMMMMYYYY(item.getProcessPeriod()));
        holder.txtDescription.setText( item.getDescription() );

        return convertView;
    }
}

