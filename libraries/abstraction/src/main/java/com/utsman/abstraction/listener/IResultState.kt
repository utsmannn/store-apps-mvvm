/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.abstraction.listener

interface IResultState<T: Any> {
    fun onIdle()
    fun onLoading()
    fun onSuccess(data: T)
    fun onError(throwable: Throwable)
}