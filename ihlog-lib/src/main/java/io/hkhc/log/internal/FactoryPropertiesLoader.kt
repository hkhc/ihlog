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

package io.hkhc.log.internal

import io.hkhc.log.IHLogProvider
import java.io.IOException
import java.util.Properties

class FactoryPropertiesLoader {

    private val PROPERTY_PATH = "/ihlog.properties"
    private val PROVIDER_KEY = "provider"

    fun getStreamFromResource(path: String = PROPERTY_PATH) =
        this::class.java.getResourceAsStream(path)

    fun getProviderClassName(path: String = PROPERTY_PATH): String? {

        val prop = Properties()
        var result: String? = null
        @Suppress("EmptyCatchBlock")
        getStreamFromResource(path)?.let {
            try {
                prop.load(it)
                result = prop.getProperty(PROVIDER_KEY)
            } catch (e: IOException) { }
        }
        return result
    }

    fun loadProvider(path: String = PROPERTY_PATH): IHLogProvider? {
        val prop = Properties()
        getStreamFromResource(path)?.let {
            prop.load(it)
            val providerClassName = prop.getProperty(PROVIDER_KEY)
            providerClassName?.let {
                return Class.forName(providerClassName).getDeclaredConstructor().newInstance() as IHLogProvider
            }
        }
        return null
    }
}
