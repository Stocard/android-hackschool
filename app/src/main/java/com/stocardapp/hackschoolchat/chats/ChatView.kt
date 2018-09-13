package com.stocardapp.hackschoolchat.chats

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.stocardapp.hackschoolchat.database.ChatMessage


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ChatView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {


    @ModelProp
    fun setValue(chatMessage: ChatMessage) {
        this.text = "${chatMessage.name}: ${chatMessage.message}"
    }


}