package com.stocardapp.hackschoolchat

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.stocardapp.hackschoolchat.database.AppDatabase
import com.stocardapp.hackschoolchat.database.ChatMessage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.launch


class MainActivity : AppCompatActivity() {

    private val simpleEpoxyAdapter = SimpleEpoxyAdapter()

    private val database: AppDatabase by lazy { Room.databaseBuilder(applicationContext, AppDatabase::class.java, "chat_db").build() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        simpleEpoxyAdapter.enableDiffing()
        recyclerview_chat.adapter = simpleEpoxyAdapter
        recyclerview_chat.layoutManager = LinearLayoutManager(this)

        chat_message_input.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    val message = chat_message_input.text
                    chat_message_input.text = null
                    send(message.toString())
                    true
                }
                else -> false
            }
        }

        database.chatDao().allLive.observe(this, Observer<MutableList<ChatMessage>> { list ->
            val models = list.map {
                val model = KotlinViewModel_()
                model.id(it.uid)
                model.value(it)
            }
            simpleEpoxyAdapter.removeAllModels()
            simpleEpoxyAdapter.addModels(models)
            recyclerview_chat.scrollToPosition(models.size - 1)
        })

    }

    private fun send(message: String) = launch {
        database.chatDao().insertAll(ChatMessage(name = "Tobias", message = message))
    }

}
