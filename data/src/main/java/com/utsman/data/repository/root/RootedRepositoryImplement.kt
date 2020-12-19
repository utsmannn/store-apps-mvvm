/*
 * Created by Muhammad Utsman on 18/12/20 9:00 PM
 * Copyright (c) 2020 . All rights reserved.
 */

package com.utsman.data.repository.root

import android.content.Context
import com.scottyab.rootbeer.RootBeer
import com.utsman.abstraction.extensions.logi
import com.utsman.data.model.dto.rooted.CommandResult
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume

class RootedRepositoryImplement @Inject constructor(private val context: Context) : RootedRepository {

    private val listCheckError = listOf(
        "denied", "denial", "error", "failure", "not allow"
    )

    override fun rooted(): Boolean {
        val rootBeer = RootBeer(context)
        return rootBeer.checkForRootNative() && rootBeer.isRooted
    }

    @InternalCoroutinesApi
    override suspend fun installApk(dir: String): CommandResult = suspendCancellableCoroutine { task ->
        CoroutineScope(Dispatchers.IO).launch {
            val command = "pm install -r $dir"

            try {
                val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
                process.waitFor()
                val errorReader = BufferedReader(InputStreamReader(process.inputStream))
                val errorLine = errorReader.readLine()
                logi("error line is not null -> $errorLine")

                val errorMessageLower = errorLine.toLowerCase(Locale.getDefault())

                for (check in listCheckError) {
                    if (errorMessageLower.contains(check)) {
                        task.resume(false, errorLine)
                        break
                    } else {
                        task.resume(true, errorLine)
                    }
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