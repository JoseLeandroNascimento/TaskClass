package com.example.taskclass.ui.events.domain

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.EEventStatus
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class CheckedEventUseCase @Inject constructor(
    private val repo: EventRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(eventId: Int, checked: Boolean): Flow<Resource<Unit>> {
        return repo.updateStatus(id = eventId, status = if (checked) EEventStatus.CONCLUIDA else EEventStatus.AGENDADO)
    }

}