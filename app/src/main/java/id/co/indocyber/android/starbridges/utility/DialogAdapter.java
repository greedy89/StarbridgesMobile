package id.co.indocyber.android.starbridges.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;


public class DialogAdapter {

    public static void makeToast(Context context, String message){
        new Toast(context).makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showMessageDialog(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showAlertDialogFinish(final Activity context, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                context.finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void goingToNextActivity(final Activity activity, final Activity target, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activity.startActivity(new Intent(activity, target.getClass()));
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void goingToWeb(final Activity activity, final Uri link, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, link);
                activity.startActivityForResult(launchBrowser, 5);
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public static  void showDialogOneBtn(final Activity ctx, String title ,String msg,String positiveBtn,DialogInterface.OnClickListener listenerPositiveBtn ){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(false);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveBtn, listenerPositiveBtn);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static  void showDialogTwoBtn(final Activity ctx, String title , String msg, String positiveBtn, DialogInterface.OnClickListener listenerPositiveBtn, String negativeBtn, DialogInterface.OnClickListener listenerNegativeBtn ){

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(false);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveBtn, listenerPositiveBtn);
        builder.setNegativeButton(negativeBtn, listenerNegativeBtn);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
