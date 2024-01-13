package com.example.appchat.ui.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.data.db.entity.User
import com.example.appchat.databinding.ListItemChatsBinding
import com.example.appchat.databinding.ListItemUserBinding


class UserDiffCallback : DiffUtil.ItemCallback<User>() {


    //kiem tra 2 doi tuong co giong nhau khong
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    //kiêm tra 2 noi dung co giong nhau khong
    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.info.id == newItem.info.id
    }

}

class UserListAdapter internal constructor(private val viewModel: UsersViewModel) :
    ListAdapter<User, UserListAdapter.ViewHolder>(UserDiffCallback()) {
    class ViewHolder(private val binding: ListItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(viewModel: UsersViewModel,item:User){
                binding.viewmodel=viewModel
                binding.user=item
                binding.executePendingBindings()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemUserBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(viewModel,getItem(position))
    }
}
