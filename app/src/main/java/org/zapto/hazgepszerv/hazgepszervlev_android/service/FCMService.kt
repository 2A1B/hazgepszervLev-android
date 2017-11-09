package org.zapto.hazgepszerv.hazgepszervlev_android.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import org.zapto.hazgepszerv.hazgepszervlev_android.activities.MainActivity
import org.zapto.hazgepszerv.hazgepszervlev_android.R
import android.app.ActivityManager
import android.graphics.Color
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.preference.PreferenceManager

class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        var notificationTitle: String? = null
        var notificationBody: String? = null
        var dataTitle: String? = null
        var dataMessage: String? = null

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val notificationEnabled = sharedPreferences.getBoolean("notifications_new_message", false)
        val inAppNotificationEnabled = sharedPreferences.getBoolean("notifications_in_app", true)

        if (remoteMessage!!.data.isNotEmpty()) {
            dataTitle = remoteMessage.data["title"]
            dataMessage = remoteMessage.data["message"]
        }

        if (remoteMessage.notification != null) {
            notificationTitle = remoteMessage.notification.title
            notificationBody = remoteMessage.notification.body
        }

        if(notificationEnabled == true) {
            if (applicationInForeground()) {
                if(inAppNotificationEnabled) {
                    val intent = Intent("myFunction")
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                }
            } else {
                sendNotification(notificationTitle, notificationBody, dataTitle, dataMessage)
            }
        }
    }

    private fun sendNotification(notificationTitle: String?, notificationBody: String?, dataTitle: String?, dataMessage: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("title", dataTitle)
        intent.putExtra("message", dataMessage)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(0, 1000, 200, 1000))
                .setLights(Color.MAGENTA, 500, 500)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent) as NotificationCompat.Builder

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun applicationInForeground(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services = activityManager.runningAppProcesses
        var isActivityFound = false

        if (services[0].processName.equals(packageName, ignoreCase = true)) {
            isActivityFound = true
        }

        return isActivityFound
    }
}