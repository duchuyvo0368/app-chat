package com.example.appchat.ui.start.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appchat.data.Event
import com.example.appchat.data.db.repository.AuthRepository
import com.example.appchat.ui.DefaultViewModel
import com.example.appchat.data.Result
import com.example.appchat.data.model.Login
import com.example.appchat.utils.isEmailValid
import com.example.appchat.utils.isTextValid
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : DefaultViewModel() {

    private val authRepository = AuthRepository()
    private val _isLoggedInEvent = MutableLiveData<Event<FirebaseUser>>()

    val isLoggedInEvent: LiveData<Event<FirebaseUser>> = _isLoggedInEvent
    val emailText = MutableLiveData<String>() // Two way
    val passwordText = MutableLiveData<String>() // Two way
    val isLoggingIn = MutableLiveData<Boolean>() // Two way

    private fun login() {
        isLoggingIn.value = true
        val login = Login(emailText.value!!, passwordText.value!!)

        authRepository.loginUser(login) { result: Result<FirebaseUser> ->
            onResult(null, result)
            if (result is Result.Success) _isLoggedInEvent.value = Event(result.data!!)
            if (result is Result.Success || result is Result.Error) isLoggingIn.value = false
        }
    }

    fun loginPressed() {
        if (!isEmailValid(emailText.value.toString())) {
            mSnackBarText.value = Event("Invalid email format")
            return
        }
        if (!isTextValid(6, passwordText.value)) {
            mSnackBarText.value = Event("Password is too short")
            return
        }

        login()
    }
}