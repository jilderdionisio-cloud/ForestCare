package com.plant.forestcare.data.local.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.plant.forestcare.R

class WateringNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val plantName = inputData.getString(KEY_PLANT_NAME) ?: "tu planta"
        
        showNotification(plantName)
        
        return Result.success()
    }

    private fun showNotification(plantName: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        val channelId = "watering_alerts_channel"
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Alertas de ForestCare",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones para el riego de plantas"
            }
            notificationManager.createNotificationChannel(channel)
        }
        
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_menu_my_calendar) // Standard icon
            .setContentTitle("¡Hora de regar!")
            .setContentText("¡Es hora de regar tu $plantName!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
            
        notificationManager.notify(plantName.hashCode(), notification)
    }

    companion object {
        const val KEY_PLANT_NAME = "plant_name"
    }
}
