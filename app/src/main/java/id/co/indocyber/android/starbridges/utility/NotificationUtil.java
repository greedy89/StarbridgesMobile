package id.co.indocyber.android.starbridges.utility;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;

import java.io.File;

import id.co.indocyber.android.starbridges.R;

public class NotificationUtil {
    public static final String CHANNEL_ID = "Starbridges";
    public static final String CHANNEL_NAME = "Starbridges";
    public static final String CHANNEL_DESCRIPTION = "Starbridges";
    public static void showNotification(Context context, String contentText)
    {
        int notificationId = 0;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_starbridges)
                .setContentTitle("SAP Notification")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(notificationId, builder.build());
    }

    public static void showDownloadSuccessNotification(Context context, String title, String contentText, String pathFile) {

        int NOTIFICATION_ID = 234;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            mChannel.setDescription(CHANNEL_DESCRIPTION);
            //mChannel.enableLights(true);
            //mChannel.setLightColor(Color.RED);
            //mChannel.enableVibration(true);
            //mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(true);

            if (notificationManager != null) {

                notificationManager.createNotificationChannel(mChannel);
            }

        }
File file = new File(pathFile);
        Uri uri = Uri.fromFile(file);
        Intent resultIntent = new Intent(Intent.ACTION_VIEW);

        String mime = "*/*";
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        if(mimeTypeMap.hasExtension(mimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {
            mime = mimeTypeMap.getMimeTypeFromExtension(mimeTypeMap.getFileExtensionFromUrl(uri.toString()));
        }
        resultIntent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                ? FileProvider.getUriForFile(context, "com.example.android.fileprovider", file) : uri,mime);
        resultIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);





        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(contentText)
                //.setStyle(new NotificationCompat.BigTextStyle().bigText("Notice that the NotificationCompat.Builder constructor requires that you provide a channel ID. This is required for compatibility with Android 8.0 (API level 26) and higher, but is ignored by older versions By default, the notification's text content is truncated to fit one line. If you want your notification to be longer, you can enable an expandable notification by adding a style template with setStyle(). For example, the following code creates a larger text area"))
                .setSmallIcon(R.mipmap.ic_launcher2_round)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setChannelId(CHANNEL_ID)
                .setAutoCancel(true);
        //.setColor(getResources().getColor(android.R.color.holo_red_dark))
        //.addAction(R.drawable.ic_launcher_foreground, "Call", resultPendingIntent)
        //.addAction(R.drawable.ic_launcher_foreground, "More", resultPendingIntent)
        //.addAction(R.drawable.ic_launcher_foreground, "And more", resultPendingIntent);


        if (notificationManager != null) {

            //notificationManager.notify(NOTIFICATION_ID, builder.build());

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
        }
    }
}
