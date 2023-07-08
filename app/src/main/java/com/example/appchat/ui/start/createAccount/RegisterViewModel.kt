package com.example.appchat.ui.start.createAccount


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appchat.data.Event
import com.example.appchat.data.Result
import com.example.appchat.data.db.entity.User
import com.example.appchat.data.db.repository.AuthRepository
import com.example.appchat.data.db.repository.DatabaseRepository
import com.example.appchat.data.model.CreateUser
import com.example.appchat.isValidEmail
import com.example.appchat.isValidText
import com.example.appchat.ui.DefaultViewModel
import com.google.firebase.auth.FirebaseUser

class RegisterViewModel : DefaultViewModel() {
    private val _loginAccount = MutableLiveData<Event<Unit>>()
    val loginAccount: LiveData<Event<Unit>> = _loginAccount
    private val dbRepository = DatabaseRepository()
    private val authRepository = AuthRepository()

    private val mICreateEvent = MutableLiveData<Event<FirebaseUser>>()


//    private var _passwordStrength = MutableLiveData<PasswordStrength>()
//    val passwordStrength: LiveData<PasswordStrength> = _passwordStrength


    val isCreateEvent: LiveData<Event<FirebaseUser>> = mICreateEvent
    val displayName = MutableLiveData<String>()
    val emailText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()
    val isCreatingAccount = MutableLiveData<Boolean>()

    val passwordStrength: LiveData<String> = emailText

    private fun createAccount() {
        isCreatingAccount.value = true
        val createUser = CreateUser(displayName.value!!, emailText.value!!, passwordText.value!!)
        authRepository.createUser(createUser) { result: Result<FirebaseUser> ->
            onResult(null, result)
            if (result is Result.Success) {
                mICreateEvent.value = Event(result.data!!)
                dbRepository.updateNewUser(User().apply {
                    info.id = result.data.uid
                    info.displayName = createUser.displayName
                })
            }
            if (result is Result.Success || result is Result.Error) isCreatingAccount.value = false
        }
    }


    fun createAccountPressed() {


        if (!isValidText(2, displayName.value)) {
            mSnackBarText.value = Event("Display name is too short")
            return
        }

        if (!isValidEmail(emailText.value.toString())) {
            mSnackBarText.value = Event("Invalid email format")
            return
        }
        if (!isValidText(6, passwordText.value)) {
            mSnackBarText.value = Event("Password is too short")
            return

        }

        createAccount()
    }


    fun goToLoginAccountPressed() {
        _loginAccount.value = Event(Unit)
    }


}