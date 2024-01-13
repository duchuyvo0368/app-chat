package com.example.appchat.utils

import androidx.lifecycle.MutableLiveData
//mở rộng MutableLiveData
fun <T> MutableLiveData<MutableList<T>>.addNewItem(item: T) {
    val newList = mutableListOf<T>()
    this.value?.let { newList.addAll(it) }//nếu khác null thêm các phần tử vào newList
    newList.add(item)//thêm các phần tử item vào cuối danh sách
    this.value = newList//gán newList cho MutableLiveData
}
//
fun <T> MutableLiveData<MutableList<T>>.updateItemAt(item:T,index:Int){
    val newList= mutableListOf<T>()
    this.value?.let { newList.addAll(it) }//nếu khác null thêm các phần tử vào newList
    newList[index]=item//vị trí cập nhật
    this.value=newList//gán newList cho MutableLiveData
}
fun <T> MutableLiveData<MutableList<T>>.removeItem(item: T) {
    val newList = mutableListOf<T>()
    this.value?.let { newList.addAll(it) }
    newList.remove(item)
    this.value = newList
}
