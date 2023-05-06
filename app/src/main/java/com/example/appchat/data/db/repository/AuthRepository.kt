package com.example.appchat.data.db.repository
import com.example.appchat.data.Resource
import com.example.appchat.data.db.remote.FirebaseAuth
import com.example.appchat.data.model.Login
import com.google.firebase.auth.FirebaseUser

class AuthRepository {
    private val firebaseAuth= FirebaseAuth()


    fun loginUser(login: Login,b:(Resource<FirebaseUser>)->Unit) {
        firebaseAuth.requestLogin(login).addOnCompleteListener {//khi requestLogin() được thực hiện xong thì gọi đến addOnComplete..
            if (it.isSuccessful){//đăng nhập thành công
                b.invoke(Resource.Success(it.result.user))
            }else{
                b.invoke(Resource.Error(null,"Đăng nhập không thành công"))
            }
        }
    }

}