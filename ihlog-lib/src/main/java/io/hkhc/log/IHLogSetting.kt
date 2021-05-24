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

import io.hkhc.log.providers.SimpleMetaTag
import io.hkhc.log.providers.StdioLogProvider

data class IHLogSetting(
    var provider: IHLogProvider? = StdioLogProvider(),
    var metaTagPolicy: MetaTag? = SimpleMetaTag(""),
    var defaultPriority: Priority? = Priority.Debug
) {
    fun overlayTo(other: IHLogSetting) {
        provider?.let { other.provider = it }
        metaTagPolicy?.let { other.metaTagPolicy = it }
        defaultPriority?.let { other.defaultPriority = it }
    }

}
