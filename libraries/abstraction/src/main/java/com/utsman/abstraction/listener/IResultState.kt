package com.utsman.abstraction.listener

interface IResultState<T: Any> {
    fun onIdle()
    fun onLoading()
    fun onSuccess(data: T)
    fun onError(throwable: Throwable)
}