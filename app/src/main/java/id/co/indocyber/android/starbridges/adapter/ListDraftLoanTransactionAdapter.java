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
import id.co.indocyber.android.starbridges.model.ListDraftTransactionLoan.ReturnValue;
import id.co.indocyber.android.starbridges.network.StringConverter;
import id.co.indocyber.android.starbridges.utility.SharedPreferenceUtils;

public class ListDraftLoanTransactionAdapter extends ArrayAdapter<ReturnValue> {
    private final Context context;
    private final List<ReturnValue> listTransaction;
    private List<String> listID = new ArrayList<>();
    private LayoutInflater inflater;
    private SparseBooleanArray mSelectedItemsIds;

    public ListDraftLoanTransactionAdapter(@NonNull Context context, List<ReturnValue> listTransaction) {
        super(context, R.layout.list_draft_loan_transaction, listTransaction);
        this.context = context;
        this.listTransaction = listTransaction;
        mSelectedItemsIds = new SparseBooleanArray();
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // get rowview from inflater
        View rowView = inflater.inflate(R.layout.list_draft_loan_transaction, parent, false);

        // get the text view from the rowView
        TextView txtTransactionTypeTransactionLoan = (TextView) rowView.findViewById(R.id.txtTransactionTypeTransactionLoan);
        TextView txtPolicyTransactionLoan = (TextView) rowView.findViewById(R.id.txtPolicyTransactionLoan);
        TextView txtAmountTransactionLoan = (TextView) rowView.findViewById(R.id.txtAmountTransactionLoan);
        TextView txtStartDateTransactionLoan = (TextView) rowView.findViewById(R.id.txtStartDateTransactionLoan);


        StringConverter stringConverter = new StringConverter();

        txtAmountTransactionLoan.setText("Amount: " + stringConverter.numberFormat(listTransaction.get(position).getAmount() + ""));
        txtTransactionTypeTransactionLoan.setText(listTransaction.get(position).getTransactionType());
        txtPolicyTransactionLoan.setText(listTransaction.get(position).getPolicy());
        txtStartDateTransactionLoan.setText(stringConverter.dateFormat3(listTransaction.get(position).getStartDate()));

        return rowView;
//        View itemView = convertView;


    }

    @Override
    public void remove(@Nullable ReturnValue object) {
        listTransaction.remove(object);
        listID.add(object.getID());
        SharedPreferenceUtils.setSetting(context,"lstIdSelected", listID.toString());
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