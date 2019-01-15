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
import id.co.indocyber.android.starbridges.model.EntertainReimbursement.EntertainReimbursementDetail;
import id.co.indocyber.android.starbridges.model.OvertimeReimbursement.OvertimeReimbursementDetail;
import id.co.indocyber.android.starbridges.network.StringConverter;

public class EntertainReimbursementDetailAdapter extends SelectableAdapter<EntertainReimbursementDetailAdapter.ViewHolder> {//RecyclerView.Adapter<TransportReimbursementDetailAdapter.ViewHolder> {//extends ArrayAdapter<TransportReimbursementDetail> {
    Context context;
    List<EntertainReimbursementDetail> list;
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

    public List<EntertainReimbursementDetail> getList() {
        return list;
    }

    public EntertainReimbursementDetail getItem(int position) {
        return list.get(position);
    }

    public EntertainReimbursementDetailAdapter(@NonNull Context context, int resource, @NonNull List<EntertainReimbursementDetail> objects) {
        //mSelectedItemsIds=new SparseBooleanArray();
        this.context = context;
        this.list = objects;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.list_entertain_reimbursement_detail, parent, false);

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
        EntertainReimbursementDetail item = list.get(position);
        if (isSelected(position)) {
            holder.layoutItem.setBackgroundColor(context.getResources().getColor(R.color.colorBackgroundSelected));
        } else {
            holder.layoutItem.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }

        holder.txtDate.setText(stringConverter.dateFormatDDMMYYYY(item.getDate()));
        holder.txtAmount.setText(stringConverter.numberFormat(item.getAmountDetail()+""));
        holder.txtBusinessType.setText(item.getBusinessType());
        holder.txtCompanyName.setText(item.getCompanyName());
        holder.txtParticipant.setText(item.getParticipant());
        holder.txtPlace.setText(item.getEntertainPlace());
        holder.txtPosition.setText(item.getPosition());
        holder.txtProject.setText(item.getProject());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtAmount, txtPlace, txtProject, txtParticipant, txtPosition, txtCompanyName, txtBusinessType;
        ViewGroup layoutItem;

        public ViewHolder(View itemView) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.layoutItem);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtPlace = itemView.findViewById(R.id.txtPlace);
            txtProject = itemView.findViewById(R.id.txtProject);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtParticipant = itemView.findViewById(R.id.txtParticipant);
            txtPosition = itemView.findViewById(R.id.txtPosition);
            txtCompanyName = itemView.findViewById(R.id.txtCompanyName);
            txtBusinessType = itemView.findViewById(R.id.txtBusinessType);
        }
    }
}
/*
public class EntertainReimbursementDetailAdapter extends ArrayAdapter<EntertainReimbursementDetail> {
    Context context;
    List<EntertainReimbursementDetail> list;
    LayoutInflater inflater;
    private SparseBooleanArray mSelectedItemsIds;


    public EntertainReimbursementDetailAdapter(@NonNull Context context, int resource, @NonNull List<EntertainReimbursementDetail> objects) {
        super(context, resource, objects);
        mSelectedItemsIds=new SparseBooleanArray();
        this.context = context;
        this.list = objects;
        inflater=LayoutInflater.from(context);
    }

    private class ViewHolder {
        TextView txtDate, txtAmount, txtPlace, txtProject, txtParticipant, txtPosition, txtCompanyName, txtBusinessType;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View itemView = convertView;
        final EntertainReimbursementDetailAdapter.ViewHolder holder;

        if(convertView==null)
        {
            holder = new EntertainReimbursementDetailAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.list_entertain_reimbursement_detail, null);
            // Locate the TextViews in listview_item.xml
            holder.txtDate = convertView.findViewById(R.id.txtDate);
            holder.txtPlace = convertView.findViewById(R.id.txtPlace);
            holder.txtProject = convertView.findViewById(R.id.txtProject);
            holder.txtAmount = convertView.findViewById(R.id.txtAmount);
            holder.txtParticipant = convertView.findViewById(R.id.txtParticipant);
            holder.txtPosition = convertView.findViewById(R.id.txtPosition);
            holder.txtCompanyName = convertView.findViewById(R.id.txtCompanyName);
            holder.txtBusinessType = convertView.findViewById(R.id.txtBusinessType);
            convertView.setTag(holder);
        }
        else
        {
            holder = (EntertainReimbursementDetailAdapter.ViewHolder) convertView.getTag();
        }



        StringConverter stringConverter=  new StringConverter();
        EntertainReimbursementDetail item = list.get(position);



        holder.txtDate.setText(stringConverter.dateFormatDDMMYYYY(item.getDate()));
        holder.txtAmount.setText(stringConverter.numberFormat(item.getAmountDetail()+""));
        holder.txtBusinessType.setText(item.getBusinessType());
        holder.txtCompanyName.setText(item.getCompanyName());
        holder.txtParticipant.setText(item.getParticipant());
        holder.txtPlace.setText(item.getEntertainPlace());
        holder.txtPosition.setText(item.getPosition());
        holder.txtProject.setText(item.getProject());

        return convertView;
    }

    @Override
    public void remove(@Nullable EntertainReimbursementDetail object) {
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
}



*/
