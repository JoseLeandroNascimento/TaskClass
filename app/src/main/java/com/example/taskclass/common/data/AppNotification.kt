package com.example.taskclass.common.data

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed class AppNotification(val message: String) {
    class Success(message: String) : AppNotification(message)
    class Error(message: String) : AppNotification(message)
    class Info(message: String) : AppNotification(message)
    class Warning(message: String) : AppNotification(message)
}

object NotificationCenter {

    private val _events = MutableSharedFlow<AppNotification>()
    val events = _events.asSharedFlow()

    suspend fun push(notification: AppNotification) {
        _events.emit(notification)
    }
}