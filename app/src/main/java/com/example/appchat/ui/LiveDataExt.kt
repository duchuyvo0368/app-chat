package com.example.appchat.ui

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<MutableList<T>>.addNewItem(item: T) {
    val newList = mutableListOf<T>()
    this.value?.let { newList.addAll(it) }
    newList.add(item)
    this.value = newList
}
fun <T> MutableLiveData<MutableList<T>>.updateItemAt(item:T,index:Int){
    val newList= mutableListOf<T>()
    this.value?.let { newList.addAll(it) }
    newList[index]=item
    this.value=newList
}