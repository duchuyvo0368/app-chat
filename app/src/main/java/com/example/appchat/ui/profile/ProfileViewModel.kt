package com.example.appchat.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appchat.data.db.entity.User
import com.example.appchat.data.db.remote.FirebaseReferenceValueObserver
import com.example.appchat.data.db.repository.DatabaseRepository
import com.example.appchat.ui.DefaultViewModel
import com.example.appchat.data.Result
import com.example.appchat.data.db.entity.UserNotification
import com.example.appchat.data.db.entity.UserRequest

class ProfileViewModelFactory(private val myUserID: String, private val otherUserID: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(myUserID, otherUserID) as T
    }
}

enum class LayoutState {
    IS_FRIEND, NOT_FRIEND, ACCEPT_DECLINE, REQUEST_SENT
}

class ProfileViewModel(private val myUserID: String, private val otherUserID: String) :
    DefaultViewModel() {
    private val repository: DatabaseRepository = DatabaseRepository()
    private val firebaseReferenceValueObserver = FirebaseReferenceValueObserver()
    private val _myUser: MutableLiveData<User> = MutableLiveData()
    private val _otherUser: MutableLiveData<User> = MutableLiveData()

    val otherUser: LiveData<User> = _otherUser
    val layoutState = MediatorLiveData<LayoutState>()

    init {
        layoutState.addSource(_myUser) {
            updateLayoutState(it, _otherUser.value)
        }
        setupProfile()
    }

    private fun setupProfile() {
        repository.loadUser(myUserID){result:Result<User>->
            onResult(_otherUser,result)
            if (result is Result.Success){
                repository.loadAndObserveUser(myUserID,firebaseReferenceValueObserver){result2:Result<User>->
                    onResult(_myUser,result)
                }
            }
        }
    }

    private fun updateLayoutState(myUser: User?, otherUser: User?) {
        if (_myUser != null && otherUser != null) {
            if (myUser != null) {
                layoutState.value = when {
                    //kiểm tra có phần tử trong friends bằng otherUser.id
                    myUser.friends[otherUser.info.id] != null -> LayoutState.IS_FRIEND
                    myUser.notification[otherUser.info.id]!=null->LayoutState.ACCEPT_DECLINE
                    myUser.sentRequest[otherUser.info.id]!=null->LayoutState.REQUEST_SENT
                    else -> {LayoutState.NOT_FRIEND}
                }
            }
        }
    }
    fun addFriendPressed(){
        repository.updateNewSentRequest(myUserID,UserRequest(_otherUser.value!!.info.id))
        repository.updateNotification(_otherUser.value!!.info.id,UserNotification(myUserID))

    }

}