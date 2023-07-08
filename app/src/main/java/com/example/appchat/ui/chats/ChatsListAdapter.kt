package com.example.appchat.ui.chats


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.data.model.ChatWithUserInfo
import com.example.appchat.databinding.ListItemChatsBinding

class ChatDiffCallback :DiffUtil.ItemCallback<ChatWithUserInfo>(){
    override fun areItemsTheSame(oldItem: ChatWithUserInfo, newItem: ChatWithUserInfo): Boolean {
        return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: ChatWithUserInfo, newItem: ChatWithUserInfo): Boolean {
        return oldItem.mChat.info.id==newItem.mChat.info.id
    }

}

class ChatsListAdapter internal constructor(private val viewModel:ChatsViewModel):
    ListAdapter<(ChatWithUserInfo), ChatsListAdapter.ViewHolder>(ChatDiffCallback()){
    class ViewHolder(private val binding:ListItemChatsBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ChatsViewModel,item:ChatWithUserInfo){
            binding.viewmodel=viewModel
            binding.chatwithuserinfo=item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        val binding=ListItemChatsBinding.inflate(layoutInflater,parent,false)
        return  ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(viewModel,getItem(position))
    }
}