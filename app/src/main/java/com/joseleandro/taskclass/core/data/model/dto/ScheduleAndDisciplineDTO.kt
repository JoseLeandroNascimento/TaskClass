package com.joseleandro.taskclass.core.data.model.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.joseleandro.taskclass.core.data.model.entity.DisciplineEntity
import com.joseleandro.taskclass.core.data.model.entity.ScheduleEntity

data class ScheduleAndDisciplineDTO(
   @Embedded
    val schedule: ScheduleEntity,

    @Relation(
        entityColumn = "discipline_id",
        parentColumn = "discipline_id",
    )
    val discipline: DisciplineEntity
)
