package com.example.taskclass.common.data

data class FieldState<T>(
    val value: T,
    val error: String? = null,
    val validators: List<Validator<T>> = emptyList()
) {

    fun updateValue(newValue: T, validate: Boolean = true): FieldState<T> {
        val fieldUpdate = copy(value = newValue)
        if (validate) {
            return fieldUpdate.validate()
        }
        return fieldUpdate
    }

    fun validate(): FieldState<T> {
        val messageError = validators
            .asSequence()
            .mapNotNull { it.execute(this.value) }
            .firstOrNull()

        return copy(error = messageError)
    }

    val isValid: Boolean
        get() = error == null
}
