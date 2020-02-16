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
import java.io.File

class FileExtTest {

    @Rule @JvmField
    val rule = LogTesterRule()

    @Test
    fun `test 1 line file`() {

        val file = File("test-data/1line.txt")
        file.debug()

        assertThat(rule.writer.toString()).isEqualTo(
            """
                01-01 08:00:00.000  -/FET d/Hello This is a test

            """.trimIndent()
        )
    }

    @Test
    fun `test 2 line file`() {

        val file = File("test-data/2line.txt")
        file.debug()

        assertThat(rule.writer.toString()).isEqualTo(
            """
                01-01 08:00:00.000  -/FET d/Hello
                01-01 08:00:00.001  -/FET d/World

            """.trimIndent()
        )
    }

    @Test
    fun `test over 500 byte file`() {

        val file = File("test-data/over512b.txt")
        file.debug(maxSize = 500)

        assertThat(rule.writer.toString()).isEqualTo(
            """
                01-01 08:00:00.000  -/FET d/01234567890123456789012345678901234567890123456789
                01-01 08:00:00.001  -/FET d/01234567890123456789012345678901234567890123456789
                01-01 08:00:00.002  -/FET d/01234567890123456789012345678901234567890123456789
                01-01 08:00:00.003  -/FET d/01234567890123456789012345678901234567890123456789
                01-01 08:00:00.004  -/FET d/01234567890123456789012345678901234567890123456789
                01-01 08:00:00.005  -/FET d/01234567890123456789012345678901234567890123456789
                01-01 08:00:00.006  -/FET d/01234567890123456789012345678901234567890123456789
                01-01 08:00:00.007  -/FET d/01234567890123456789012345678901234567890123456789
                01-01 08:00:00.008  -/FET d/01234567890123456789012345678901234567890123456789
                01-01 08:00:00.009  -/FET d/01234567890123456789012345678901234567890123456789

            """.trimIndent()
        )
    }
}
