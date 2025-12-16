package com.joseleandro.taskclass.ui.events.presentation.eventCreateScreen

import com.joseleandro.taskclass.common.data.FieldState
import com.joseleandro.taskclass.common.validators.NotBlankValidator
import com.joseleandro.taskclass.common.validators.RequiredValidator
import com.joseleandro.taskclass.core.data.model.entity.TypeEventEntity

data class FormState(
    val title: FieldState<String> = FieldState(
        "", validators = listOf(
            NotBlankValidator(messageError = "Forneça um nome valido")
        )
    ),
    val date: FieldState<String> = FieldState(
        "", validators = listOf(
            NotBlankValidator(messageError = "Data inválida")
        )
    ),
    val time: FieldState<String> = FieldState(
        "", validators = listOf(
            NotBlankValidator(messageError = "Hora inválida")
        )
    ),
    val description: FieldState<String> = FieldState(""),
    val typeEventSelected: FieldState<TypeEventEntity?> = FieldState(null, validators = listOf(
        RequiredValidator(messageError = "Campo obrigatório")
    )),
) {

    fun validateAll(): FormState {
        return copy(
            title = title.validate(),
            date = date.validate(),
            time = time.validate(),
            description = description.validate(),
            typeEventSelected = typeEventSelected.validate()
        )
    }

    fun isValid(): Boolean {
        val validated = validateAll()
        return validated.title.isValid &&
                validated.date.isValid &&
                validated.time.isValid &&
                validated.description.isValid &&
                validated.typeEventSelected.isValid
    }
}