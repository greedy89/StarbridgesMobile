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

import java.util.ArrayList;
import java.util.List;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.model.EntertainReimbursement.EntertainReimbursement;
import id.co.indocyber.android.starbridges.model.TransportReimbursement.ReturnValue;
import id.co.indocyber.android.starbridges.model.TransportReimbursement.TransportReimbursement;
import id.co.indocyber.android.starbridges.network.StringConverter;
import id.co.indocyber.android.starbridges.utility.SharedPreferenceUtils;


public class EntertainReimbursementDraftAdapter extends ArrayAdapter<EntertainReimbursement> {
    Context context;
    List<EntertainReimbursement> list;
    LayoutInflater inflater;

    private SparseBooleanArray mSelectedItemsIds;

    public List<EntertainReimbursement> getList() {
        return list;
    }

    public EntertainReimbursementDraftAdapter(@NonNull Context context, int resource, @NonNull List<EntertainReimbursement> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        inflater=LayoutInflater.from(context);

        mSelectedItemsIds=new SparseBooleanArray();

    }

    private class ViewHolder {
        TextView txtType;
        TextView txtIsPreProcess;
        TextView txtDescription;
        TextView txtAmount;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final EntertainReimbursementDraftAdapter.ViewHolder holder;

        if(convertView==null)
        {
            holder = new EntertainReimbursementDraftAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.list_entertain_reimbursement_draft, null);

            holder.txtType = convertView.findViewById(R.id.txtType);
            holder.txtIsPreProcess = convertView.findViewById(R.id.txtIsPreProcess);
            holder.txtDescription = convertView.findViewById(R.id.txtDescription);
            holder.txtAmount = convertView.findViewById(R.id.txtAmount);

            convertView.setTag(holder);
        }
        else
        {
            holder = (EntertainReimbursementDraftAdapter.ViewHolder) convertView.getTag();
        }

        StringConverter stringConverter=  new StringConverter();
        EntertainReimbursement item = list.get(position);
        holder.txtType.setText(item.getType());
        holder.txtIsPreProcess.setText(item.getPreProcess() ? "Pre Process" : "Process");
        holder.txtDescription.setText(item.getDescription());
        holder.txtAmount.setText(stringConverter.numberFormat(item.getAmount() + ""));

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
    public void remove(@Nullable EntertainReimbursement object) {
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

