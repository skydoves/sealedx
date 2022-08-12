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

package com.skydoves.sealedx.core

import com.skydoves.sealedx.core.annotations.ExtensiveModel
import com.skydoves.sealedx.core.annotations.ExtensiveSealed

/**
 * An interface that can be used to represent extensive types of the sealed classes and interfaces
 * that are annotated with [ExtensiveSealed].
 *
 * This interface type will be replaced by the [ExtensiveModel.type] in generated codes on compile time.
 * [Extensive] can be used to represent the data type for the primary constructor in the data class.
 *
 * ```
 * @ExtensiveSealed(
 *   models = [
 *     ExtensiveModel(type = PokemonExtensive::class)
 *   ]
 * )
 * sealed interface UIState {
 *   data class Success(val data: Extensive) : UIState
 *   object Loading : UIState
 *   object Error : UIState
 * }
 * ```
 *
 * The [Extensive] in the `Success` data class will be replaced with `PokemonExtensive` in the new
 * generated class.
 *
 * ```
 * data class Success(val data: PokemonExtensive) : PokemonExtensiveUIState
 * ```
 */
public interface Extensive
