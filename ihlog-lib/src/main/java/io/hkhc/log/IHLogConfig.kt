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

package io.hkhc.log

import io.hkhc.log.internal.LogFactory
import io.hkhc.log.internal.TagMaker
import io.hkhc.log.providers.SimpleMetaTag
import io.hkhc.log.providers.StdioLogProvider

object IHLogConfig {

    var setting: IHLogSetting = IHLogSetting(
        StdioLogProvider(),
        SimpleMetaTag(""),
        Priority.Warn
    )

    init {
        init(setting)
    }

    fun init(newSetting: IHLogSetting) {
        if (newSetting != setting) newSetting.overlayTo(setting)
        with(setting) {
            provider?.let { LogFactory.defaultProvider = it }
            defaultPriority?.let { LogFactory.logLevel = it }
            metaTagPolicy?.let { TagMaker.metaTagPolicy = it }
        }
    }

}
