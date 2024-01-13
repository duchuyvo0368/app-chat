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
import com.example.appchat.utils.addNewItem
import com.example.appchat.utils.convertTowUserIDs
import com.example.appchat.utils.updateItemAt


//tạo ra môt viewmodel để truyen tham so vào hàm khoi tao
class ChatsViewModelFactory(private val myUserID: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatsViewModel(myUserID) as T
    }
}


class ChatsViewModel(val myUserID: String) : DefaultViewModel() {
    private val repository: DatabaseRepository = DatabaseRepository()
    private val firebaseReferenceObserverList = ArrayList<FirebaseReferenceValueObserver>()
    private val _updatedChatWithUserInfo = MutableLiveData<ChatWithUserInfo>()
    private val _selectedChat = MutableLiveData<Event<ChatWithUserInfo>>()

    var selectedChat: LiveData<Event<ChatWithUserInfo>> = _selectedChat

    //MediatorLiveData ket hop dư lieu 2 livedata,vừa lang nghe va update
    var chatsList = MediatorLiveData<MutableList<ChatWithUserInfo>>()


    init {
        //addSource dung them mo livedata vao MediatorLiveData va xu ly logic
        chatsList.addSource(_updatedChatWithUserInfo) { newChat ->
                val chat = chatsList.value?.find { it.mChat.info.id == newChat.mChat.info.id }//tim kiếm trò chuyện trong ds(find:tìm thấy)
            if (chat == null) {//thêm mới
                chatsList.addNewItem(newChat)
            } else {//cập nhật lại
                chatsList.updateItemAt(newChat, chatsList.value!!.indexOf(chat))
            }
        }
        setupChats()
    }

    private fun setupChats() {
        loadFriends()
    }

    private fun loadFriends() {
        repository.loadFriends(myUserID) { result: Result<List<UserFriend>> ->
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
        repository.loadAndObserveChat(convertTowUserIDs(myUserID,userInfo.id),observer){ result:Result<Chat>->
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
    fun selectChatWithUserInfoPressed(chat:ChatWithUserInfo){
        _selectedChat.value=Event(chat)
    }

}