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

import io.hkhc.log.providers.FakeTimeSource
import io.hkhc.log.providers.StringLogProvider
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.StringWriter

class StringLogProviderTest {

    private lateinit var testResult: StringWriter

    @Before
    fun setup() {
        testResult = StringWriter()
        IHLogConfig.init(IHLogSetting(
            provider = StringLogProvider(testResult, FakeTimeSource())
        ))
    }

    @Test
    fun testLogWithFake() {
        l.debug("Hello")
        l.debug("World")
        l.debug("Banana")
        l.debug("Orange")
        assertEquals("""
            01-01 08:00:00.000  -/SLPT d/Hello
            01-01 08:00:00.001  -/SLPT d/World
            01-01 08:00:00.002  -/SLPT d/Banana
            01-01 08:00:00.003  -/SLPT d/Orange
        """.trimIndent()+"\n", testResult.toString())
    }


}
