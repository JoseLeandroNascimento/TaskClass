package com.example.taskclass.ui.typeEvents.presentation.typeEvent

import com.example.taskclass.core.data.model.Order
import com.example.taskclass.core.data.model.entity.TypeEventEntity

sealed interface TypeEventAction {
    data class OnDelete(val id: Int) : TypeEventAction
    data class Search(val query: String) : TypeEventAction
    data class ShowSearch(val show: Boolean) : TypeEventAction
    data class OrderBy(val order: Order<TypeEventEntity>) : TypeEventAction
}
