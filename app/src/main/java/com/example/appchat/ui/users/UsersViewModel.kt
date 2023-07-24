package com.example.appchat.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.appchat.data.Event
import com.example.appchat.data.db.entity.User
import com.example.appchat.data.db.repository.DatabaseRepository
import com.example.appchat.ui.DefaultViewModel
import com.example.appchat.data.Result

class UsersViewModelFactory(private val myUserId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UsersViewModel(myUserId) as T
    }
}

class UsersViewModel(private val myUsersId: String) : DefaultViewModel() {
    private val repository: DatabaseRepository = DatabaseRepository()
    private val updateUsersList = MutableLiveData<MutableList<User>>()
    private val _selectUser=MutableLiveData<Event<User>>()
    var selectUser:LiveData<Event<User>> = _selectUser
    val usersList = MediatorLiveData<List<User>>()


    init {
        usersList.addSource(updateUsersList) {

            usersList.value = updateUsersList.value?.filter { it.info.id != myUsersId }
        }
        loadUser()
    }

    private fun loadUser() {
        repository.loadUsers { result: Result<MutableList<User>>->
            onResult(updateUsersList, result)
        }
    }
    fun selectUser(user:User){
        _selectUser.value=Event(user)
    }


}