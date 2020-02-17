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

package io.hkhc.gradle.allpublish

import org.gradle.api.component.SoftwareComponent
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.TaskProvider
import javax.inject.Inject

open class AllPublishExtension @Inject constructor(objectFactory: ObjectFactory) {

    init {
        System.out.println("AllPublishPublish new extension ${objectFactory}")
    }

    // TODO make use of provider API for values

    var documentTask = objectFactory.property(TaskProvider::class.java)

    var sourceSet = objectFactory.property(Any::class.java)

    var publicationName = objectFactory.property(String::class.java)

    var publishComponent = objectFactory.property(SoftwareComponent::class.java)

    var label = objectFactory.property(String::class.java)

    override fun toString():String {
        return "${super.toString()} / ${documentTask?.get()} / ${sourceSet.get()} / ${publicationName.get()} /"+
        " ${publishComponent.get()} / ${label.get()}"
    }

}