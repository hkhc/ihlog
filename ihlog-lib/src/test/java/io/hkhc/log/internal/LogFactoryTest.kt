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

package io.hkhc.log.internal

import io.hkhc.log.MockTimeSource
import io.hkhc.log.Priority
import io.hkhc.log.providers.NullLogProvider
import io.hkhc.log.providers.PrintWriterLogProvider
import io.hkhc.log.providers.PriorityLog
import io.hkhc.log.providers.SimpleMetaTag
import io.hkhc.log.providers.StringLogProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.io.StringWriter

class LogFactoryTest {

    @Before
    fun setUp() {
        LogFactory.defaultProvider = NullLogProvider()
        TagMaker.metaTagPolicy = SimpleMetaTag("WWW")
    }

    @Test
    fun `create tag`() {
        // given
        TagMaker.metaTagPolicy = SimpleMetaTag("")
        // then
        assertThat(LogFactory.createTag(LogFactoryTest::class.java)).isEqualTo("LFT")
        // when
        TagMaker.metaTagPolicy = SimpleMetaTag("TEST")
        assertThat(LogFactory.createTag(LogFactoryTest::class.java)).isEqualTo("TEST_LFT")
    }

    @Test
    fun `log meta tag`() {

        // given
        TagMaker.metaTagPolicy = SimpleMetaTag("TEST")

        // when
        val log1 = LogFactory.getLog(LogFactoryTest::class.java)

        // then
        assertThat(log1.getLogTag()).isEqualTo("TEST_LFT")
    }

    @Test
    fun `log meta tag change is active immediately`() {

        // given
        TagMaker.metaTagPolicy = SimpleMetaTag("TEST")

        // when
        var log1 = LogFactory.getLog(LogFactoryTest::class.java)

        // then
        assertThat(log1.getLogTag()).isEqualTo("TEST_LFT")

        // when
        TagMaker.metaTagPolicy = SimpleMetaTag("APPLE")
        log1 = LogFactory.getLog(LogFactoryTest::class.java)

        // then
        // The metatag is not changed after the first getLog()
        assertThat(log1.getLogTag()).isEqualTo("TEST_LFT")
    }

    @Test
    fun `log is cached`() {

        // given
        val log1 = LogFactory.getLog(LogFactoryTest::class.java)
        val log2 = LogFactory.getLog(LogFactoryTest::class.java)

        // then
        assertThat(log1).isSameAs(log2)
    }

    @Test
    fun `log with different tag`() {

        // given
        val log1 = LogFactory.getLog(LogFactoryTest::class.java)
        val log2 = LogFactory.getLog(TagMakerTest::class.java)

        // then
        assertThat(log1).isNotSameAs(log2)
    }

    @Test
    fun `change default provider`() {

        // given
        val writer = StringWriter()
        LogFactory.defaultProvider = StringLogProvider(writer, MockTimeSource(0))
        LogFactory.logLevel = Priority.Trace
        var log1 = LogFactory.getLog(LogFactoryTest::class.java)

        // then
        var actualProvider = (log1 as PriorityLog).delegate
        assertThat(actualProvider).isInstanceOf(PrintWriterLogProvider.PrintWriterLog::class.java)

        // when
        LogFactory.defaultProvider = NullLogProvider()
        log1 = LogFactory.getLog(TagMakerTest::class.java)

        // then
        actualProvider = (log1 as PriorityLog).delegate
        assertThat(actualProvider).isInstanceOf(NullLogProvider.NullIHLog::class.java)
    }
}