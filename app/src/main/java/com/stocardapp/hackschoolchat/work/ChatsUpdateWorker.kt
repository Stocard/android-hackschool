package com.stocardapp.hackschoolchat.work

import androidx.work.*
import com.stocardapp.hackschoolchat.database.Updater
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ChatsUpdateWorker : Worker() {

    private val updater by lazy { Updater(applicationContext) }

    override fun doWork(): Result {
        Timber.d("Doing work")
        return try {
            updater.update()
            Result.SUCCESS
        } catch (e: Throwable) {
            e.printStackTrace()
            Result.FAILURE
        }
    }

    companion object {
        fun schedule(delay: Long? = null) {
            Timber.tag("UpdateWorker").d("Scheduling work in $delay seconds.")
            val work: OneTimeWorkRequest = OneTimeWorkRequest.Builder(ChatsUpdateWorker::class.java).apply {
                if (delay != null) setInitialDelay(delay, TimeUnit.SECONDS)
            }.build()
            WorkManager.getInstance().enqueue(work)
        }

        /**
         * CHECK: Seems as if periodic can only tun once every 15 minutes...
         */
        fun startPeriodic() {
            val constraints: androidx.work.Constraints = androidx.work.Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

            val work: PeriodicWorkRequest = PeriodicWorkRequest.Builder(ChatsUpdateWorker::class.java, 5, TimeUnit.SECONDS)
                    .setConstraints(constraints)
                    .build()
        }
    }

}