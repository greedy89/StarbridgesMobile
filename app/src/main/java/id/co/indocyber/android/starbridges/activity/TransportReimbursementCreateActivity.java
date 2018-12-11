package id.co.indocyber.android.starbridges.activity;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.utility.MonthYearPickerDialog;

public class TransportReimbursementCreateActivity extends AppCompatActivity {

    private EditText txtPeriodCreateTR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_reimbursement_create);

        txtPeriodCreateTR = (EditText)findViewById(R.id.txtPeriodCreateTR);

        txtPeriodCreateTR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MonthYearPickerDialog pickerDialog = new MonthYearPickerDialog();
                pickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
                        Toast.makeText(TransportReimbursementCreateActivity.this, year + "-" + month, Toast.LENGTH_SHORT).show();
                    }
                });
                pickerDialog.show(getSupportFragmentManager(), "MonthYearPickerDialog");
            }
        });
    }

    private DatePickerDialog createDialogWithoutDateField() {
        DatePickerDialog dpd = new DatePickerDialog(this, null, 2014, 1, 24);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
        }
        return dpd;
    }
}
