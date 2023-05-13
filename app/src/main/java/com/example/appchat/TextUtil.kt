package com.example.appchat

import java.util.regex.Pattern

private val EMAIL_ADDRESS:Pattern= Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+")

fun isValidEmail(email: String):Boolean{
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
fun isValidPassword(length:Int,text:String):Boolean{
    if (text.isBlank() ||text.length<length){
        return false
    }
    return true
}