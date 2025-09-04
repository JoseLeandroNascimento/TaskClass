package com.example.taskclass.common.validators

import com.example.taskclass.common.data.Validator

open class RequiredValidator<T>(val messageError: String = "Campo é obrigatório") : Validator<T?> {
    override fun execute(value: T?): String? {
        return if (value == null) messageError else null
    }
}