package com.example.appchat.ui.chats

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.data.model.ChatWithUserInfo

@BindingAdapter("bind_chats_list")
fun bindChatsList(listView: RecyclerView,item:List<ChatWithUserInfo>?){
    item?.let { (listView.adapter as  ChatsListAdapter).submitList(item) }
}