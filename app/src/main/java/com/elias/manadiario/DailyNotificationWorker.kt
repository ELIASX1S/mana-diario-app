package com.elias.manadiario

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class DailyNotificationWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        NotificationHelper.showDailyNotification(applicationContext)
        return Result.success()
    }
}