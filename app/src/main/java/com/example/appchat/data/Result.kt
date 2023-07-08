package com.example.appchat.data


//sealed định nghĩa các hoạt động của lớp con
//<T> truyền kiểu tùy ý
sealed class Result<out R>
{
    data class Success< out T>(val data: T?=null,val msg:String?=null):Result<T>()
    class Error(val msg: String?=null):Result<Nothing>()
    object Loading : Result<Nothing>()


}