

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
import id.co.indocyber.android.starbridges.model.OvertimeReimbursement.OvertimeReimbursement;
import id.co.indocyber.android.starbridges.model.TransportReimbursement.ReturnValue;
import id.co.indocyber.android.starbridges.model.TransportReimbursement.TransportReimbursement;
import id.co.indocyber.android.starbridges.network.StringConverter;
import id.co.indocyber.android.starbridges.utility.SharedPreferenceUtils;


public class OvertimeReimbursementDraftAdapter extends ArrayAdapter<OvertimeReimbursement> {
    Context context;
    List<OvertimeReimbursement> list;
    LayoutInflater inflater;

    private SparseBooleanArray mSelectedItemsIds;

    public List<OvertimeReimbursement> getList() {
        return list;
    }

    public OvertimeReimbursementDraftAdapter(@NonNull Context context, int resource, @NonNull List<OvertimeReimbursement> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        inflater = LayoutInflater.from(context);

        mSelectedItemsIds = new SparseBooleanArray();

    }

    private class ViewHolder {
        TextView txtProcessPeriod;
        TextView txtType;
        TextView txtAmount;
        TextView txtDescription;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final OvertimeReimbursementDraftAdapter.ViewHolder holder;

        if (convertView == null) {
            holder = new OvertimeReimbursementDraftAdapter.ViewHolder();

            convertView = inflater.inflate(R.layout.list_overtime_reimbursement_draft, null);
            holder. txtProcessPeriod = convertView.findViewById(R.id.txtProcessPeriod);
            holder.txtType = convertView.findViewById(R.id.txtType);
            holder. txtAmount = convertView.findViewById(R.id.txtAmount);
            holder.txtDescription = convertView.findViewById(R.id.txtDescription);

            convertView.setTag(holder);
        } else {
            holder = (OvertimeReimbursementDraftAdapter.ViewHolder) convertView.getTag();
        }

        StringConverter stringConverter = new StringConverter();
        OvertimeReimbursement item = list.get(position);
        holder.txtProcessPeriod.setText(stringConverter.dateFormatMMMMYYYY(item.getProcessPeriod()));
        holder.txtType.setText(item.getType());
        holder.txtAmount.setText(stringConverter.numberFormat(item.getAmount()+""));
        holder.txtDescription.setText(item.getDescription());

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
    public void remove(@Nullable OvertimeReimbursement object) {
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

