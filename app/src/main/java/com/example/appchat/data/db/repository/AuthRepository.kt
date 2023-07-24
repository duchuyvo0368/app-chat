package com.example.appchat.data.db.repository

import com.example.appchat.data.Result
import com.example.appchat.data.db.remote.FirebaseAuthSource
import com.example.appchat.data.model.CreateUser
import com.example.appchat.data.model.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository {
    private val firebaseAuthSource = FirebaseAuthSource()


    fun loginUser(login: Login, b: (Result<FirebaseUser>) -> Unit) {
        b.invoke(Result.Loading)
        firebaseAuthSource.loginWithEmailAndPassword(login)
            .addOnSuccessListener {//khi requestLogin() được thực hiện xong thì gọi đến addOnComplete..
                //invoke() truyen kq den Result
                b.invoke(Result.Success(it.user))//dang nha thanh cong
            }.addOnFailureListener {
            b.invoke(Result.Error(msg = it.message))//dang nhap that bai
        }
    }

    fun createUser(createUser: CreateUser, b: (Result<FirebaseUser>) -> Unit) {
        b.invoke(Result.Loading)
        firebaseAuthSource.createUser(createUser).addOnSuccessListener {
            b.invoke(Result.Success(it.user))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.message))
        }
    }
}
