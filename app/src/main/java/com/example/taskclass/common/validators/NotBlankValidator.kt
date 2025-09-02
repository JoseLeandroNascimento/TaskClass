package com.example.taskclass.common.validators

import com.example.taskclass.common.data.Validator

class NotBlankValidator(val messageError: String = "Campo não pode ser vazio") : Validator<String> {
    override fun execute(value: String): String? {
        return if (value.isBlank()) messageError else null
    }
}