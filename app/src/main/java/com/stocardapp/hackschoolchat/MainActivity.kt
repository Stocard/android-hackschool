package com.stocardapp.hackschoolchat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.stocardapp.hackschoolchat.database.Updater
import com.stocardapp.hackschoolchat.utils.onDone
import com.stocardapp.hackschoolchat.work.ChatsUpdateWorker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.isActive
import kotlinx.coroutines.experimental.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private val updater by lazy { Updater(this) }
    private lateinit var updateJob: Job
    private val viewModel by lazy { ViewModelProviders.of(this).get(ChatViewModel::class.java) }
    private val simpleEpoxyAdapter: SimpleEpoxyAdapter by lazy {
        val adapter = SimpleEpoxyAdapter()
        adapter.enableDiffing()
        adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerview_chat.layoutManager = LinearLayoutManager(this)
        recyclerview_chat.adapter = simpleEpoxyAdapter

        viewModel.chats().observe(this, Observer<List<EpoxyModel<*>>>(this::updateUi))

        chat_message_input.onDone { message ->
            if (!message.isEmpty()) {
                launch {
                    viewModel.send(message)
                }
                chat_message_input.text = null
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // TODO: can we use a periodic job instead?
        updateJob = launch {
            var count = 0
            while (this.isActive) {
                Timber.i("update loop ${++count}")
                // updater.update()
                ChatsUpdateWorker.schedule()
                delay(3, TimeUnit.SECONDS)
            }
        }
    }

    override fun onStop() {
        Timber.w("onStop")
        super.onStop()
        updateJob.cancel()
    }

    private fun updateUi(models: List<EpoxyModel<*>>) {
        Timber.i("Updating UI")
        simpleEpoxyAdapter.removeAllModels()
        simpleEpoxyAdapter.addModels(models)
        recyclerview_chat.scrollToPosition(models.size - 1)
    }

}
