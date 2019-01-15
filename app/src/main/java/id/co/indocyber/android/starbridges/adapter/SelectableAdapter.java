package id.co.indocyber.android.starbridges.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import java.util.List;

public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder> extends  RecyclerView.Adapter<VH> {
    private static final String TAG = SelectableAdapter.class.getSimpleName();

    private SparseBooleanArray mSelectedItemsIds;

    public SelectableAdapter() {
        mSelectedItemsIds = new SparseBooleanArray();
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

    public Boolean isSelected(int position) {
        return getSelectedIds().get(position);
    }

}
