package com.example.android.starbridges.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.android.starbridges.R;
import com.example.android.starbridges.activity.LeaveCancelationDetailActivity;
import com.example.android.starbridges.activity.ReimburseDetailActivity;
import com.example.android.starbridges.model.ListDraftReimbursement.ReturnValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListDraftReimbursementAdapter extends ArrayAdapter<ReturnValue> {
    private final Context context;
    private final List<ReturnValue> draftLeaveReimbursement;
    public static List<String> listID = new ArrayList<>();

    public ListDraftReimbursementAdapter(Context context, List<ReturnValue> draftLeaveReimbursement){
        super(context, R.layout.list_draft_leave_cancelation, draftLeaveReimbursement);

        this.context = context;
        this.draftLeaveReimbursement = draftLeaveReimbursement;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // create inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // get rowview from inflater
        View rowView = inflater.inflate(R.layout.list_draft_reimburse, parent, false);

        // get the text view from the rowView
        TextView txtDesciptionDraftReimburse = (TextView) rowView.findViewById(R.id.txtDesciptionDraftReimburse);
        TextView txtAmountDraftReimburse = (TextView) rowView.findViewById(R.id.txtAmountDraftReimburse);
        TextView txtTypeDraftReimburse = (TextView) rowView.findViewById(R.id.txtTypeDraftReimburse);
        TextView txtTransactionDateDraftReimburse = (TextView) rowView.findViewById(R.id.txtTransactionDateDraftReimburse);
        Button btnEditDraft = (Button) rowView.findViewById(R.id.btnEditDraft);
        CheckBox chcDraftReimburse = (CheckBox) rowView.findViewById(R.id.chcDraftReimburse);

        txtDesciptionDraftReimburse.setText(draftLeaveReimbursement.get(position).getDescription());
        txtAmountDraftReimburse.setText( draftLeaveReimbursement.get(position).getAmount());
        txtTypeDraftReimburse.setText( draftLeaveReimbursement.get(position).getType());
        txtTransactionDateDraftReimburse.setText(dateFormat(draftLeaveReimbursement.get(position).getTransactionDate()) );

        btnEditDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReimburseDetailActivity.class);
                intent.putExtra("id", draftLeaveReimbursement.get(position).getID());
                context.startActivity(intent);
            }
        });

        chcDraftReimburse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CompoundButton) v).isChecked())
                    listID.add(draftLeaveReimbursement.get(position).getID()); // add to cb array
                else
                    listID.remove(draftLeaveReimbursement.get(position).getID());
            }
        });

        // return rowView
        return rowView;
    }

    public String dateFormat(String dateString)
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        DateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        Date date1;
        String result;
        try{
            date1=df.parse(dateString);
            result=sdf.format(date1);
        }catch (Exception e)
        {
            result="";
        }
        return result;
    }
}

