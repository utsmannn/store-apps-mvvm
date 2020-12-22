/*
 * Created by Muhammad Utsman on 18/12/20 9:00 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.root

import android.content.Context
import com.scottyab.rootbeer.RootBeer
import com.utsman.abstraction.extensions.logi
import com.utsman.data.model.dto.entity.ErrorLogInstallerEntity
import com.utsman.data.model.dto.rooted.CommandResult
import com.utsman.data.repository.database.ErrorLogInstallerRepository
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import kotlin.coroutines.resume

class RootedRepositoryImplement @Inject constructor(
    private val context: Context,
    private val errorLogInstallerRepository: ErrorLogInstallerRepository
) : RootedRepository {

    override fun rooted(): Boolean {
        val rootBeer = RootBeer(context)
        return rootBeer.checkForRootNative() && rootBeer.isRooted
    }

    @InternalCoroutinesApi
    override suspend fun installApk(dir: String, name: String?): CommandResult =
        suspendCancellableCoroutine { task ->
            CoroutineScope(Dispatchers.IO).launch {
                val command = "pm install -r $dir"

                try {
                    val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
                    val errorReader = BufferedReader(InputStreamReader(process.errorStream))
                    val errorLine = errorReader.readLine()
                    process.waitFor()

                    if (errorLine != null) {
                        val errorLogEntity = ErrorLogInstallerEntity(
                            name = name,
                            dir = dir,
                            reason = errorLine,
                            millis = System.currentTimeMillis()
                        )

                        errorLogInstallerRepository.insertError(errorLogEntity)
                        task.resume(false, errorLine)
                    } else {
                        task.resume(true, "Success")
                    }


                } catch (e: RuntimeException) {
                    e.printStackTrace()
                    task.resume(false, e.localizedMessage)
                } catch (e: IOException) {
                    e.printStackTrace()
                    task.resume(false, e.localizedMessage)
                } catch (e: SecurityException) {
                    e.printStackTrace()
                    task.resume(false, e.localizedMessage)
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    task.resume(false, e.localizedMessage)
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                    task.resume(false, e.localizedMessage)
                }
            }
        }

    @InternalCoroutinesApi
    private fun CancellableContinuation<CommandResult>.resume(success: Boolean, message: String?) {
        if (isActive) {
            resume(CommandResult(success, message))
        } else {
            tryResume(CommandResult(success, message))
        }
    }
}