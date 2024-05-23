package com.example.practice

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import kotlinx.coroutines.*

class Service : LifecycleService() {
    val NOTI_ID = 99
    val CHANNEL_ID = "FGS153"
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val serviceChannel = NotificationChannel(CHANNEL_ID,"FOREGROUND", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notificationBuilder = NotificationCompat.Builder(this,CHANNEL_ID) // compat은 버전 호환성
            .setContentTitle("Foreground Service")
            .setSmallIcon(R.drawable.ic_flo_logo)
            .setProgress(1000,0,false)

        val notification = notificationBuilder.build()
        startForeground(NOTI_ID,notification)
        countNumbers(notificationBuilder)
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }
    private fun countNumbers(notificationBuilder: NotificationCompat.Builder) {
        serviceScope.launch {
            for (i in 1..1000) {
                notificationBuilder.setProgress(1000, i, false)
                val notification = notificationBuilder.build()
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(NOTI_ID, notification)
                delay(100)
            }
            notificationBuilder.setContentText("Count complete!")
                .setProgress(0, 0, false)
            val notification = notificationBuilder.build()
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(NOTI_ID, notification)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}

