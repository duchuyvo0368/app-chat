package com.example.appchat.ui.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import  com.example.appchat.data.Result
import androidx.lifecycle.ViewModelProvider
import com.example.appchat.data.db.remote.FirebaseReferenceValueObserver
import com.example.appchat.data.db.repository.DatabaseRepository
import com.example.appchat.data.model.ChatWithUserInfo
import com.example.appchat.ui.DefaultViewModel
import com.example.appchat.data.Event
import com.example.appchat.data.db.entity.Chat
import com.example.appchat.data.db.entity.UserFriend
import com.example.appchat.data.db.entity.UserInfo
import com.example.appchat.ui.addNewItem
import com.example.appchat.ui.convertTowUserIDs
import com.example.appchat.ui.updateItemAt


//tạo ra môt viewmodel để truyen tham so vào hàm khoi tao
class ChatsViewModelFactory(private val myUserId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatsViewModel(myUserId) as T
    }
}


class ChatsViewModel(val myUserId: String) : DefaultViewModel() {
    private val repository: DatabaseRepository = DatabaseRepository()
    private val firebaseReferenceObserverList = ArrayList<FirebaseReferenceValueObserver>()
    private val _updatedChatWithUserInfo = MutableLiveData<ChatWithUserInfo>()
    private val _selectedChat = MutableLiveData<Event<ChatWithUserInfo>>()

    var selectedChat: LiveData<Event<ChatWithUserInfo>> = _selectedChat

    //MediatorLiveData ket hop dư lieu 2 livedata,vua lang nghe va update
    var chatsList = MediatorLiveData<MutableList<ChatWithUserInfo>>()


    init {
        //addSource dung them mo livedata vao MediatorLiveData va xu ly logic
        chatsList.addSource(_updatedChatWithUserInfo) { newChat ->
            val chat = chatsList.value?.find { it.mChat.info.id == newChat.mChat.info.id }
            if (chat == null) {
                chatsList.addNewItem(newChat)
            } else {
                chatsList.updateItemAt(newChat, chatsList.value!!.indexOf(chat))
            }
        }
        setupChats()
    }

    private fun setupChats() {
        loadFriends()
    }

    private fun loadFriends() {
        repository.loadFriends(myUserId) { result: Result<List<UserFriend>> ->
            onResult(null, result)
            if (result is Result.Success)
                result.data?.forEach { loadUserInfo(it) }
        }
    }

    private fun loadUserInfo(userFriend: UserFriend) {
        repository.loadUserInfo(userFriend.userID) { result: Result<UserInfo> ->
            onResult(null, result)
            if (result is Result.Success) result.data?.let { loadAndObserveChat(it) }
        }
    }

    private fun loadAndObserveChat(userInfo: UserInfo) {
        val observer = FirebaseReferenceValueObserver()
        firebaseReferenceObserverList.add(observer)
        repository.loadAndObserveChat(convertTowUserIDs(myUserId,userInfo.id),observer){result:Result<Chat>->
            if (result is Result.Success) {
                _updatedChatWithUserInfo.value=result.data?.let { ChatWithUserInfo(it,userInfo) }
            }else if (result is Result.Error) {
                chatsList.value?.let {
                    val newList= mutableListOf<ChatWithUserInfo>().apply { addAll(it) }
                    newList.removeIf { it2->result.msg.toString().contains(it2.myInfo.id) }
                    chatsList.value=newList
                }
            }
        }

    }

}