package ru.tagilov.avitotrainee.core.util

sealed class TypedResult<T> {
    class Err<T> : TypedResult<T>()
    data class Ok<T>(@Transient val result: T) : TypedResult<T>()
}