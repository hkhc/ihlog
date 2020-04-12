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

    init {
        LogSettings.metaTag = ""
    }

    @Rule @JvmField
    val logTesterRule = LogTesterRule()

    @Test fun `test trace lambda with filter`() {

        var count = 0

        LogSettings.logLevel = Priority.Trace
        l.l.trace { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        LogSettings.logLevel = Priority.Error
        l.trace { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)
    }

    @Test fun `test trace tag + lambda with filter`() {

        var count = 0

        LogSettings.logLevel = Priority.Trace
        l.trace("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        LogSettings.logLevel = Priority.Error
        l.trace("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)
    }

    @Test fun `test trace tag + message`() {
        l.trace("TAG", "Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG t/Hello")
    }

    @Test fun `test trace messaage`() {
        l.trace("Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET t/Hello")
    }

    @Test fun `test trace tag + lambda`() {
        l.trace("TAG") { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG t/Hello")
    }

    @Test fun `test trace lambda`() {
        l.trace { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET t/Hello")
    }

    @Test fun `test info lambda with filter`() {

        var count = 0

        LogSettings.logLevel = Priority.Trace
        l.info { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        LogSettings.logLevel = Priority.Error
        l.info { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)
    }

    @Test fun `test info tag + lambda with filter`() {

        var count = 0

        LogSettings.logLevel = Priority.Trace
        l.info("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        LogSettings.logLevel = Priority.Error
        l.info("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)
    }

    @Test fun `test info tag + message`() {
        l.info("TAG", "Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG i/Hello")
    }

    @Test fun `test info messaage`() {
        l.info("Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET i/Hello")
    }

    @Test fun `test info tag + lambda`() {
        l.info("TAG") { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG i/Hello")
    }

    @Test fun `test info lambda`() {
        l.info { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET i/Hello")
    }

    @Test fun `test debug lambda with filter`() {

        var count = 0

        LogSettings.logLevel = Priority.Trace
        l.debug { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        LogSettings.logLevel = Priority.Error
        l.debug { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)
    }

    @Test fun `test debug tag + lambda with filter`() {

        var count = 0

        LogSettings.logLevel = Priority.Trace
        l.debug("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        LogSettings.logLevel = Priority.Error
        l.debug("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)
    }

    @Test fun `test debug tag + message`() {
        l.debug("TAG", "Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG d/Hello")
    }

    @Test fun `test debug messaage`() {
        l.debug("Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET d/Hello")
    }

    @Test fun `test debug tag + lambda`() {
        l.debug("TAG") { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG d/Hello")
    }

    @Test fun `test debug lambda`() {
        l.debug { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET d/Hello")
    }

    @Test fun `test warn lambda with filter`() {

        var count = 0

        LogSettings.logLevel = Priority.Trace
        l.warn { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        LogSettings.logLevel = Priority.Error
        l.warn { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)
    }

    @Test fun `test warn tag + lambda with filter`() {

        var count = 0

        LogSettings.logLevel = Priority.Trace
        l.warn("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        LogSettings.logLevel = Priority.Error
        l.warn("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)
    }

    @Test fun `test warn tag + message`() {
        l.warn("TAG", "Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG w/Hello")
    }

    @Test fun `test warn messaage`() {
        l.warn("Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET w/Hello")
    }

    @Test fun `test warn tag + lambda`() {
        l.warn("TAG") { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG w/Hello")
    }

    @Test fun `test warn lambda`() {
        l.warn { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET w/Hello")
    }

    @Test fun `test err tag + lambda with filter`() {

        var count = 0

        LogSettings.logLevel = Priority.Trace
        l.err("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        LogSettings.logLevel = Priority.Fatal
        l.err("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)
    }

    @Test fun `test err tag + message`() {
        l.err("TAG", "Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG e/Hello")
    }

    @Test fun `test err messaage`() {
        l.err("Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET e/Hello")
    }

    @Test fun `test err tag + lambda`() {
        l.err("TAG") { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG e/Hello")
    }

    @Test fun `test err lambda`() {
        l.err { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET e/Hello")
    }

    @Test fun `test fatal tag + lambda with filter`() {

        var count = 0

        LogSettings.logLevel = Priority.Trace
        l.fatal("TAG") { "Hello ${++count}" }

        assertThat(count).isEqualTo(1)

        // Fatal log cannot be masked
    }

    @Test fun `test fatal tag + message`() {
        l.fatal("TAG", "Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG f/Hello")
    }

    @Test fun `test fatal messaage`() {
        l.fatal("Hello")
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET f/Hello")
    }

    @Test fun `test fatal tag + lambda`() {
        l.fatal("TAG") { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/TAG f/Hello")
    }

    @Test fun `test fatal lambda`() {
        l.fatal { "Hello" }
        assertThat(logTesterRule.firstLine).isEqualTo("01-01 08:00:00.000  -/LET f/Hello")
    }
}