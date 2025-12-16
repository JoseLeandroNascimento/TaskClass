package com.joseleandro.taskclass.ui.events.domain

import com.joseleandro.taskclass.common.data.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class CheckedEventUseCase @Inject constructor(
    private val repo: EventRepository
) {

    operator fun invoke(eventId: Int, checked: Boolean): Flow<Resource<Unit>> {

        return repo.updateCompleted(id = eventId, checked)
    }

}