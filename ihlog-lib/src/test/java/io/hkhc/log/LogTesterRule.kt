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
import io.hkhc.log.providers.StringLogProvider
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.StringWriter

class LogTesterRule : TestRule {

    lateinit var writer: StringWriter

    val firstLine: String
        get() = writer.toString().trim()

    class LogTesterStatement(var writer: StringWriter, var base: Statement?) : Statement() {
        override fun evaluate() {
            LogFactory.defaultProvider = StringLogProvider(writer, MockTimeSource(0))
            LogFactory.logLevel = Priority.Trace
            base?.evaluate()
        }
    }

    override fun apply(base: Statement?, description: Description?): Statement {
        writer = StringWriter()
        return LogTesterStatement(writer, base)
    }
}