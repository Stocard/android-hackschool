package com.stocardapp.hackschoolchat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.stocardapp.hackschoolchat.backend.service
import com.stocardapp.hackschoolchat.utils.onDone
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.launch
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private val simpleEpoxyAdapter = SimpleEpoxyAdapter().apply { enableDiffing() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerview_chat.adapter = simpleEpoxyAdapter
        recyclerview_chat.layoutManager = LinearLayoutManager(this)

        val viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
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
    }

    override fun onResume() {
        super.onResume()

        launch {
            try {
                val response: Response<String> = service.listRepos()
                response.toString()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    fun updateUi(models: List<EpoxyModel<*>>) {
        simpleEpoxyAdapter.removeAllModels()
        simpleEpoxyAdapter.addModels(models)
        recyclerview_chat.scrollToPosition(models.size - 1)
    }

}
