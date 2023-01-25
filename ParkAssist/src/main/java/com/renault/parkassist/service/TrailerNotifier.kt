package com.renault.parkassist.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.renault.parkassist.R
import com.renault.parkassist.repository.surroundview.FeatureConfig
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.TrailerPresence
import org.koin.core.KoinComponent
import org.koin.core.inject

class TrailerNotifier(private val context: Context) : KoinComponent {

    companion object {
        private const val NOTIFICATION_ID = 111
        private const val CHANNEL_ID = "com.renault.trailer"
    }

    private val surroundRepository: IExtSurroundViewRepository by inject()

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val channelName =
        R.string.rlb_parkassist_trailer_notif_channel

    private val contentTitleString =
        R.string.rlb_parkassist_trailer_notif_title

    private val contentTextString
        get() =
            if (surroundRepository.featureConfig == FeatureConfig.AVM)
                R.string.rlb_parkassist_trailer_avm_notif_content
            else R.string.rlb_parkassist_trailer_rvc_notif_content

    private val notification
        get() = Notification.Builder(
            context,
            CHANNEL_ID
        )
            .setContentTitle(context.getString(contentTitleString))
            .setContentText(context.getString(contentTextString))
            .setSmallIcon(R.drawable.ric_adas_trailer)
            .setOngoing(true)
            .build()

    private val channel
        get() =
            NotificationChannel(
                CHANNEL_ID,
                context.getString(channelName),
                NotificationManager.IMPORTANCE_LOW
            )

    fun startListening() {
        if (surroundRepository.isTrailerViewSupported) {
            createChannelIfNecessary()
            surroundRepository.trailerPresence.observeForever { presence ->
                if (presence == TrailerPresence.TRAILER_PRESENCE_DETECTED) startNotify()
                else cancel()
            }
        }
    }

    fun refresh() {
        cancel()
        if (surroundRepository.trailerPresence.value == TrailerPresence.TRAILER_PRESENCE_DETECTED)
            startNotify()
    }

    private fun createChannelIfNecessary() {
        notificationManager.getNotificationChannel(CHANNEL_ID)
            ?: notificationManager.createNotificationChannel(channel)
    }

    private fun startNotify() {
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun cancel() {
        notificationManager.cancel(NOTIFICATION_ID)
    }
}