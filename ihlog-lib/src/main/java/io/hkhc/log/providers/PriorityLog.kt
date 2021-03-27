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

import io.hkhc.log.IHLog
import io.hkhc.log.Priority
import io.hkhc.log.internal.LogFactory

class PriorityLog(val delegate: IHLog): IHLog {

    override fun exception(tag: String?, message: String?, throwable: Throwable) {
        if (!Priority.Error.shouldBeFilteredBy(LogFactory.logLevel)) {
            delegate.exception(tag, message, throwable)
        }
    }

    override fun log(priority: Priority, tag: String?, message: String) {
        if (!priority.shouldBeFilteredBy(LogFactory.logLevel)) {
            delegate.log(priority, tag, message)
        }
    }

    override fun log(priority: Priority, tag: String?, lambda: () -> String) {
        if (!priority.shouldBeFilteredBy(LogFactory.logLevel)) {
            delegate.log(priority, tag, lambda)
        }
    }

    override fun getLogTag() = delegate.getLogTag()
}
