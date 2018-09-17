package com.stocardapp.hackschoolchat.ui.chats

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.stocardapp.hackschoolchat.R
import com.stocardapp.hackschoolchat.database.ChatMessage


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class MessageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {


    @ModelProp
    fun setValue(chatMessage: ChatMessage) {
        this.text = "${chatMessage.name}: ${chatMessage.message}"

        if (chatMessage.timestamp == Long.MAX_VALUE) {
            this.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        } else {
            val color: Int = chatMessage.name.hashCode().or(0xFF000000.toInt())
            this.setTextColor(color)
        }
    }


}