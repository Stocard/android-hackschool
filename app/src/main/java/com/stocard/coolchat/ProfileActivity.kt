package com.stocard.coolchat

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)
        setTitle(R.string.profile_activity_title)

        save_name_button.setOnClickListener {
            val name = name_input.text.toString()
            setNameAndFinish(name)
        }
    }

    private fun setNameAndFinish(name: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit().putString("name", name).apply()
        finish()
    }
}
