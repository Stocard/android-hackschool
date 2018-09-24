package com.stocard.coolchat.ui.chat

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.stocard.coolchat.data.Message

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class MessageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {


    @ModelProp
    fun setValue(chatMessage: Message) {
        this.text = "${chatMessage.name}: ${chatMessage.message}"
        id = chatMessage.hashCode()

        val color: Int = chatMessage.name.hashCode().or(0xFF000000.toInt())
        this.setTextColor(color)
    }

}