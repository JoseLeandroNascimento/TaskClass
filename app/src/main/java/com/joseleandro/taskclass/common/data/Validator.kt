package com.joseleandro.taskclass.common.data

fun interface Validator<T> {
    fun execute(value: T): String?
}