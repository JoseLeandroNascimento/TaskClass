package com.example.taskclass.ui.typeEvents.presentation.typeEvent

sealed interface TypeEventAction {
    data class OnDelete(val id: Int): TypeEventAction
}