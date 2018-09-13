package com.stocardapp.hackschoolchat.utils

import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.onDone(doneFun: (String) -> Any) {
    setOnEditorActionListener { _, actionId, _ ->
        when (actionId) {
            EditorInfo.IME_ACTION_DONE -> {
                doneFun(text.toString())
                true
            }
            else -> false
        }
    }
}