package com.example.appchat.data.model

import com.example.appchat.data.db.entity.Chat
import com.example.appchat.data.db.entity.UserInfo

data class ChatWithUserInfo(var mChat: Chat, var myInfo: UserInfo)