package com.example.appchat.data.db.repository

import com.example.appchat.data.db.entity.User
import com.example.appchat.data.db.remote.FirebaseDataSource
import com.example.appchat.data.Result
import com.example.appchat.data.db.entity.Chat
import com.example.appchat.data.db.entity.UserFriend
import com.example.appchat.data.db.entity.UserInfo
import com.example.appchat.data.db.remote.FirebaseReferenceValueObserver
import com.example.appchat.ui.wrapSnapshotToArrayList
import com.example.appchat.ui.wrapSnapshotToClass
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot


class DatabaseRepository {
    private val firebaseDatabaseService: FirebaseDataSource = FirebaseDataSource()
    fun updateNewUser(user: User) {
        firebaseDatabaseService.updateNewUser(user)
    }
//lay du lieu tu remote(FirebaseDataSource) cho ChatsViewModel
    fun loadFriends(userId: String, b: ((Result<List<UserFriend>>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseDatabaseService.loadFriendsTask(userId).addOnSuccessListener(object :OnSuccessListener<DataSnapshot>{
            override fun onSuccess(p0: DataSnapshot?) {
                val friendsList = wrapSnapshotToArrayList(UserFriend::class.java, p0!!)
                b.invoke(Result.Success(friendsList))
            }

        }
        //code bang lambada
    ).addOnFailureListener {
            b.invoke(Result.Error(it.message))

        }
    }

    fun loadUserInfo(userID: String, b: ((Result<UserInfo>) -> Unit)) {
        firebaseDatabaseService.loadFriendsTask(userID).addOnSuccessListener {
            b.invoke(Result.Success(wrapSnapshotToClass(UserInfo::class.java, it)))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.message))
        }
    }

    fun loadAndObserveChat(
        chatID: String,
        observer: FirebaseReferenceValueObserver,
        b: ((Result<Chat>) -> Unit)
    ) {
        firebaseDatabaseService.attachChatObserver(Chat::class.java, chatID, observer, b)
    }
}