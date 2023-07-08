package com.example.appchat.data.db.remote

import com.example.appchat.data.model.Login
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.appchat.data.Result
import com.example.appchat.data.model.CreateUser

//dung lay
class FirebaseAuthStateObserver {
    //FirebaseAuth la mot dich vu nguoi dung(dang nhap,dang xuat,dat lai mk,xac thuc sdt,quan ly thong ti ng dung)
    private var authListener: FirebaseAuth.AuthStateListener? = null
    private var instance: FirebaseAuth? = null


    //lang nghe trang thai cua nguoi dung
    //tao ham satrt() lang nghe, chi can goi satrt va truyen tham so
    fun start(valueEventListener: FirebaseAuth.AuthStateListener, instance: FirebaseAuth) {
        this.authListener = valueEventListener
        this.instance = instance
        this.instance!!.addAuthStateListener(authListener!!)
    }

    fun clear() {
        authListener!!.let {
            instance?.removeAuthStateListener(it)//kiem tra null truoc khi truy cap removeAuthStateListener
        }
    }
}

class FirebaseAuthSource {
    companion object {
        //lấy một instance duy nhất của lớp FirebaseAuth,
        val authInstance = FirebaseAuth.getInstance()
    }

    fun loginWithEmailAndPassword(login: Login): Task<AuthResult> {
        return authInstance.signInWithEmailAndPassword(login.email, login.password)
    }
    fun createUser(createUser: CreateUser):Task<AuthResult>{
        return authInstance.createUserWithEmailAndPassword(createUser.email,createUser.password)
    }
    //lang nghe su thay doi trang thai xac thuc(dang nha,dang xuat,xoa tai khoan...) cua nguoi dung
    private fun attachAuthObserver(b: (Result<FirebaseUser>) -> Unit): FirebaseAuth.AuthStateListener {
        val authStateListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(it: FirebaseAuth) {
                if (it.currentUser == null) {
                    b.invoke(Result.Error("No user"))//invoke() truyen kq den Result
                } else {
                    b.invoke(Result.Success(it.currentUser))//currentUser chu thong tin nguoi dung(name,email...)
                }
            }


        }
        return authStateListener
    }

    fun logout() {
        authInstance.signOut()
    }
}