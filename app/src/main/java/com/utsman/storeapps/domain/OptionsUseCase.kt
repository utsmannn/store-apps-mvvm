/*
 * Created by Muhammad Utsman on 18/12/20 9:26 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps.domain

import com.utsman.abstraction.extensions.toBytesReadable
import com.utsman.data.repository.database.DownloadedRepository
import com.utsman.data.repository.root.RootedRepository
import com.utsman.data.repository.setting.OptionsRepository
import com.utsman.data.utils.DownloadUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.coroutines.resume

class OptionsUseCase @Inject constructor(
    rootedRepository: RootedRepository,
    private val optionsRepository: OptionsRepository,
    private val downloadedRepository: DownloadedRepository
) {
    val isRooted = rootedRepository.rooted()
    val sizeDownload: MutableStateFlow<String> = MutableStateFlow("0 KB")

    fun toggleAutoInstaller(scope: CoroutineScope) = scope.launch {
        optionsRepository.toggleAutoInstaller()
    }

    fun toggleMaturity(scope: CoroutineScope) = scope.launch {
        optionsRepository.toggleMaturity()
    }

    suspend fun getValueAutoInstallerSync() = optionsRepository.autoInstallerSync()
    suspend fun getValueMaturitySync() = optionsRepository.maturitySync()


    private suspend fun calculateDownloadDir(): Long {
        return suspendCancellableCoroutine { task ->
            val dir = DownloadUtils.getDownloadDir()
            val files = dir?.listFiles() ?: emptyArray()
            val total = files.map { f ->
                f.length()
            }.sum()

            if (task.isActive) {
                task.resume(total)
            } else {
                task.tryResume(total)
            }
        }
    }

    suspend fun getSizeDownloadDir() {
        sizeDownload.value = calculateDownloadDir().toBytesReadable()
    }

    fun countFile(): Int {
        val dir = DownloadUtils.getDownloadDir()
        val files = dir?.listFiles() ?: emptyArray()
        return files.size
    }

    suspend fun cleanFiles(): Boolean {
        downloadedRepository.removeAll()
        return suspendCancellableCoroutine { task ->
            val dir = DownloadUtils.getDownloadDir()
            val result = dir?.deleteRecursively() ?: false
            if (task.isActive) {
                task.resume(result)
            } else {
                task.tryResume(result)
            }
        }
    }

}