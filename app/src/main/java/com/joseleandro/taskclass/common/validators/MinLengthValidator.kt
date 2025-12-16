package com.joseleandro.taskclass.common.validators

import com.joseleandro.taskclass.common.data.Validator
import java.util.Locale

class MinLengthValidator(val min: Int, val messageError: String = "Tamanho mínimo é %d") :
    Validator<String> {
    override fun execute(value: String): String? {
        return if (value.length < min) String.format(Locale.getDefault(),messageError, min) else null
    }
}