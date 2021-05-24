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
import io.hkhc.log.IHLogProvider
import io.hkhc.log.Priority
import io.hkhc.log.SystemTimeSource
import java.text.SimpleDateFormat
import java.util.Locale

class BoxLogProvider: IHLogProvider {

    override fun getLog(defaultTag: String) = BoxLog(defaultTag)

    class BoxLog(
        defaultTag: String,
    ) : AbstractIHLog(defaultTag) {

        private var dateFormat = SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.US)
        private val timeSource = SystemTimeSource()
        private val priorityMap = mapOf(
            Priority.Info to 'I', Priority.Warn to 'W', Priority.Debug to 'D',
            Priority.Fatal to 'F', Priority.Error to 'E', Priority.Trace to 'T'
        )

        private fun boxEdge(size: Int, top: Boolean): String {
            val builder = StringBuilder()
            builder.append(if (top) "┌" else "└")
            for(i in 0..(size+1)) builder.append("─")
            builder.append(if (top) "┐" else "┘")
            return builder.toString()
        }

        override fun log(priority: Priority, tag: String?, message: String) {
            val actualTag = tag?:"DEFAULT_TAG"
            val time = dateFormat.format(timeSource.getTime())
            val priorityChar = priorityMap[priority]
            var line = if (message.length > 30)
                message.substring(0, 30)
            else
                message
            for(i in 1..30-message.length) line+=' '
            println(boxEdge(18, true)+boxEdge(1, true)+boxEdge(actualTag.length, true)+boxEdge(30, true))
            println("│ $time ││ $priorityChar ││ $actualTag ││ $line |")
            println(boxEdge(18, false)+boxEdge(1, false)+boxEdge(actualTag.length, false)+boxEdge(30, false))
        }
    }
}
