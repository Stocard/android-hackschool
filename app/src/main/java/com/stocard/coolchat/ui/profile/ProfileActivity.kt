package com.stocard.coolchat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.profile.*

class ProfileActivity : AppCompatActivity() {

    private val chatRepository = ServiceLocator.get(this).chatRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)
        setTitle(R.string.profile_activity_title)

        save_name_button.setOnClickListener {
            val name = name_input.text.toString()
            chatRepository.setName(name)
            finish()
        }
    }
}
