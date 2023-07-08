package com.example.appchat.data.db.entity

import com.google.firebase.database.PropertyName
import java.util.Date

data class Message(
    @get:PropertyName("senderId") @set:PropertyName("senderId") var senderId:String,
    @get:PropertyName("text") @set:PropertyName("text") var text:String,
    @get:PropertyName("epochTimeMs") @set:PropertyName("epochTimeMs") var epochTimeMs:Long=Date().time,
    @get:PropertyName("seen") @set:PropertyName("seen") var seen:Boolean=false
)
