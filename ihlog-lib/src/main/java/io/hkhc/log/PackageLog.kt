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
import kotlin.reflect.KProperty

class PackageLog {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): IHLog {

        // if the delegate is invoked from package file, we use
        // StackTrace to find the class object.
        // The classname first element of stack trace is this PackageLog class
        // so we take the next one.

        val ref = thisRef
            ?.let {it::class.java}
            ?: Class.forName(Throwable().stackTrace[1].className)
        return LogFactory.getLog(ref)
    }

}