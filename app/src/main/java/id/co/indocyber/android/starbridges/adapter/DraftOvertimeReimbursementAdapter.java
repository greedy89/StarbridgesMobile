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
import id.co.indocyber.android.starbridges.model.ListOvertimeReimbursement.ReturnValue;
import id.co.indocyber.android.starbridges.utility.SharedPreferenceUtils;

public class DraftOvertimeReimbursementAdapter extends ArrayAdapter<ReturnValue> {
    private Context context;
    private List<ReturnValue> listData;
    private List<String> listSelectedId = new ArrayList<>();
    private LayoutInflater inflater;
    private SparseBooleanArray mSelectedItemsIds;

    public DraftOvertimeReimbursementAdapter(@NonNull Context context, int resource,List<ReturnValue> listData) {
        super(context, resource,listData);
        this.context = context;
        this.listData= listData;
        mSelectedItemsIds = new SparseBooleanArray();
        inflater = LayoutInflater.from(context);
    }

    private class ViewHolder{
        TextView processPeriod;
        TextView type;
        TextView amount;
        TextView description;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if(convertView!=null){
            holder = (ViewHolder) convertView.getTag();
        }else{
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_overtime_reimbursement,null);
            convertView.findViewById(R.id.lblDecisionNumber).setVisibility(View.GONE);
            convertView.findViewById(R.id.lblApprovedDate).setVisibility(View.GONE);
            convertView.findViewById(R.id.vDecisionNumber).setVisibility(View.GONE);
            convertView.findViewById(R.id.vApprovedDate).setVisibility(View.GONE);
            holder.processPeriod = (TextView) convertView.findViewById(R.id.vProcessPeriod);
            holder.type = (TextView) convertView.findViewById(R.id.vType);
            holder.amount = (TextView) convertView.findViewById(R.id.vAmout);
            holder.description = (TextView) convertView.findViewById(R.id.vDescription);
            convertView.setTag(holder);
        }
        holder.processPeriod.setText(listData.get(position).getProcessPeriod());
        holder.type.setText(listData.get(position).getType());
        holder.amount.setText(String.valueOf(listData.get(position).getAmount()));
        holder.description.setText(listData.get(position).getDescription());

        return convertView;
//        return super.getView(position, convertView, parent);
    }

    @Override
    public void remove(@Nullable ReturnValue object) {
        listData.remove(object);
        listSelectedId.add(object.getID());
        SharedPreferenceUtils.setSetting(context,"lstIdSelected", listSelectedId.toString());
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}
