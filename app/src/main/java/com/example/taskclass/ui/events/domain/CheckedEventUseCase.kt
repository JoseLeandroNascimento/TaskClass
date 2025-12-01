package com.example.taskclass.ui.events.domain

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.enums.EEventStatus
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class CheckedEventUseCase @Inject constructor(
    private val repo: EventRepository
) {

    operator fun invoke(eventId: Int, checked: Boolean): Flow<Resource<Unit>> {

        return repo.updateCompleted(id = eventId, checked)
    }

}