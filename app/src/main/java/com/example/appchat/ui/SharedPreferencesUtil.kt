package com.example.appchat.ui

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtil {
    private const val PACKGE_NAME="com.example.appchat"
    private const val KEY_USER_ID="user_ìnfo"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PACKGE_NAME,Context.MODE_PRIVATE)
    }
     fun getUserID(context: Context):String?{
        return getPrefs(context).getString(KEY_USER_ID,null)
    }
}