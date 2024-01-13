package com.example.appchat.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appchat.AppChat
import com.example.appchat.data.db.entity.UserNotification

import com.example.appchat.data.db.remote.FirebaseAuthStateObserver
import com.example.appchat.data.db.remote.FirebaseReferenceConnectedObserver
import com.example.appchat.data.db.remote.FirebaseReferenceValueObserver
import com.example.appchat.data.db.repository.AuthRepository
import com.example.appchat.data.db.repository.DatabaseRepository
import com.google.firebase.auth.FirebaseUser
import com.example.appchat.data.Result

class MainViewModel : ViewModel() {

    private val dbRepository = DatabaseRepository()
    private val authRepository = AuthRepository()

    private val _userNotificationsList = MutableLiveData<MutableList<UserNotification>>()

    private val fbRefNotificationsObserver = FirebaseReferenceValueObserver()
    private val fbAuthStateObserver = FirebaseAuthStateObserver()
    private val fbRefConnectedObserver = FirebaseReferenceConnectedObserver()
    private var userID = AppChat.myUserID

    var userNotificationsList: LiveData<MutableList<UserNotification>> = _userNotificationsList

    init {
       setupAuthObserver()
    }

    override fun onCleared() {
        super.onCleared()
        fbRefNotificationsObserver.clear()
        fbRefConnectedObserver.clear()
        fbAuthStateObserver.clear()
    }

    private fun setupAuthObserver(){
        authRepository.observeAuthState(fbAuthStateObserver) { result: Result<FirebaseUser> ->
            if (result is Result.Success) {
                userID = result.data!!.uid
                startObservingNotifications()
                fbRefConnectedObserver.start(userID)
            } else {
                fbRefConnectedObserver.clear()
                stopObservingNotifications()
            }
        }
    }

    private fun startObservingNotifications() {
        dbRepository.loadAndObserveUserNotifications(userID, fbRefNotificationsObserver) { result: Result<MutableList<UserNotification>> ->
            if (result is Result.Success) {
                _userNotificationsList.value = result.data!!
            }
        }
    }

    private fun stopObservingNotifications() {
        fbRefNotificationsObserver.clear()
    }
}
