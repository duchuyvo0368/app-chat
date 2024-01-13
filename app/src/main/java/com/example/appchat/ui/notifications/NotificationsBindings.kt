package com.example.appchat.ui.notifications

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.data.db.entity.UserInfo

@BindingAdapter("bind_notifications_list")
fun bindNotificationsList(listView: RecyclerView, items: List<UserInfo>?) {
    items?.let { (listView.adapter as NotificationsListAdapter).submitList(items) }
}
