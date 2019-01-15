package id.co.indocyber.android.starbridges.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import id.co.indocyber.android.starbridges.activity.OvertimeDetailActivity;

public class MessageUtil {
    public static void showMessage(Context context, String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();

    }
}
