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

package com.skydoves.sealedx.processor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier
import com.skydoves.sealedx.core.annotations.ExtensiveSealed

/**
 * @author skydoves (Jaewoong Eum)
 * @since 1.0.0
 *
 * Validate [KSClassDeclaration] that is annotated with [ExtensiveSealed] is a sealed class/interface.
 *
 * @param logger A [KSPLogger] to log processor status.
 */
internal fun KSClassDeclaration.validateModifierContainsSealed(logger: KSPLogger): Boolean {
  if (!modifiers.contains(Modifier.SEALED)) {
    logger.error(
      "${ExtensiveSealed::class.simpleName} can't be attached to ${classKind.type}. " +
        "You can only attach to the sealed class or sealed interface "
    )
    return false
  }
  return true
}

public fun KSPLogger.anothers() {
}
