package id.co.indocyber.android.starbridges.reminder.alarmManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import id.co.indocyber.android.starbridges.R;
import id.co.indocyber.android.starbridges.activity.BeaconDetailActivity;
import id.co.indocyber.android.starbridges.activity.LoginActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public final class Notification {
    static NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIVATE_MESSAGES_CHANNEL_ID = "private_messages";
    private static String CHANELL_ID = "id.co.indocyber.android.starbridges.1001";

    public static void deliverNotification(Context context, String title, String message) {
            mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            Intent contentIntent = new Intent(context, LoginActivity.class);
            PendingIntent contentPendingIntent = PendingIntent.getActivity
                    (context, NOTIFICATION_ID, contentIntent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                    .setSmallIcon(R.mipmap.ic_launcher2_round)
                    .setSmallIcon(R.mipmap.ic_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(contentPendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setChannelId(PRIVATE_MESSAGES_CHANNEL_ID)
                    .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(message));
            mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public static void showNotification(Context ctx,String title,String Message){
        NotificationManager notifManager = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent contentIntent = new Intent(ctx, LoginActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(ctx,0,contentIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);
        builder.setContentTitle(title);
        builder.setContentText(Message);
        builder.setContentIntent(contentPendingIntent);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.mipmap.ic_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(),R.mipmap.ic_icon));
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(Message));
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);


        if(android.os.Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            NotificationChannel channelManager = new NotificationChannel(CHANELL_ID,"amos",NotificationManager.IMPORTANCE_HIGH);
            channelManager.enableVibration(true);
            channelManager.setVibrationPattern(new long[1000]);
            builder.setChannelId(CHANELL_ID);
            notifManager.createNotificationChannel(channelManager);
        }
        notifManager.notify(0,builder.build());
    }

    public static void showBeaconNotification(Context context, String title, String message, String beaconFindedString) {
        mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Intent contentIntent = new Intent(context, BeaconDetailActivity.class);
        contentIntent.putExtra("beaconFindedString", beaconFindedString);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                    .setSmallIcon(R.mipmap.ic_launcher2_round)
                .setSmallIcon(R.mipmap.ic_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setChannelId(PRIVATE_MESSAGES_CHANNEL_ID)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message));
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }


}
