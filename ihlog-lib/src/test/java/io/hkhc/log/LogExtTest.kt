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

import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

class LogExtTest {

    @Rule @JvmField
    val logTesterRule = LogTesterRule()

    @Test fun `test trace lambda with filter`() {

        var count = 0

        LogSettings.logLevel=Severity.Trace
        trace { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        LogSettings.logLevel=Severity.Error
        trace { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)
    }

    @Test fun `test trace tag + lambda with filter`() {

        var count = 0

        LogSettings.logLevel=Severity.Trace
        trace("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        LogSettings.logLevel=Severity.Error
        trace("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)
    }

    @Test fun `test trace tag + message`() {
        trace("TAG", "Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG t/Hello")
    }

    @Test fun `test trace messaage`() {
        trace("Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET t/Hello")
    }

    @Test fun `test trace tag + lambda`() {
        trace("TAG") { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG t/Hello")
    }

    @Test fun `test trace lambda`() {
        trace { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET t/Hello")
    }

    @Test fun `test info lambda with filter`() {

        var count = 0

        LogSettings.logLevel=Severity.Trace
        info { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        LogSettings.logLevel=Severity.Error
        info { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)
    }

    @Test fun `test info tag + lambda with filter`() {

        var count = 0

        LogSettings.logLevel=Severity.Trace
        info("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        LogSettings.logLevel=Severity.Error
        info("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)
    }

    @Test fun `test info tag + message`() {
        info("TAG", "Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG i/Hello")
    }

    @Test fun `test info messaage`() {
        info("Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET i/Hello")
    }

    @Test fun `test info tag + lambda`() {
        info("TAG") { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG i/Hello")
    }

    @Test fun `test info lambda`() {
        info { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET i/Hello")
    }

    @Test fun `test debug lambda with filter`() {

        var count = 0

        LogSettings.logLevel=Severity.Trace
        debug { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        LogSettings.logLevel=Severity.Error
        debug { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)
    }

    @Test fun `test debug tag + lambda with filter`() {

        var count = 0

        LogSettings.logLevel=Severity.Trace
        debug("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        LogSettings.logLevel=Severity.Error
        debug("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)
    }

    @Test fun `test debug tag + message`() {
        debug("TAG", "Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG d/Hello")
    }

    @Test fun `test debug messaage`() {
        debug("Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET d/Hello")
    }

    @Test fun `test debug tag + lambda`() {
        debug("TAG") { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG d/Hello")
    }

    @Test fun `test debug lambda`() {
        debug { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET d/Hello")
    }

    @Test fun `test warn lambda with filter`() {

        var count = 0

        LogSettings.logLevel=Severity.Trace
        warn { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        LogSettings.logLevel=Severity.Error
        warn { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)
    }

    @Test fun `test warn tag + lambda with filter`() {

        var count = 0

        LogSettings.logLevel=Severity.Trace
        warn("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        LogSettings.logLevel=Severity.Error
        warn("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)
    }

    @Test fun `test warn tag + message`() {
        warn("TAG", "Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG w/Hello")
    }

    @Test fun `test warn messaage`() {
        warn("Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET w/Hello")
    }

    @Test fun `test warn tag + lambda`() {
        warn("TAG") { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG w/Hello")
    }

    @Test fun `test warn lambda`() {
        warn { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET w/Hello")
    }

    @Test fun `test err tag + lambda with filter`() {

        var count = 0

        LogSettings.logLevel=Severity.Trace
        err("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        LogSettings.logLevel=Severity.Fatal
        err("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)
    }

    @Test fun `test err tag + message`() {
        err("TAG", "Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG e/Hello")
    }

    @Test fun `test err messaage`() {
        err("Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET e/Hello")
    }

    @Test fun `test err tag + lambda`() {
        err("TAG") { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG e/Hello")
    }

    @Test fun `test err lambda`() {
        err { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET e/Hello")
    }

    @Test fun `test fatal tag + lambda with filter`() {

        var count = 0

        LogSettings.logLevel=Severity.Trace
        fatal("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        // Fatal log cannot be masked

    }

    @Test fun `test fatal tag + message`() {
        fatal("TAG", "Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG f/Hello")
    }

    @Test fun `test fatal messaage`() {
        fatal("Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET f/Hello")
    }

    @Test fun `test fatal tag + lambda`() {
        fatal("TAG") { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG f/Hello")
    }

    @Test fun `test fatal lambda`() {
        fatal { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET f/Hello")
    }

}