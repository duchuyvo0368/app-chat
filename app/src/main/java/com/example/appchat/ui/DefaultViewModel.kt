package com.example.appchat.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appchat.data.Event

abstract class DefaultViewModel : ViewModel() {
    protected val mSnackBarText = MutableLiveData<Event<String>>()
    val snackBarText:LiveData<Event<String>> =mSnackBarText
}