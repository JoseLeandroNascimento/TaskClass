package com.joseleandro.taskclass.common.utils

fun Int.parseHors(): String {
    if (this < 10)
        return "0${this}:00"
    return "$this:00"
}
