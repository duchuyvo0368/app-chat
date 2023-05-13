package com.example.appchat.data

import androidx.lifecycle.Observer


//open class định nghĩa một lớp có thể kế thừu và mở rộng từ lớp con khác
//<out T> là kiểu dữ liệu trả về là kiểu con của T(không cần ép kiểu)
//<T> là kiểu dữ liệu trả về là T
open class Event<out T>(private val content: T) {
    private var isHandled = false

    fun getContent(): T? {
        return if (isHandled) {
            null
        } else {
            isHandled = true
            content
        }
    }
}
//Lambda expression (onEventUnhandledContent: (T) ->Unit)
class EventObserver<T>(private val onEventUnhandledContent: (T) ->Unit):Observer<Event<T>>{
    override fun onChanged(value: Event<T>) {
        //lắng nghe khi giá trị của LiveData thay đổi,nếu giá trị k null thì truyền vào onEventUnhandledContent()
        value.getContent()?.let { onEventUnhandledContent(it) }
    }

}