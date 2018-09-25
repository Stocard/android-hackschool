package com.stocard.coolchat.ui.chat

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.View
import com.stocard.coolchat.ProfileActivity
import com.stocard.coolchat.R
import com.stocard.coolchat.data.NetworkState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(ChatViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        send_button.setOnClickListener {
            val input = message_input.text.toString()
            message_input.text = null
            viewModel.send(input).observe(this, Observer { success ->
                if (success != null && !success) displayError(R.string.posting_message_failed_message)
            })
        }

        viewModel.viewState.observe(this, Observer { viewState ->
            viewState?.let { updateUi(it) }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val profileLink = menu?.add(R.string.profile_activity_title)
        profileLink?.intent = Intent(this, ProfileActivity::class.java)
        return true
    }

    private fun updateUi(state: ChatViewState) {
        Log.d(LOG_TAG, "viewState changed to $state")

        chat_recycler_view.setModels(state.models)
        chat_recycler_view.scrollToPosition(chat_recycler_view.adapter.itemCount - 1)

        when (state.networkState) {
            NetworkState.DONE -> {
                error_output.visibility = View.GONE
                progress_bar.visibility = View.GONE
            }
            NetworkState.REFRESHING -> {
                error_output.visibility = View.GONE
                progress_bar.visibility = View.VISIBLE
            }
            NetworkState.ERROR -> {
                error_output.visibility = View.VISIBLE
                progress_bar.visibility = View.GONE
            }
        }
    }


    private fun displayError(@StringRes message: Int) {
        AlertDialog.Builder(this).apply {
            setMessage(message)
        }.show()
    }

    companion object {
        private const val LOG_TAG = "MainActivity"
    }
}
