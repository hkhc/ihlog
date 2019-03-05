/*
 * Copyright (c) 2019. Herman Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package io.hkhc.log.providers

import android.os.Build
import android.util.Log
import io.hkhc.log.AbstractIHLog
import io.hkhc.log.IHLog
import io.hkhc.log.IHLogProvider
import io.hkhc.log.Severity

/**
 * Log provider for the usual Android Log.
 *
 * @property currentOSVersion indicate the Android OS version the class is running on, as specified
 * in Build.VERSION.SDK_INT. Pre-N Android has limitation to on tag size. The provider split the tag
 * so that it can fit in to pre-N tag field. The rest part of tag is moved to the log body.
 *
 */
class AndroidLogProvider(var currentOSVersion: Int = Build.VERSION.SDK_INT) : IHLogProvider {

    internal fun isAndroidN() = currentOSVersion >= Build.VERSION_CODES.N

    override fun getLog(defaultTag: String): IHLog {

        return if (isAndroidN())
            NAndroidIHLog(defaultTag)
        else
            PreNAndroidIHLog(defaultTag)
    }

    open class NAndroidIHLog(defaultTag: String) : AbstractIHLog(defaultTag) {

        override fun log(priority: Severity, tag: String?, message: String) {

            val level = when (priority) {
                Severity.Info -> Log.INFO
                Severity.Trace -> Log.INFO
                Severity.Debug -> Log.DEBUG
                Severity.Error -> Log.ERROR
                Severity.Fatal -> Log.ASSERT
                Severity.Warn -> Log.WARN
            }

            message.lineSequence().forEach { line ->
                Log.println(level, tag ?: defaultTag, line)
            }
        }
    }

    class PreNAndroidIHLog(tag: String) : NAndroidIHLog(tag) {

        private val preNLogTagSize = 23
        private val primaryTag: String
        private val secondaryTag: String

        init {

            if (tag.length>preNLogTagSize) {
                primaryTag = tag.substring(0, preNLogTagSize)
                secondaryTag = tag.substring(preNLogTagSize)
            } else {
                primaryTag = tag
                secondaryTag = ""
            }
        }

        override fun log(priority: Severity, tag: String?, message: String) {

            if (tag == null)
                log(priority, defaultTag, message)
            else {
                if (tag.length>preNLogTagSize) {
                    val pTag = tag.substring(0, preNLogTagSize)
                    val sTag = tag.substring(preNLogTagSize)
                    message.lineSequence().forEach { line ->
                        super.log(priority, pTag, "$sTag: $line")
                    }
                } else
                    super.log(priority, tag, message)
            }
        }
    }
}
