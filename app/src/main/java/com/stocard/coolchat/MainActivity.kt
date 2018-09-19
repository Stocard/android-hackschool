package com.stocard.coolchat

import android.app.AlertDialog
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import com.stocard.coolchat.backend.Backend
import com.stocard.coolchat.backend.BackendService
import com.stocard.coolchat.backend.enqueue
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val backend: BackendService by lazy { Backend.instance }
    private val adapter: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chat_list.adapter = adapter

        send_button.setOnClickListener {
            val input = message_input.text.toString()
            send(input)
            message_input.text = null
        }
    }

    override fun onResume() {
        super.onResume()
        fetchMessages()
    }

    private fun send(message: String) {
        progress_bar.visibility = View.VISIBLE
        backend.postMessage(Message(message, "name")).enqueue(
                { error ->
                    Log.e(LOG_TAG, Log.getStackTraceString(error))
                    progress_bar.visibility = View.GONE
                    displayError(R.string.posting_message_failed_message)
                },
                { _ ->
                    progress_bar.visibility = View.GONE
                    fetchMessages()
                }
        )
    }

    private fun fetchMessages() {
        progress_bar.visibility = View.VISIBLE
        backend.fetchMessages().enqueue(
                { error ->
                    Log.e(LOG_TAG, Log.getStackTraceString(error))
                    progress_bar.visibility = View.GONE
                    displayError(R.string.fetching_messages_failed_message)
                },
                { result ->
                    progress_bar.visibility = View.GONE
                    result.body()?.let { showMessages(it) }
                }
        )
    }

    private fun displayError(@StringRes message: Int) {
        AlertDialog.Builder(this).apply {
            setMessage(message)
        }.show()
    }

    private fun showMessages(messages: List<Message>) {
        val messageItems = messages.map { it.name + ": " + it.message }
        adapter.clear()
        adapter.addAll(messageItems)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(R.string.refresh).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        fetchMessages()
        return true
    }

    companion object {
        private const val LOG_TAG = "MainActivity"
    }

}
