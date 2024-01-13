package com.example.appchat.ui.profile

import androidx.lifecycle.*
import com.example.appchat.data.db.entity.User
import com.example.appchat.data.db.remote.FirebaseReferenceValueObserver
import com.example.appchat.data.db.repository.DatabaseRepository
import com.example.appchat.ui.DefaultViewModel
import com.example.appchat.data.Result
import com.example.appchat.data.db.entity.Chat
import com.example.appchat.data.db.entity.Message
import com.example.appchat.data.db.entity.UserFriend
import com.example.appchat.data.db.entity.UserNotification
import com.example.appchat.data.db.entity.UserRequest
import com.example.appchat.utils.convertTowUserIDs


class ProfileViewModelFactory(private val myUserID: String, private val otherUserID: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(myUserID, otherUserID) as T
    }
}

enum class LayoutState {
    IS_FRIEND, NOT_FRIEND, ACCEPT_DECLINE, REQUEST_SENT
}

class ProfileViewModel(private val myUserID: String, private val userID: String) :
    DefaultViewModel() {

    private val repository: DatabaseRepository = DatabaseRepository()
    private val firebaseReferenceObserver = FirebaseReferenceValueObserver()
    private val _myUser: MutableLiveData<User> = MutableLiveData()
    private val _otherUser: MutableLiveData<User> = MutableLiveData()

    val otherUser: LiveData<User> = _otherUser
    val layoutState = MediatorLiveData<LayoutState>()

    init {
        layoutState.addSource(_myUser) { updateLayoutState(it, _otherUser.value) }
        setupProfile()
    }

    override fun onCleared() {
        super.onCleared()
        firebaseReferenceObserver.clear()
    }

    private fun updateLayoutState(myUser: User?, otherUser: User?) {
        if (myUser != null && otherUser != null) {
            layoutState.value = when {
                myUser.friends[otherUser.info.id] != null -> LayoutState.IS_FRIEND
                myUser.notifications[otherUser.info.id] != null -> LayoutState.ACCEPT_DECLINE
                myUser.sentRequest[otherUser.info.id] != null -> LayoutState.REQUEST_SENT
                else -> LayoutState.NOT_FRIEND
            }
        }
    }

    private fun setupProfile() {
        repository.loadUser(userID) { result: Result<User> ->
            onResult(_otherUser, result)
            if (result is Result.Success) {
                repository.loadAndObserveUser(myUserID, firebaseReferenceObserver) { result2: Result<User> ->
                    onResult(_myUser, result2)
                }
            }
        }
    }

    fun addFriendPressed() {
        repository.updateNewSentRequest(myUserID, UserRequest(_otherUser.value!!.info.id))
        repository.updateNewNotification(_otherUser.value!!.info.id, UserNotification(myUserID))
    }

    fun removeFriendPressed() {
        repository.removeFriend(myUserID, _otherUser.value!!.info.id)
        repository.removeChat(convertTowUserIDs(myUserID, _otherUser.value!!.info.id))
        repository.removeMessages(convertTowUserIDs(myUserID, _otherUser.value!!.info.id))
    }

    fun acceptFriendRequestPressed() {
        repository.updateNewFriend(UserFriend(myUserID), UserFriend(_otherUser.value!!.info.id))

        val newChat = Chat().apply {
            info.id = convertTowUserIDs(myUserID, _otherUser.value!!.info.id)
            lastMessage = Message(seen = true, text = "Say hello!")
        }

        repository.updateNewChat(newChat)
        repository.removeNotification(myUserID, _otherUser.value!!.info.id)
        repository.removeSentRequest(_otherUser.value!!.info.id, myUserID)
    }

    fun declineFriendRequestPressed() {
        repository.removeSentRequest(myUserID, _otherUser.value!!.info.id)
        repository.removeNotification(myUserID, _otherUser.value!!.info.id)
    }
}
