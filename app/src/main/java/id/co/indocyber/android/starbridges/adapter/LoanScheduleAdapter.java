package id.co.indocyber.android.starbridges.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.model.ListLoanSchedule.ReturnValue;
import id.co.indocyber.android.starbridges.network.StringConverter;

public class LoanScheduleAdapter extends ArrayAdapter<ReturnValue> {
    private final Context context;
    private final List<ReturnValue> listTransaction;

    public LoanScheduleAdapter(@NonNull Context context, List<ReturnValue> listTransaction) {
        super(context, R.layout.list_loan_schedule, listTransaction);
        this.context = context;
        this.listTransaction = listTransaction;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // get rowview from inflater
        View rowView = inflater.inflate(R.layout.list_loan_schedule, parent, false);

        // get the text view from the rowView
        TextView txtIsProcessLoanSchedule = (TextView) rowView.findViewById(R.id.txtIsProcessLoanSchedule);
        TextView txtIsCloseStepLoanSchedule = (TextView) rowView.findViewById(R.id.txtIsCloseStepLoanSchedule);
        TextView txtAmountLoanSchedule = (TextView) rowView.findViewById(R.id.txtAmountLoanSchedule);
        TextView txtProcessPeriodLoanSchedule=(TextView)rowView.findViewById(R.id.txtProcessPeriodLoanSchedule);

        StringConverter stringConverter=new StringConverter();

        String isProcess=listTransaction.get(position).getIsProcessed()==false?"Scheduled":"Processed";
        String isClose=listTransaction.get(position).getIsClosed()==false?"Scheduled":"Closed";


        txtAmountLoanSchedule.setText("Amount: "+ stringConverter.numberFormat(listTransaction.get(position).getAmount()+""));
        txtIsProcessLoanSchedule.setText("Is Processed: "+isProcess);
        txtIsCloseStepLoanSchedule.setText("Is Closed: "+isClose);
        txtProcessPeriodLoanSchedule.setText(stringConverter.dateFormatMMMMYYYY(listTransaction.get(position).getProcessPeriod()));

        return rowView;
//        View itemView = convertView;


    }
}