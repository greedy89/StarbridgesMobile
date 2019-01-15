package id.co.indocyber.android.starbridges.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.interfaces.OnItemClickListener;
import id.co.indocyber.android.starbridges.interfaces.OnItemLongClickListener;
import id.co.indocyber.android.starbridges.model.TransportReimbursementDetail.TransportReimbursementDetail;
import id.co.indocyber.android.starbridges.network.StringConverter;


public class TransportReimbursementDetailAdapter extends SelectableAdapter<TransportReimbursementDetailAdapter.ViewHolder> {//RecyclerView.Adapter<TransportReimbursementDetailAdapter.ViewHolder> {//extends ArrayAdapter<TransportReimbursementDetail> {
    Context context;
    List<TransportReimbursementDetail> list;
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

    public List<TransportReimbursementDetail> getList() {
        return list;
    }

    public TransportReimbursementDetail getItem(int position) {
        return list.get(position);
    }

    public TransportReimbursementDetailAdapter(@NonNull Context context, int resource, @NonNull List<TransportReimbursementDetail> objects) {
        //mSelectedItemsIds=new SparseBooleanArray();
        this.context = context;
        this.list = objects;
        inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.lst_transport_reimbursement_detail, parent, false);

        final ViewHolder holder = new ViewHolder(convertView);
        if(onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
        if(onItemLongClickListener != null) {
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

        StringConverter stringConverter=  new StringConverter();
        TransportReimbursementDetail item = list.get(position);
        if(isSelected(position)) {
            holder.layoutItem.setBackgroundColor(context.getResources().getColor(R.color.colorBackgroundSelected));
        }
        else {
            holder.layoutItem.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }



        holder.txtDate.setText(stringConverter.dateFormatDDMMYYYY(item.getDate()));
        holder.txtLocation.setText(item.getLocation());
        holder.txtAmount.setText(stringConverter.numberFormat(item.getAmountDetail()+""));
        holder.txtNotes.setText(item.getNotes());
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtLocation, txtAmount, txtNotes;
        ViewGroup layoutItem;

        public ViewHolder(View itemView) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.layoutItem);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtNotes = itemView.findViewById(R.id.txtNotes);
        }
    }

/*
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View itemView = convertView;
        final TransportReimbursementDetailAdapter.ViewHolder holder;

        if(convertView==null)
        {
            holder = new TransportReimbursementDetailAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.lst_transport_reimbursement_detail, null);
            // Locate the TextViews in listview_item.xml
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtLocation = (TextView) convertView.findViewById(R.id.txtLocation);
            holder.txtAmount = (TextView) convertView.findViewById(R.id.txtAmount);
            holder.txtNotes = (TextView) convertView.findViewById(R.id.txtNotes);
            convertView.setTag(holder);
        }
        else
        {
            holder = (TransportReimbursementDetailAdapter.ViewHolder) convertView.getTag();
        }



        StringConverter stringConverter=  new StringConverter();
        TransportReimbursementDetail transportReimbursementDetail = list.get(position);



        holder.txtDate.setText(stringConverter.dateFormatDDMMYYYY(transportReimbursementDetail.getDate()));
        holder.txtLocation.setText(transportReimbursementDetail.getLocation());
        holder.txtAmount.setText(stringConverter.numberFormat(transportReimbursementDetail.getAmountDetail()+""));
        holder.txtNotes.setText(transportReimbursementDetail.getNotes());

        return convertView;
    }

    @Override
    public void remove(@Nullable TransportReimbursementDetail object) {
        list.remove(object);
        notifyDataSetChanged();
    }*/

    /*public void toggleSelection(int position) {
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
    }*/
}