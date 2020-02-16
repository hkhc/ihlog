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

sealed class Severity(val value: Int) {

    fun shouldBeFilteredBy(p: Severity) =
        p.value > value

    object Trace : Severity(1)
    object Debug : Severity(2)
    object Info : Severity(3)
    object Warn : Severity(4)
    object Error : Severity(5)
    object Fatal : Severity(6)
}
