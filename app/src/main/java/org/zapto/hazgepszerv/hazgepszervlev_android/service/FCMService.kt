package org.zapto.hazgepszerv.hazgepszervlev_android.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import org.zapto.hazgepszerv.hazgepszervlev_android.MainActivity
import org.zapto.hazgepszerv.hazgepszervlev_android.R

class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        var notificationTitle: String? = null
        var notificationBody: String? = null
        var dataTitle: String? = null
        var dataMessage: String? = null

        if (remoteMessage!!.data.isNotEmpty()) {
            dataTitle = remoteMessage.data["title"]
            dataMessage = remoteMessage.data["message"]
        }

        if (remoteMessage.notification != null) {
            notificationTitle = remoteMessage.notification.title
            notificationBody = remoteMessage.notification.body
        }

        sendNotification(notificationTitle, notificationBody, dataTitle, dataMessage)
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
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent) as NotificationCompat.Builder

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0, notificationBuilder.build())
    }
}