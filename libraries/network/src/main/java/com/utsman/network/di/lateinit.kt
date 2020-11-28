/*
 * Created by Muhammad Utsman on 28/11/20 3:54 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.network.di

import com.squareup.moshi.Moshi
import com.utsman.abstraction.di.Module
import com.utsman.network.utils.JsonBeautifier

lateinit var moshi: Module<Moshi>
lateinit var jsonBeautifier: Module<JsonBeautifier>