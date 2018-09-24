package com.stocard.coolchat.ui.chat

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import java.text.SimpleDateFormat
import java.util.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ChatHeaderView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    init {
        setTextColor(Color.LTGRAY)
        setBackgroundColor(Color.parseColor("#f5f5f5"))
        gravity = Gravity.CENTER_HORIZONTAL
    }

    @ModelProp
    fun setValue(date: Date) {
        this.text = dateFormat.format(date)
    }

    companion object {
        private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    }

}