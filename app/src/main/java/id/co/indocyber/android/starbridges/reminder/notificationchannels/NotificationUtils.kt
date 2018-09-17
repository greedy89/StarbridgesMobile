package id.co.indocyber.android.starbridges.reminder.notificationchannels

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import id.co.indocyber.android.starbridges.R
import id.co.indocyber.android.starbridges.activity.BeaconDetailActivity
import id.co.indocyber.android.starbridges.activity.LoginActivity

class NotificationUtils(private val context: Context) {
    companion object {
        private const val PM_NOTIFICATION_ID = 10
        private const val ACTIVITY_NOTIFICATION_ID = 11

        private const val PRIVATE_MESSAGES_CHANNEL_ID = "private_messages"
        private const val NEW_ACTIVITY_CHANNEL_ID = "new_activity"
        private const val NOTIFICATION_ID = 0
    }

    private val notificationManager by lazy {
        context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
    }

    init {
        createDefaultChannels()
    }

    fun showPMNotification(message: String, title: String) {
        val contentIntent = Intent(context, LoginActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, contentIntent, 0)
        val notification = NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(contentPendingIntent)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(message))
                /**
                 * further customizations, like sound, light color, vibration pattern etc
                 */
                .setChannelId(PRIVATE_MESSAGES_CHANNEL_ID)
                .build()
        notificationManager.notify(PM_NOTIFICATION_ID, notification)
    }

    fun showBeaconNotification(message: String, title: String, beaconFindedString: String) {
        val contentIntent = Intent(context, BeaconDetailActivity::class.java)
        contentIntent.putExtra("beaconFindedString", beaconFindedString)
        val contentPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, contentIntent, 0)
        val notification = NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(contentPendingIntent)
//                .setStyle(NotificationCompat.BigTextStyle()
//                        .bigText(message))
                .setSubText(message)
                /**
                 * further customizations, like sound, light color, vibration pattern etc
                 */
                .setChannelId(PRIVATE_MESSAGES_CHANNEL_ID)
                .build()
        notificationManager.notify(PM_NOTIFICATION_ID, notification)
    }

    fun showNewActivityNotification(message: String) {
        val notification = NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_icon)
                .setContentTitle("New like on your post")
                .setContentText(message)
                /**
                 * further customizations
                 */
                .setChannelId(NEW_ACTIVITY_CHANNEL_ID)
                .build()
        notificationManager.notify(ACTIVITY_NOTIFICATION_ID, notification)
    }

    @SuppressLint("NewApi")
    fun deletePmNotificationChannel() {
        isAndroidO {
            notificationManager.deleteNotificationChannel(PRIVATE_MESSAGES_CHANNEL_ID)
        }
    }

    @SuppressLint("NewApi")
    fun deleteNewActivityNotificationChannel() {
        isAndroidO {
            notificationManager.deleteNotificationChannel(NEW_ACTIVITY_CHANNEL_ID)
        }
    }

    @SuppressLint("NewApi")
    fun getPmNotificationChannel(): NotificationChannel? {
        isAndroidO {
            return notificationManager.getNotificationChannel(PRIVATE_MESSAGES_CHANNEL_ID)
        }
        return null
    }

    @SuppressLint("NewApi")
    fun getNewActivityNotificationChannel(): NotificationChannel? {
        isAndroidO {
            return notificationManager.getNotificationChannel(NEW_ACTIVITY_CHANNEL_ID)
        }
        return null
    }

    @SuppressLint("NewApi")
    fun notificationBlocked(channel: NotificationChannel): Boolean {
        isAndroidO {
            return channel.importance == NotificationManager.IMPORTANCE_NONE
        }
        return notificationManager.areNotificationsEnabled() // fallback for pre-O devices
    }

    @SuppressLint("NewApi")
    private fun createDefaultChannels() {
        // create the channels
        isAndroidO {
            val privateMessagesChannel = NotificationChannel(
                    PRIVATE_MESSAGES_CHANNEL_ID,
                    context.getString(R.string.pm_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT)

            with(privateMessagesChannel) {
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400)
                description = "Notification category for private messages"
            }

            val newActivityChannel = NotificationChannel(NEW_ACTIVITY_CHANNEL_ID,
                    context.getString(R.string.new_activity_channel_name), NotificationManager.IMPORTANCE_DEFAULT)

            notificationManager.createNotificationChannel(privateMessagesChannel)
            notificationManager.createNotificationChannel(newActivityChannel)
        }
    }
}