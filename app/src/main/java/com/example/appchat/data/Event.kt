package com.example.appchat.data


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
class EventObserver<T>(private val onEventUnhandledContent: (T) ->Unit)