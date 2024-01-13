package com.example.appchat

import android.app.Application
import com.example.appchat.utils.SharedPreferencesUtil


class AppChat : Application() {
    override fun onCreate() {
        super.onCreate()
        application=this//(tham chieu den Application)

    }
//lay id tu moi noi cua ung dung dam bao gia tri k bi mat khi ung dung chay
    companion object {
        lateinit var application: Application
            private set//chi cho trong companion object ms co quyen truy cap(application)
        var myUserID: String = ""
            get() {
                field = SharedPreferencesUtil.getUserID(application.applicationContext).orEmpty()
                return field
            }
            private set//chi cho trong companion object và lop ms co quyen truy cap(myUserID)
    }
}