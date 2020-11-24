package com.utsman.abstraction.dto

sealed class ResultState<T: Any>(val payload: T? = null, val throwable: Throwable? = null, val message: String? = null) {
    class Loading<T: Any> : ResultState<T>()
    class Idle<T: Any>: ResultState<T>()
    data class Success<T: Any>(val data: T) : ResultState<T>(payload = data)
    data class Error<T: Any>(val th: Throwable) : ResultState<T>(throwable = th)
}