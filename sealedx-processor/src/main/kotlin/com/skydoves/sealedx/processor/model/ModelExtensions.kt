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

package com.skydoves.sealedx.processor.model

import com.skydoves.sealedx.processor.ksp.name
import java.util.Locale

/**
 * @author skydoves (Jaewoong Eum)
 * @since 1.0.0
 *
 * Returns a model name of a declaration from the [ExtensiveModelBag].
 * If the bag includes valid [name] value, this property returns [name].
 * If the [name] value is not valid, this property returns name of the type.
 */
internal inline val ExtensiveModelBag.modelName: String
  get() = (name.ifEmpty { type.name }).replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(
      Locale.ROOT
    ) else it.toString()
  }
