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
import com.example.android.starbridges.activity.LeaveRequestDetailActivity;
import com.example.android.starbridges.model.listdraftleaverequest.ReturnValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 5/22/2018.
 */

public class ListDraftLeaveRequestAdapter extends ArrayAdapter<ReturnValue> {
    private final Context context;
    private final List<ReturnValue> draftLeaveRequestList;
    public static List<String> listID = new ArrayList<>();

    public ListDraftLeaveRequestAdapter(Context context, List<ReturnValue> draftLeaveRequestList){
        super(context, R.layout.list_draft_leave_request, draftLeaveRequestList);

        this.context = context;
        this.draftLeaveRequestList = draftLeaveRequestList;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // create inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // get rowview from inflater
        View rowView = inflater.inflate(R.layout.list_draft_leave_request, parent, false);

        // get the text view from the rowView
        TextView requestType = (TextView) rowView.findViewById(R.id.requestTypeView);
        TextView leave = (TextView) rowView.findViewById(R.id.leaveView);
        TextView unitReduce = (TextView) rowView.findViewById(R.id.unitReduceView);
        TextView notes = (TextView) rowView.findViewById(R.id.notesView);
        Button btnEdit = (Button) rowView.findViewById(R.id.btnEditDraft);
        CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);

        requestType.setText(draftLeaveRequestList.get(position).getLeaveType());
        leave.setText(draftLeaveRequestList.get(position).getStartLeave() + " - " + draftLeaveRequestList.get(position).getEndLeave());
        unitReduce.setText(draftLeaveRequestList.get(position).getTotalUnit().toString());
        notes.setText(draftLeaveRequestList.get(position).getNotes());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LeaveRequestDetailActivity.class);
                intent.putExtra("ID", draftLeaveRequestList.get(position).getID());
                context.startActivity(intent);
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CompoundButton) v).isChecked())
                    listID.add( draftLeaveRequestList.get(position).getID()); // add to cb array
                else
                    listID.remove(draftLeaveRequestList.get(position).getID());
            }
        });

        // return rowView
        return rowView;
    }
}
