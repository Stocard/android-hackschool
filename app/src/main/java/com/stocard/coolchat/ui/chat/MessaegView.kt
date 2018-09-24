package com.stocard.coolchat.ui.chat

import android.support.v7.widget.AppCompatTextView
import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.stocard.coolchat.R

@EpoxyModelClass(layout = R.layout.model_chat_message)
abstract class ChatMessageView : EpoxyModelWithHolder<Holder>() {

    @EpoxyAttribute
    lateinit var name: String

    @EpoxyAttribute
    lateinit var message: String

    override fun bind(holder: Holder) {
        holder.nameView.text = name
        holder.messageView.text = message
    }

}

class Holder : EpoxyHolder() {

    private lateinit var layout: View

    val nameView: AppCompatTextView by lazy { layout.findViewById<AppCompatTextView>(R.id.name) }
    val messageView: AppCompatTextView by lazy { layout.findViewById<AppCompatTextView>(R.id.message) }

    override fun bindView(itemView: View) {
        this.layout = itemView
    }

}