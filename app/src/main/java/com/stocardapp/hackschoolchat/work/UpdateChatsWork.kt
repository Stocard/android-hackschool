package com.stocardapp.hackschoolchat.work

import androidx.work.*
import com.stocardapp.hackschoolchat.database.Updater
import timber.log.Timber
import java.util.concurrent.TimeUnit

class UpdateChatsWork : Worker() {

    val updater = Updater(applicationContext)

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
            val work: OneTimeWorkRequest = OneTimeWorkRequest.Builder(UpdateChatsWork::class.java).apply {
                if (delay != null) setInitialDelay(delay, TimeUnit.SECONDS)
            }.build()
            WorkManager.getInstance().enqueue(work)
        }

        fun startPeriodic() {
            val constraints: androidx.work.Constraints = androidx.work.Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

            // FIXME Seems as if periodic can only tun once every 15 minutes...
            val work: PeriodicWorkRequest = PeriodicWorkRequest.Builder(UpdateChatsWork::class.java, 5, TimeUnit.SECONDS)
                    .setConstraints(constraints)
                    .build()
        }
    }

}