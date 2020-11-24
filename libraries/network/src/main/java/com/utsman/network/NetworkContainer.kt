package com.utsman.network

import com.squareup.moshi.Moshi

class NetworkContainer {

    companion object {
        val get by lazy {
            NetworkContainer()
        }
    }

    lateinit var moshi: Moshi

    init {
        setupMoshi()
    }

    private fun setupMoshi() = run {
        moshi = Moshi.Builder().build()
    }

}