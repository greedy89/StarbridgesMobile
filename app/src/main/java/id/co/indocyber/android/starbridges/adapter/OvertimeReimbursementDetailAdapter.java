package id.co.indocyber.android.starbridges.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.interfaces.OnItemClickListener;
import id.co.indocyber.android.starbridges.interfaces.OnItemLongClickListener;
import id.co.indocyber.android.starbridges.model.OvertimeReimbursement.OvertimeReimbursementDetail;
import id.co.indocyber.android.starbridges.network.StringConverter;

public class OvertimeReimbursementDetailAdapter extends SelectableAdapter<OvertimeReimbursementDetailAdapter.ViewHolder> {//RecyclerView.Adapter<TransportReimbursementDetailAdapter.ViewHolder> {//extends ArrayAdapter<TransportReimbursementDetail> {
    Context context;
    List<OvertimeReimbursementDetail> list;
    LayoutInflater inflater;
    //private SparseBooleanArray mSelectedItemsIds;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public List<OvertimeReimbursementDetail> getList() {
        return list;
    }

    public OvertimeReimbursementDetail getItem(int position) {
        return list.get(position);
    }

    public OvertimeReimbursementDetailAdapter(@NonNull Context context, int resource, @NonNull List<OvertimeReimbursementDetail> objects) {
        //mSelectedItemsIds=new SparseBooleanArray();
        this.context = context;
        this.list = objects;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.lst_overtime_reimbursement_detail, parent, false);

        final ViewHolder holder = new ViewHolder(convertView);
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemLongClickListener.onLongItemClick(holder.getAdapterPosition());
                }
            });
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        StringConverter stringConverter = new StringConverter();
        OvertimeReimbursementDetail item = list.get(position);
        if (isSelected(position)) {
            holder.layoutItem.setBackgroundColor(context.getResources().getColor(R.color.colorBackgroundSelected));
        } else {
            holder.layoutItem.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }

        holder.txtDate.setText(stringConverter.dateFormatDDMMYYYY(item.getDate()));
        holder.txtAmount.setText(stringConverter.numberFormat(item.getAmountDetail() + ""));
        holder.txtNotes.setText(item.getNotes());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtAmount, txtNotes;
        ViewGroup layoutItem;

        public ViewHolder(View itemView) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.layoutItem);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtNotes = itemView.findViewById(R.id.txtNotes);
        }
    }
}
/*
public class OvertimeReimbursementDetailAdapter extends ArrayAdapter<OvertimeReimbursementDetail> {
    Context context;
    List<OvertimeReimbursementDetail> list;
    LayoutInflater inflater;
    private SparseBooleanArray mSelectedItemsIds;


    public OvertimeReimbursementDetailAdapter(@NonNull Context context, int resource, @NonNull List<OvertimeReimbursementDetail> objects) {
        super(context, resource, objects);
        mSelectedItemsIds=new SparseBooleanArray();
        this.context = context;
        this.list = objects;
        inflater=LayoutInflater.from(context);
    }

    private class ViewHolder {
        TextView txtDate, txtAmount, txtNotes;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View itemView = convertView;
        final OvertimeReimbursementDetailAdapter.ViewHolder holder;

        if(convertView==null)
        {
            holder = new OvertimeReimbursementDetailAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.lst_overtime_reimbursement_detail, null);
            // Locate the TextViews in listview_item.xml
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtAmount = (TextView) convertView.findViewById(R.id.txtAmount);
            holder.txtNotes = (TextView) convertView.findViewById(R.id.txtNotes);
            convertView.setTag(holder);
        }
        else
        {
            holder = (OvertimeReimbursementDetailAdapter.ViewHolder) convertView.getTag();
        }



        StringConverter stringConverter=  new StringConverter();
        OvertimeReimbursementDetail transportReimbursementDetail = list.get(position);



        holder.txtDate.setText(stringConverter.dateFormatDDMMYYYY(transportReimbursementDetail.getDate()));
        holder.txtAmount.setText(stringConverter.numberFormat(transportReimbursementDetail.getAmountDetail()+""));
        holder.txtNotes.setText(transportReimbursementDetail.getNotes());

        return convertView;
    }

    @Override
    public void remove(@Nullable OvertimeReimbursementDetail object) {
        list.remove(object);
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
}*/
