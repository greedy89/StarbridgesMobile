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

import java.util.List;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.model.TransportReimbursement.TransportReimbursement;
import id.co.indocyber.android.starbridges.network.StringConverter;


public class TransportReimbursementDraftAdapter extends ArrayAdapter<TransportReimbursement> {
    Context context;
    List<TransportReimbursement> list;
    LayoutInflater inflater;

    private SparseBooleanArray mSelectedItemsIds;

    public List<TransportReimbursement> getList() {
        return list;
    }

    public TransportReimbursementDraftAdapter(@NonNull Context context, int resource, @NonNull List<TransportReimbursement> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        inflater=LayoutInflater.from(context);

        mSelectedItemsIds=new SparseBooleanArray();

    }

    private class ViewHolder {
        TextView txtType, txtAmount;
        TextView txtProcessPeriod, txtDescription;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final TransportReimbursementDraftAdapter.ViewHolder holder;

        if(convertView==null)
        {
            holder = new TransportReimbursementDraftAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.lst_transport_reimbursement_draft, null);

            holder.txtType =  convertView.findViewById(R.id.txtType);
            holder.txtAmount =  convertView.findViewById(R.id.txtAmount);
            holder.txtProcessPeriod = convertView.findViewById(R.id.txtProcessPeriod);
            holder.txtDescription = convertView.findViewById(R.id.txtDescription);

            convertView.setTag(holder);
        }
        else
        {
            holder = (TransportReimbursementDraftAdapter.ViewHolder) convertView.getTag();
        }

        StringConverter stringConverter=  new StringConverter();
        TransportReimbursement item = list.get(position);
        holder.txtType.setText(item.getType());
        holder.txtAmount.setText(stringConverter.numberFormat(item.getAmount()+"")  );
        //Calendar calendar = Calendar.getInstance();
        //calendar.set(list.get(position).getYear(), list.get(position).getMonth()-1, 1);
        //holder.txtProcessPeriod.setName("Process "+ stringConverter.dateFormatDDMMYYYY(list.get(position).getProcessPeriod()));
        //holder.txtProcessPeriod.setName("Process "+ stringConverter.dateFormatMMMMYYYY( stringConverter.dateToString(calendar.getTime())));
        holder.txtProcessPeriod.setText(stringConverter.dateFormatMMMMYYYY(item.getProcessPeriod()));
        holder.txtDescription.setText( item.getDescription() );

        return convertView;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    @Override
    public void remove(@Nullable TransportReimbursement object) {
        list.remove(object);
        notifyDataSetChanged();
    }






    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }



    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }


}
