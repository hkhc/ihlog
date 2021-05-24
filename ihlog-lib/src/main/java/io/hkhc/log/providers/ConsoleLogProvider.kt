/*
 * Copyright (c) 2021. Herman Cheung
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

import io.hkhc.log.AbstractIHLog
import io.hkhc.log.IHLog
import io.hkhc.log.IHLogProvider
import io.hkhc.log.Priority
import io.hkhc.log.Priority.Debug
import io.hkhc.log.Priority.Error
import io.hkhc.log.Priority.Fatal
import io.hkhc.log.Priority.Info
import io.hkhc.log.Priority.Trace
import io.hkhc.log.Priority.Warn
import io.hkhc.log.SystemTimeSource
import io.hkhc.log.TimeSource
import java.io.PrintWriter

/**
 * Log provider to send log to two other [IHLog]s. One is for less than error output, and the other
 * for error, fatal or exception. Both [IHLog]s share the same tag.
 *
 * @property timeSource Time source implementation to get real time clock
 * @property outLogProvider Log provider to create log for non-error output.
 * @property errLogProvider Log provider to create log for error output.
 *
 */
class ConsoleLogProvider(
    val timeSource: TimeSource = SystemTimeSource(),
    val outLogProvider: PrintWriterLogProvider =
        PrintWriterLogProvider(PrintWriter(System.out), timeSource),
    val errLogProvider: PrintWriterLogProvider =
        PrintWriterLogProvider(PrintWriter(System.err), timeSource),
) : IHLogProvider {

    override fun getLog(defaultTag: String): IHLog =
            ConsoleLog(
                defaultTag,
                outLogProvider.getLog(defaultTag),
                errLogProvider.getLog(defaultTag)
            )

    class ConsoleLog(defaultTag: String, var outLogger: IHLog, var errLogger: IHLog)
        : AbstractIHLog(defaultTag) {

        override fun log(priority: Priority, tag: String?, message: String) {
            when(priority) {
                Info, Trace, Debug ->
                    outLogger.log(priority, tag, message)
                Warn, Error, Fatal ->
                    errLogger.log(priority, tag, message)
            }
        }
    }
}
