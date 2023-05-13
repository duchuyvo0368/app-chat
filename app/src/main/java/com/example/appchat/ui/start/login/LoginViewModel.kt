package com.example.appchat.ui.start.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appchat.data.Event
import com.example.appchat.data.Resource
import com.example.appchat.data.db.repository.AuthRepository
import com.example.appchat.data.model.Login
import com.example.appchat.isValidEmail
import com.example.appchat.isValidPassword
import com.example.appchat.ui.DefaultViewModel
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : DefaultViewModel() {
    private val authRepository = AuthRepository()
    private val _isLoggedInEvent = MutableLiveData<Event<FirebaseUser>>()
    val isLoggedInEvent: LiveData<Event<FirebaseUser>> = _isLoggedInEvent
    val emailText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()
    val isLoginIn = MutableLiveData<Boolean>()


    private fun login() {
        isLoginIn.value = true//đại diện cho quá trình đăng nhập
        val login = Login(emailText.value!!, passwordText.value!!)
        authRepository.loginUser(login) {
            if (it is Resource.Success) _isLoggedInEvent.value = Event(it.data!!) //trạng thái đăng nhập thành công
            if (it is Resource.Success || it is Resource.Error) isLoginIn.value = false//trang thái false

        }
    }

    fun loginPressed() {
        if (!isValidEmail(emailText.value!!)) {
            mSnackBarText.value=Event("Invalid email format")
            return
        }
        if (!isValidPassword(6,passwordText.value!!)){
            mSnackBarText.value=Event("Password is too short")
            return
        }
        login()
    }
}