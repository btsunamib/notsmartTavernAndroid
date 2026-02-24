package com.sillytavern.android

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SillyTavernApp : Application() {
    
    companion object {
        const val CHANNEL_ID_CHAT = "chat_channel"
        const val CHANNEL_ID_DOWNLOAD = "download_channel"
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chatChannel = NotificationChannel(
                CHANNEL_ID_CHAT,
                getString(R.string.chat_notifications),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.chat_notifications_desc)
            }
            
            val downloadChannel = NotificationChannel(
                CHANNEL_ID_DOWNLOAD,
                getString(R.string.download_notifications),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.download_notifications_desc)
            }
            
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannels(listOf(chatChannel, downloadChannel))
        }
    }
}
