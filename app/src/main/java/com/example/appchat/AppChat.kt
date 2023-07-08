package com.example.appchat

import android.app.Application
import com.example.appchat.ui.SharedPreferencesUtil
import com.google.android.gms.common.util.SharedPreferencesUtils

class AppChat : Application() {
    override fun onCreate() {
        super.onCreate()
        application=this

    }

    companion object {
        lateinit var application: Application
            private set
        var myUserId: String = ""
            get() {
                field = SharedPreferencesUtil.getUserID(application.applicationContext).orEmpty()
                return field
            }
            private set
    }
}