    package com.example.practice
    import android.app.Service
    import android.content.Intent
    import android.os.IBinder
    import android.app.NotificationManager
    import android.app.NotificationChannel
    import android.app.PendingIntent
    import android.content.Context
    import android.os.Build
    import androidx.core.app.NotificationCompat
    import androidx.core.app.NotificationManagerCompat
    import kotlinx.coroutines.*
    class ForegroundService : Service() {
        private lateinit var pendingIntent: PendingIntent
        private val serviceScope = CoroutineScope(Dispatchers.Main)

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            createNotificationChannel()
            val notificationIntent = Intent(this, MainActivity::class.java)
            pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Progress Service")
                .setContentText("Initializing...")
                .setSmallIcon(R.drawable.ic_flo_logo)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setProgress(1000, 0, false)
                .build()

            startForeground(1, notification)
            countInBackground()

            return START_NOT_STICKY
        }

        private fun countInBackground() {
            serviceScope.launch {
                for (i in 1..1000) {
                    delay(10)
                    val progressNotification = NotificationCompat.Builder(this@ForegroundService, CHANNEL_ID)
                        .setContentTitle("Counting Progress")
                        .setContentText("Count: $i")
                        .setSmallIcon(R.drawable.ic_flo_logo)
                        .setContentIntent(pendingIntent)
                        .setOnlyAlertOnce(true)
                        .setProgress(1000, i, false)
                        .build()
                    NotificationManagerCompat.from(this@ForegroundService).notify(1, progressNotification)
                }
            }
        }

        override fun onBind(intent: Intent?): IBinder? = null

        private fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel", NotificationManager.IMPORTANCE_LOW)
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(channel)
            }
        }

        companion object {
            const val CHANNEL_ID = "ForegroundServiceChannel"
        }
    }
