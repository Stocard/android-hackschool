package com.stocardapp.hackschoolchat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.stocardapp.hackschoolchat.backend.Backend
import com.stocardapp.hackschoolchat.database.ChatMessage
import com.stocardapp.hackschoolchat.utils.onDone
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import retrofit2.Response
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private val simpleEpoxyAdapter: SimpleEpoxyAdapter by lazy {
        val adapter = SimpleEpoxyAdapter()
        adapter.enableDiffing()
        adapter
    }
    private val viewModel by lazy { ViewModelProviders.of(this).get(ChatViewModel::class.java) }

    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerview_chat.layoutManager = LinearLayoutManager(this)
        recyclerview_chat.adapter = simpleEpoxyAdapter

        viewModel.chats().observe(this, Observer<List<EpoxyModel<*>>>(this::updateUi))

        chat_message_input.onDone { message ->
            if (message.isEmpty()) {

            } else {
                launch {
                    viewModel.send(message)
                }
                chat_message_input.text = null
            }
        }
        job = updateData()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    private fun updateUi(models: List<EpoxyModel<*>>) {
        simpleEpoxyAdapter.removeAllModels()
        simpleEpoxyAdapter.addModels(models)
        recyclerview_chat.scrollToPosition(models.size - 1)
    }

    // TODO: move this into Job and trigger automatically
    private fun updateData() = launch {
        while (true) {
            delay(3, TimeUnit.SECONDS)
            try {
                val response: Response<List<ChatMessage>>? = Backend.instance.getMessages().execute()
                val body: List<ChatMessage>? = response?.body()
                if (body != null) {
                    viewModel.update(body)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                delay(30, TimeUnit.SECONDS)
            }
        }
    }

}
