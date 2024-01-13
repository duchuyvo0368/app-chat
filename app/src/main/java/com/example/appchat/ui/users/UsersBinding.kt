package com.example.appchat.ui.users

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.data.db.entity.User

@BindingAdapter("bind_users_list")
fun bindUsersList(listView:RecyclerView,items:List<User>?){
    items?.let { (listView.adapter as UserListAdapter).submitList(items) }
}