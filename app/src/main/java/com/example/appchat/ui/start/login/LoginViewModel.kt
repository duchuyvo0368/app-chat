package com.example.appchat.ui.start.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appchat.data.Event
import com.example.appchat.data.Result
import com.example.appchat.data.db.repository.AuthRepository
import com.example.appchat.data.model.Login
import com.example.appchat.isValidEmail
import com.example.appchat.isValidText
import com.example.appchat.ui.DefaultViewModel
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : DefaultViewModel() {
    private val _createAccount = MutableLiveData<Event<Unit>>()
    private val authRepository = AuthRepository()
    private val _isLoggedInEvent = MutableLiveData<Event<FirebaseUser>>()
    private val _rememberMe=MutableLiveData<Event<Boolean>>()

    val rememberMe:LiveData<Event<Boolean>> = _rememberMe
    val createAccount: LiveData<Event<Unit>> = _createAccount
    val isLoggedInEvent: LiveData<Event<FirebaseUser>> = _isLoggedInEvent
    val emailText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()
    val isLoginIn = MutableLiveData<Boolean>()


    private fun login() {
        isLoginIn.value = true//đại diện cho quá trình đăng nhập
        val login = Login(emailText.value!!, passwordText.value!!)
        authRepository.loginUser(login) {
            if (it is Result.Success)
                _isLoggedInEvent.value = Event(it.data!!) //trạng thái đăng nhập thành công
            if (it is Result.Success || it is Result.Error)
                isLoginIn.value = false//trang thái false

        }
    }

    fun loginPressed() {
        if (!isValidEmail(emailText.value.toString())) {
            mSnackBarText.value = Event("Invalid email format")
            return
        }
        if (!isValidText(6, passwordText.value)) {
            mSnackBarText.value = Event("Password is too short")
            return
        }
        login()
    }

    fun rememberMe(value:Boolean){
        _rememberMe.value=Event(value)
    }
    fun goToCreateAccountPressed() {
        _createAccount.value = Event(Unit)
    }
}