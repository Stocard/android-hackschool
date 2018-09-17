package com.stocardapp.hackschoolchat.work

import androidx.room.Room
import androidx.work.*
import com.stocardapp.hackschoolchat.backend.Backend
import com.stocardapp.hackschoolchat.database.AppDatabase
import com.stocardapp.hackschoolchat.database.ChatMessage
import retrofit2.Response
import timber.log.Timber
import java.util.concurrent.TimeUnit

class UpdateChatsWork : Worker() {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "chat_db")
                .fallbackToDestructiveMigration()
                .build()
    }

    override fun doWork(): Result {
        Timber.d("Doing work")
        return try {
            val response: Response<List<ChatMessage>>? = Backend.instance.getMessages().execute()
            val messages: List<ChatMessage> = response?.body()!! // TODO add null handling

            // TODO: only update new entries and don't drop the whole table ;-)
            database.chatDao().nukeTable()
            database.chatDao().insertAll(messages)
            Result.SUCCESS
        } catch (e: Throwable) {
            e.printStackTrace()
            Result.FAILURE
        }
    }

    companion object {
        fun run(delay: Long? = null) {
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
            val work: PeriodicWorkRequest = PeriodicWorkRequest.Builder(UpdateChatsWork::class.java, 3, TimeUnit.SECONDS)
                    .setConstraints(constraints)
                    .build()
        }
    }

}