package com.example.appchat.utils

import com.google.firebase.database.DataSnapshot


fun <T> wrapSnapshotToClass(className: Class<T>, snapshot: DataSnapshot): T? {
    return snapshot.getValue(className)
}

fun <T> wrapSnapshotToArrayList(className: Class<T>, snapshot: DataSnapshot): MutableList<T> {
    val arrList: MutableList<T> = arrayListOf()
    for (child in snapshot.children) {
        child.getValue(className)?.let { arrList.add(it) }
    }
    return arrList
}

fun convertTowUserIDs(userID1: String, userID2: String): String {
    return if (userID1 < userID2) {
        userID2 + userID1
    }else{
        userID1+userID2
    }
}