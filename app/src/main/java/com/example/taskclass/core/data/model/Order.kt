package com.example.taskclass.core.data.model

import kotlin.reflect.KProperty1

data class Order<T>(
    val selector: KProperty1<T, Comparable<*>?>,
    val ascending: Boolean = true
){
    fun getValue(item: T) = selector.get(item)
}
