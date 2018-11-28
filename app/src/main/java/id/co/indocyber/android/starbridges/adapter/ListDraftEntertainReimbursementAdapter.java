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
import id.co.indocyber.android.starbridges.model.ListDraftEntertainReimbursement.ReturnValue;
import id.co.indocyber.android.starbridges.reminder.utility.SharedPreferenceUtils;

public class ListDraftEntertainReimbursementAdapter extends ArrayAdapter<ReturnValue> {
    private Context ctx;
    private List<ReturnValue> lisData;
    private List<String> listSelectedId = new ArrayList<>();
    private LayoutInflater inflater;
    private SparseBooleanArray mSelectedItemsIds;

    public ListDraftEntertainReimbursementAdapter(@NonNull Context context, int resource, List<ReturnValue> lisData) {
        super(context, resource, lisData);
        this.ctx=context;
        this.lisData = lisData;
        mSelectedItemsIds = new SparseBooleanArray();
        inflater = LayoutInflater.from(context);
    }


    private class ViewHolder{
        TextView type;
        TextView isProcess;
        TextView description;
        TextView amount;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView!=null){
            holder=(ViewHolder)convertView.getTag();
        }else{
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_entertain_reimbursement,null);
            holder.type = (TextView) convertView.findViewById(R.id.vTypeERd);
            holder.isProcess = (TextView) convertView.findViewById(R.id.vIsProcessERd);
            holder.description = (TextView) convertView.findViewById(R.id.vDescriptionERd);
            holder.amount  = (TextView) convertView.findViewById(R.id.vAmoutERd);
            convertView.setTag(holder);
        }
        holder.type.setText(lisData.get(position).getType());
        holder.isProcess.setText(String.valueOf(lisData.get(position).isIsPreProcess()));
        holder.description.setText(lisData.get(position).getDescription());
        holder.amount.setText(String.valueOf(lisData.get(position).getAmount()));

        return super.getView(position, convertView, parent);
    }

    @Override
    public void remove(@Nullable ReturnValue object) {
        lisData.remove(object);
        listSelectedId.add(object.getID());
        SharedPreferenceUtils.setSetting(ctx,"lstIdSelected", listSelectedId.toString());
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
