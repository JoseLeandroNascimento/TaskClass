package com.joseleandro.taskclass.ui.typeEvents.presentation.typeEvent

import com.joseleandro.taskclass.core.data.model.Order
import com.joseleandro.taskclass.core.data.model.entity.TypeEventEntity

sealed interface TypeEventAction {
    data class OnDelete(val id: Int) : TypeEventAction
    data class Search(val query: String) : TypeEventAction
    data class ShowSearch(val show: Boolean) : TypeEventAction
    data class OrderBy(val order: Order<TypeEventEntity>) : TypeEventAction
}
