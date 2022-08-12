/*
 * Designed and developed by 2022 skydoves (Jaewoong Eum)
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
 */

package com.skydoves.sealedx.core.annotations

import kotlin.reflect.KClass

/**
 * This contains information on extensive models like which model type and the name of the class.
 * Generally, the simple name of the [type] will be the model name, but you can overwrite by giving
 * the [name] value manually. If you give the [name] value manually, make sure that the model name will be
 * capitalized.
 */
@MustBeDocumented
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
public annotation class ExtensiveModel(
  val type: KClass<*>,
  val name: String = ""
) {

  public companion object {
    /** The name of the `type` parameter. */
    public const val PARAM_TYPE: String = "type"

    /** The name of the `name` parameter. */
    public const val PARAM_NAME: String = "name"
  }
}
