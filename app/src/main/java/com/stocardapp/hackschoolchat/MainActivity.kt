package com.stocardapp.hackschoolchat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.inputmethod.EditorInfo
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.SimpleEpoxyAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val simpleEpoxyAdapter = SimpleEpoxyAdapter()
    private val messages = mutableListOf<EpoxyModel<*>>()

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

    private fun send(message: String) {
        val model = KotlinViewModel_()
        model.value(message)
        messages.add(model)
        simpleEpoxyAdapter.removeAllModels()
        simpleEpoxyAdapter.addModels(messages)
    }

    override fun onResume() {
        super.onResume()

        recyclerview_chat.adapter = simpleEpoxyAdapter
        recyclerview_chat.layoutManager = LinearLayoutManager(this)
    }
}
