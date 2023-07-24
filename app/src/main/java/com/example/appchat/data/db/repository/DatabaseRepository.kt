package com.example.appchat.data.db.repository

import com.example.appchat.data.db.remote.FirebaseDataSource
import com.example.appchat.data.Result
import com.example.appchat.data.db.entity.*
import com.example.appchat.data.db.remote.FirebaseReferenceValueObserver
import com.example.appchat.ui.wrapSnapshotToArrayList
import com.example.appchat.ui.wrapSnapshotToClass


class DatabaseRepository {
    private val firebaseDatabaseService: FirebaseDataSource = FirebaseDataSource()
    fun updateNewUser(user: User) {
        firebaseDatabaseService.updateNewUser(user)
    }

    //lay du lieu tu remote(FirebaseDataSource) cho ChatsViewModel
    fun loadFriends(userID: String, b: ((Result<List<UserFriend>>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseDatabaseService.loadFriendsTask(userID)
            .addOnSuccessListener {
                val friendsList = wrapSnapshotToArrayList(UserFriend::class.java, it!!)
                b.invoke(Result.Success(friendsList))
            }.addOnFailureListener {
                b.invoke(Result.Error(it.message))

            }
    }

    fun loadUserInfo(userID: String, b: ((Result<UserInfo>) -> Unit)) {
        firebaseDatabaseService.loadUserInfoTask(userID).addOnSuccessListener {
            b.invoke(Result.Success(wrapSnapshotToClass(UserInfo::class.java, it)))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.message))
        }
    }

    fun loadUsers(b: ((Result<MutableList<User>>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseDatabaseService.loadUsersTask().addOnSuccessListener {
            val usersList = wrapSnapshotToArrayList(User::class.java, it)
            b.invoke(Result.Success(usersList))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.message))
        }
    }
    fun loadUser(userID:String, b: (Result<User>) -> Unit){
        b.invoke(Result.Loading)
        firebaseDatabaseService.loadUserTask(userID).addOnSuccessListener {
            b.invoke(Result.Success(wrapSnapshotToClass(User::class.java,it)))
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

    fun loadAndObserveUser(userID:String,observer: FirebaseReferenceValueObserver,b:((Result<User>)->Unit)) {
        firebaseDatabaseService.attachChatObserver(User::class.java,userID,observer,b)
    }

    fun updateNewSentRequest(userID:String,userRequest:UserRequest){
        firebaseDatabaseService.updateNewSentRequest(userID,userRequest)
    }

    fun updateNotification(otherUserID: String, userNotification: UserNotification) {
        firebaseDatabaseService.updateNewNotification(otherUserID,userNotification)
    }
}