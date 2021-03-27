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

/**
 * Log provider for log to redirect log to multiple [IHLog] instances. All of them share the same
 * tag.
 *
 * @property providers List of [IHLogProvider]. [IHLog] object is created from each of those
 * providers. Every log line is redirected all of those [IHLog] object.
 *
 */
class CompositeLogProvider(vararg val providers: IHLogProvider) : IHLogProvider {

    override fun getLog(defaultTag: String): IHLog =
            CompositeLog(defaultTag,
                    Array(providers.size) { providers[it].getLog(defaultTag) }
            )

    class CompositeLog(defaultTag: String, var loggers: Array<IHLog>) : AbstractIHLog(defaultTag) {

        override fun log(priority: Priority, tag: String?, message: String) {
            loggers.forEach { it.log(priority, tag, message) }
        }
    }
}
