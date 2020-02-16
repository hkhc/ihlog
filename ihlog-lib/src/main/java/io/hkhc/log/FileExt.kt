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

import io.hkhc.log.internal.TagMaker
import java.io.File

const val MAX_FILE_LOG_SIZE = 4096

private fun getTag(): String {

    val ex = Exception()
    val stackTrace = ex.stackTrace
    val firstClass = stackTrace[0].className
    for (element in ex.stackTrace) {
        if (element.className != firstClass)
            return TagMaker.getClassNameAbbr(element.className)
    }
    return ""
}

fun File.log(severity: Severity, tag: String?, maxSize: Int) {

    val ex = Exception()
    val parentTag = tag ?: TagMaker.getClassNameAbbr(ex.stackTrace[1].className)
    var outputtedSize = 0

    forEachLine {
        val len = it.length
        if (outputtedSize != maxSize) {
            if (outputtedSize + len > maxSize) {
                l.log(severity, parentTag, it.substring(0, maxSize - outputtedSize))
                outputtedSize = maxSize
            } else {
                l.log(severity, parentTag, it)
                outputtedSize += len
            }
        }
    }
}

fun File.trace(maxSize: Int = MAX_FILE_LOG_SIZE) = log(Severity.Trace, getTag(), maxSize)
fun File.info(maxSize: Int = MAX_FILE_LOG_SIZE) = log(Severity.Info, getTag(), maxSize)
fun File.debug(maxSize: Int = MAX_FILE_LOG_SIZE) = log(Severity.Debug, getTag(), maxSize)
fun File.warn(maxSize: Int = MAX_FILE_LOG_SIZE) = log(Severity.Warn, getTag(), maxSize)
fun File.err(maxSize: Int = MAX_FILE_LOG_SIZE) = log(Severity.Error, getTag(), maxSize)
fun File.fatal(maxSize: Int = MAX_FILE_LOG_SIZE) = log(Severity.Fatal, getTag(), maxSize)
