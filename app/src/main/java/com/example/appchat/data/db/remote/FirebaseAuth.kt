package com.example.appchat.data.db.remote

import com.example.appchat.data.model.Login
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuth {
    companion object {
        //lấy một instance duy nhất của lớp FirebaseAuth,
        val authInstance = FirebaseAuth.getInstance()
    }

    fun requestLogin(login: Login): Task<AuthResult> {
        return authInstance.signInWithEmailAndPassword(login.email,login.password)
    }


}