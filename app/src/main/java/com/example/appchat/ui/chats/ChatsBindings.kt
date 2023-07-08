@file:Suppress("unused")

package com.example.appchat.ui.chats

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.R
import com.example.appchat.data.db.entity.Message
import com.example.appchat.data.model.ChatWithUserInfo



@BindingAdapter("bind_chats_list")
fun bindChatsList(listView: RecyclerView, items: List<ChatWithUserInfo>?) {
    items?.let { (listView.adapter as ChatsListAdapter).submitList(items) }
}

@BindingAdapter("bind_chat_message_text", "bind_chat_message_text_viewModel")
fun TextView.bindMessageYouToText(message: Message, viewModel: ChatsViewModel) {
    this.text = if (message.senderId == viewModel.myUserId) {
        "You: " + message.text
    } else {
        message.text
    }
}

@BindingAdapter("bind_message_view", "bind_message_textView", "bind_message", "bind_myUserID")
fun View.bindMessageSeen(view: View, textView: TextView, message: Message, myUserID: String) {
//    if (message.senderID != myUserID && !message.seen) {
//        view.visibility = View.VISIBLE
//        textView.setTextAppearance(R.style.MessageNotSeen)
////        textView.alpha = 1f
//    } else {
//        view.visibility = View.INVISIBLE
//        textView.setTextAppearance(R.style.MessageSeen)
////        textView.alpha = 1f
//    }
}

