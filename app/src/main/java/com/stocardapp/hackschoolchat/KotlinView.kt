package com.stocardapp.hackschoolchat

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class KotlinView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    @ModelProp
    fun setValue(value: String) {
        this.text = value
    }

}