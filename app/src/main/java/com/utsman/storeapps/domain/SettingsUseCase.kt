/*
 * Created by Muhammad Utsman on 18/12/20 9:26 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.storeapps.domain

import com.utsman.abstraction.extensions.toBytesReadable
import com.utsman.data.repository.root.RootedRepository
import com.utsman.data.repository.setting.SettingRepository
import com.utsman.data.utils.DownloadUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resume

class SettingsUseCase @Inject constructor(
    rootedRepository: RootedRepository,
    private val settingRepository: SettingRepository
) {
    val isRooted = rootedRepository.rooted()
    val sizeDownload: MutableStateFlow<String> = MutableStateFlow("0 KB")

    fun toggleAutoInstaller(scope: CoroutineScope) = scope.launch {
        settingRepository.toggleAutoInstaller()
    }

    fun toggleMaturity(scope: CoroutineScope) = scope.launch {
        settingRepository.toggleMaturity()
    }

    suspend fun getValueAutoInstallerSync() = settingRepository.autoInstallerSync()
    suspend fun getValueMaturitySync() = settingRepository.maturitySync()

    @InternalCoroutinesApi
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

    @InternalCoroutinesApi
    suspend fun getSizeDownloadDir() {
        sizeDownload.value = calculateDownloadDir().toBytesReadable()
    }

    fun countFile(): Int {
        val dir = DownloadUtils.getDownloadDir()
        val files = dir?.listFiles() ?: emptyArray()
        return files.size
    }

    @InternalCoroutinesApi
    suspend fun cleanFiles(): Boolean {
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