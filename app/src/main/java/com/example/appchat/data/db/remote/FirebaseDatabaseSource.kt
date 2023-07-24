package com.example.appchat.data.db.remote

import com.example.appchat.data.db.entity.User
import com.example.appchat.ui.wrapSnapshotToClass
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.*
import com.example.appchat.data.Result
import com.example.appchat.data.db.entity.UserNotification
import com.example.appchat.data.db.entity.UserRequest
import java.lang.Exception


class FirebaseDataSource() {
    companion object {
        val dbInstance = FirebaseDatabase.getInstance()

    }

    //
    private fun attachValueListenerToTaskCompletion(src: TaskCompletionSource<DataSnapshot>): ValueEventListener {
        return (object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                src.setResult(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                src.setException(Exception(error.message))
            }

        })
    }

    private fun <T> attachValueListenerToBlock(
        resultName: Class<T>,
        b: ((Result<T>) -> Unit)
    ): ValueEventListener {
        return (object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (wrapSnapshotToClass(resultName, snapshot) == null) {
                    b.invoke(Result.Error(snapshot.key))
                } else {
                    b.invoke(Result.Success(wrapSnapshotToClass(resultName, snapshot)))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                b.invoke(Result.Error(error.message))
            }

        })
    }

    private fun refPath(path: String): DatabaseReference {
        return dbInstance.reference.child(path)
    }

    fun updateNewUser(user: User) {
        refPath("users/${user.info.id}").setValue(user)
    }

    fun loadFriendsTask(userID: String): Task<DataSnapshot> {
        val src = TaskCompletionSource<DataSnapshot>()
        val listener = attachValueListenerToTaskCompletion(src)
        refPath("users/$userID/friends").addListenerForSingleValueEvent(listener)
        return src.task
    }
    fun loadUserInfoTask(userID:String):Task<DataSnapshot>{
        val src=TaskCompletionSource<DataSnapshot>()
        val listener=attachValueListenerToTaskCompletion(src)
        refPath("users/$userID/info").addListenerForSingleValueEvent(listener)
        return src.task
    }
    fun loadUsersTask():Task<DataSnapshot>{
        val src=TaskCompletionSource<DataSnapshot>()
        val listener=attachValueListenerToTaskCompletion(src)
        refPath("users").addListenerForSingleValueEvent(listener)
        return src.task
    }


    fun <T> attachChatObserver(
        resultClassName: Class<T>,
        chaiID: String,
        refObs: FirebaseReferenceValueObserver,
        b: ((Result<T>) -> Unit)
    ) {
        val listener = attachValueListenerToBlock(resultClassName, b)
    }

    fun loadUserTask(userID: String): Task<DataSnapshot> {
        val src=TaskCompletionSource<DataSnapshot>()
        val listener=attachValueListenerToTaskCompletion(src)
        refPath("users/$userID")
        return src.task
    }

    fun updateNewSentRequest(userID: String, userRequest: UserRequest) {
        refPath("users/${userID}/sentRequests/${userRequest.userID}").setValue(userRequest)
    }

    fun updateNewNotification(otherUserID: String, userNotification: UserNotification) {
        refPath("users/${otherUserID}/notifications/${userNotification.userID}").setValue(userNotification)
    }


}

class FirebaseReferenceValueObserver {
    //ChildEventListener nhan cac su kien thay doi o vi tri cua realtime database(DatabaseReference)
    private var valueEventListener: ChildEventListener? = null

    //DatabaseReference tham chieu den duong dan
    private var dbRef: DatabaseReference? = null

    //true đang theo dõi các sự kiện từ dbRef. false, điều đó có nghĩa là không
    private var isObserving: Boolean = false

    fun start(valueEventListener: ChildEventListener, reference: DatabaseReference) {
        isObserving = true
        reference.addChildEventListener(valueEventListener)
        this.valueEventListener = valueEventListener
        this.dbRef = reference
    }

    fun clear() {
        valueEventListener?.let { dbRef?.removeEventListener(it) }
        valueEventListener = null
        isObserving = false
        dbRef = null
    }

    fun isObserving(): Boolean {
        return isObserving
    }

}