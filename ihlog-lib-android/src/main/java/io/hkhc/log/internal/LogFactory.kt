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

package io.hkhc.log.internal

import io.hkhc.log.IHLog
import io.hkhc.log.LogSettings
import io.hkhc.log.providers.AndroidLogProvider

// TODO limit the size of map
object LogFactory {

    private val logMap = mutableMapOf<Class<out Any>, IHLog>()

    fun getCurrentDefaultProvider() =
        LogSettings.defaultProvider ?: (
            FactoryPropertiesLoader().loadProvider()?.also {
                LogSettings.defaultProvider = it
            } ?: AndroidLogProvider()
        )

    internal fun createTag(key: Class<out Any>): String {
        return TagMaker.getLogTag(key)
    }

    internal fun getNewLog(key: Class<out Any>) =
            getCurrentDefaultProvider().getLog(createTag(key))

    fun getLog(key: Class<out Any>): IHLog {
        return logMap[key] ?: (getNewLog(key).also {
                logMap[key] = it
            })
    }

    /**
     * Reset the log factory to initial state, including pruging the cached object, reset the
     * meta tags, and default log provider. It is mainly for unit tests and not for production
     * use.
     */
    fun reset() {
        logMap.clear()
    }

}
