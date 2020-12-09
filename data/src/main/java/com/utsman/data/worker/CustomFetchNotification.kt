/*
 * Created by Muhammad Utsman on 9/12/20 1:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.DownloadNotification
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchNotificationManager
import com.utsman.abstraction.ext.logi
import com.utsman.abstraction.ext.toSumReadable
import com.utsman.data.R

class CustomFetchNotification(
    private val context: Context,
    override val broadcastReceiver: BroadcastReceiver,
    override val notificationManagerAction: String
) : FetchNotificationManager {
    override fun cancelNotification(notificationId: Int) {
        logi("canceling....")
    }

    override fun cancelOngoingNotifications() {
        logi("canceling ongoing....")
    }

    override fun createNotificationChannels(
        context: Context,
        notificationManager: NotificationManager
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //val channelId = context.getString(R.string.fetch_notification_default_channel_id)
            var channel: NotificationChannel? = notificationManager.getNotificationChannel("store_app_notification")
            if (channel == null) {
                //val channelName = context.getString(R.string.fetch_notification_default_channel_name)
                channel = NotificationChannel("store_app_notification", "Notification Download", NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    override fun getActionPendingIntent(
        downloadNotification: DownloadNotification,
        actionType: DownloadNotification.ActionType
    ): PendingIntent {
        val intent = Intent(context, Class.forName("com.utsman.storeapps.MainActivity"))
        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    override fun getChannelId(notificationId: Int, context: Context): String {
        return "store_app_notification"
    }

    override fun getDownloadNotificationTitle(download: Download): String {
        return download.namespace
    }

    override fun getFetchInstanceForNamespace(namespace: String): Fetch {
        return Fetch.getDefaultInstance()
    }

    override fun getGroupActionPendingIntent(
        groupId: Int,
        downloadNotifications: List<DownloadNotification>,
        actionType: DownloadNotification.ActionType
    ): PendingIntent {
        val intent = Intent(context, Class.forName("com.utsman.storeapps.MainActivity"))
        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    override fun getNotificationBuilder(
        notificationId: Int,
        groupId: Int
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, "store_app_notification")
                       .setSmallIcon(R.drawable.ic_fluent_arrow_download_16_filled)
    }

    override fun getNotificationTimeOutMillis(): Long {
        return 30 * 1000
    }

    override fun getSubtitleText(
        context: Context,
        downloadNotification: DownloadNotification
    ): String {
        return downloadNotification.downloaded.toSumReadable() ?: ""
    }

    override fun notify(groupId: Int) {
        logi("notify....")
        NotificationManagerCompat.from(context).apply {
            notify(12, getNotificationBuilder(12, groupId).build())
        }
    }

    override fun postDownloadUpdate(download: Download): Boolean {
        return true
    }

    override fun registerBroadcastReceiver() {
        //
    }

    override fun shouldCancelNotification(downloadNotification: DownloadNotification): Boolean {
        logi("should cancel ---")
        return true
    }

    override fun shouldUpdateNotification(downloadNotification: DownloadNotification): Boolean {
        logi("should update ---")
        return true
    }

    override fun unregisterBroadcastReceiver() {
        //
    }

    override fun updateGroupSummaryNotification(
        groupId: Int,
        notificationBuilder: NotificationCompat.Builder,
        downloadNotifications: List<DownloadNotification>,
        context: Context
    ): Boolean {
        logi("update group ---")
        return true
    }

    override fun updateNotification(
        notificationBuilder: NotificationCompat.Builder,
        downloadNotification: DownloadNotification,
        context: Context
    ) {
        logi("update notification --> ${downloadNotification.progress}")
        notificationBuilder.setContentTitle(downloadNotification.title)
            .setContentText(downloadNotification.namespace)
            .setProgress(100, downloadNotification.progress, false)
    }
}