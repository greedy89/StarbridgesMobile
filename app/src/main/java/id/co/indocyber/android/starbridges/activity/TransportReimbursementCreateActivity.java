package id.co.indocyber.android.starbridges.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.utility.MonthYearPickerDialog;
import id.co.indocyber.android.starbridges.utility.YearMonthPickerDialogCustom;

public class TransportReimbursementCreateActivity extends AppCompatActivity {

    private EditText txtPeriodCreateTR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_reimbursement_create);

        txtPeriodCreateTR = (EditText)findViewById(R.id.txtPeriodCreateTR);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2018,10,01);


        final YearMonthPickerDialogCustom yearMonthPickerDialog = new YearMonthPickerDialogCustom(this, new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month) {
                Calendar calendar=Calendar.getInstance();
                calendar.add(Calendar.MONTH,-2);
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 01);
                calendar.set(Calendar.HOUR,0);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                Calendar selectedCalendar=Calendar.getInstance();
                selectedCalendar.set(year,month, 01);
                selectedCalendar.set(Calendar.HOUR,0);
                selectedCalendar.set(Calendar.HOUR_OF_DAY, 0);
                selectedCalendar.set(Calendar.MINUTE, 0);
                selectedCalendar.set(Calendar.SECOND, 0);
                selectedCalendar.set(Calendar.MILLISECOND, 1);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
                if(selectedCalendar.after(calendar))
                    txtPeriodCreateTR.setText(dateFormat.format(selectedCalendar.getTime()));

            }
        });



        txtPeriodCreateTR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearMonthPickerDialog.show();

            }
        });

    }


    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_month_year_picker);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        dialog.show();
        dialog.getWindow().setAttributes(lp);
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
