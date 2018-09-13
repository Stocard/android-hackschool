package com.stocardapp.hackschoolchat

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.SimpleEpoxyAdapter
import com.stocardapp.hackschoolchat.database.AppDatabase
import com.stocardapp.hackschoolchat.database.ChatMessage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


class MainActivity : AppCompatActivity() {

    private val simpleEpoxyAdapter = SimpleEpoxyAdapter()
    private val messages = mutableListOf<EpoxyModel<*>>()

    private val database: AppDatabase by lazy { Room.databaseBuilder(applicationContext, AppDatabase::class.java, "chat_db").build() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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


    }

    override fun onResume() {
        super.onResume()

        recyclerview_chat.adapter = simpleEpoxyAdapter
        recyclerview_chat.layoutManager = LinearLayoutManager(this)
    }

    private fun send(message: String) {
        launch {
            database.chatDao().insertAll(ChatMessage(name = "Tobias", message = message))


            val messages2 = database.chatDao().all.map {
                val model = KotlinViewModel_()
                model.value(it)
            }
            launch(UI) {
                simpleEpoxyAdapter.removeAllModels()
                simpleEpoxyAdapter.addModels(messages2)
            }
        }
    }
}
