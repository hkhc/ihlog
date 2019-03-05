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

package io.hkhc.log

@Suppress("TooManyFunctions")
interface IHLog {

    /* IHLog class are required to implemented the following */

    fun exception(tag: String?, message: String?, throwable: Throwable)

    /**
     * Implement the provider specific way for logging.
     * @param prority - Priority of log
     * @param tag - optional tag string. If it is null, the provider shall use its own default tag
     * @param message - the log message
     */
    fun log(priority: Severity, tag: String?, message: String)
    fun log(priority: Severity, tag: String?, lambda: () -> String) = log(priority, tag, lambda())

    fun getLogTag(): String

    /* helpers */

    fun logWithFilter(priority: Severity, tag: String?, message: String) {
        if (!priority.shouldBeFilteredBy(LogSettings.logLevel)) {
            log(priority, tag, message)
        }
    }

    fun logWithFilter(priority: Severity, tag: String?, lambda: () -> String) {
        if (!priority.shouldBeFilteredBy(LogSettings.logLevel)) {
            log(priority, tag, lambda)
        }
    }

    fun debug(tag: String, message: String) = logWithFilter(Severity.Debug, tag, message)
    fun debug(message: String) = logWithFilter(Severity.Debug, null, message)
    fun debug(tag: String, lambda: () -> String) = logWithFilter(Severity.Debug, tag, lambda)
    fun debug(lambda: () -> String) = logWithFilter(Severity.Debug, null, lambda)

    fun info(tag: String, message: String) = logWithFilter(Severity.Info, tag, message)
    fun info(message: String) = logWithFilter(Severity.Info, null, message)
    fun info(tag: String, lambda: () -> String) = logWithFilter(Severity.Info, tag, lambda)
    fun info(lambda: () -> String) = logWithFilter(Severity.Info, null, lambda)

    fun warn(tag: String, message: String) = logWithFilter(Severity.Warn, tag, message)
    fun warn(message: String) = logWithFilter(Severity.Warn, null, message)
    fun warn(tag: String, lambda: () -> String) = logWithFilter(Severity.Warn, tag, lambda)
    fun warn(lambda: () -> String) = logWithFilter(Severity.Warn, null, lambda)

    fun trace(tag: String, message: String) = logWithFilter(Severity.Trace, tag, message)
    fun trace(message: String) = logWithFilter(Severity.Trace, null, message)
    fun trace(tag: String, lambda: () -> String) = logWithFilter(Severity.Trace, tag, lambda)
    fun trace(lambda: () -> String) = logWithFilter(Severity.Trace, null, lambda)

    fun fatal(tag: String, message: String) = logWithFilter(Severity.Fatal, tag, message)
    fun fatal(message: String) = logWithFilter(Severity.Fatal, null, message)
    fun fatal(tag: String, lambda: () -> String) = logWithFilter(Severity.Fatal, tag, lambda)
    fun fatal(lambda: () -> String) = logWithFilter(Severity.Fatal, null, lambda)

    fun err(tag: String, message: String) = logWithFilter(Severity.Error, tag, message)
    fun err(message: String) = logWithFilter(Severity.Error, null, message)
    fun err(tag: String, lambda: () -> String) = logWithFilter(Severity.Error, tag, lambda)
    fun err(lambda: () -> String) = logWithFilter(Severity.Error, null, lambda)
    fun err(tag: String, throwable: Throwable) = l.exception(tag, null, throwable)
}
