package com.example.taskclass.common.data

data class FieldState<T>(
    val value: T,
    val error: String? = null,
    val validators: List<Validator<T>> = emptyList()
) {

    fun updateValue(newValue: T): FieldState<T> =
        copy(value = newValue).validate()

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
