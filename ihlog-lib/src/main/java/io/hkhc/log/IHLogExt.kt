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

@file:Suppress("TooManyFunctions")
@file:JvmName("Logger")
package io.hkhc.log

import io.hkhc.log.internal.LogFactory

val Any.l: IHLog
    get() {
        return LogFactory.getLog(this::class.java)
    }

/*
 We don't need to make tag parameter optional as there is always an alternative method
 without tag parameter.
  */

fun Any.debug(tag: String, message: String) = l.debug(tag, message)
fun Any.debug(message: String) = l.debug(message)
fun Any.debug(tag: String, lambda: () -> String) = l.debug(tag, lambda())
fun Any.debug(lambda: () -> String) = l.debug(lambda)

fun Any.info(tag: String, message: String) = l.info(tag, message)
fun Any.info(message: String) = l.info(message)
fun Any.info(tag: String, lambda: () -> String) = l.info(tag, lambda)
fun Any.info(lambda: () -> String) = l.info(lambda)

fun Any.warn(tag: String, message: String) = l.warn(tag, message)
fun Any.warn(message: String) = l.warn(message)
fun Any.warn(tag: String, lambda: () -> String) = l.warn(tag, lambda)
fun Any.warn(lambda: () -> String) = l.warn(lambda)

fun Any.trace(tag: String, message: String) = l.trace(tag, message)
fun Any.trace(message: String) = l.trace(message)
fun Any.trace(tag: String, lambda: () -> String) = l.trace(tag, lambda)
fun Any.trace(lambda: () -> String) = l.trace(lambda)

fun Any.fatal(tag: String, message: String) = l.fatal(tag, message)
fun Any.fatal(message: String) = l.fatal(message)
fun Any.fatal(tag: String, lambda: () -> String) = l.fatal(tag, lambda)
fun Any.fatal(lambda: () -> String) = l.fatal(lambda)

fun Any.err(tag: String, message: String) = l.err(tag, message)
fun Any.err(message: String) = l.err(message)
fun Any.err(tag: String, lambda: () -> String) = l.err(tag, lambda)
fun Any.err(lambda: () -> String) = l.err(lambda)

fun Throwable.log(tag: String?=null, message: String?=null)
        = l.exception(tag, message, this)

