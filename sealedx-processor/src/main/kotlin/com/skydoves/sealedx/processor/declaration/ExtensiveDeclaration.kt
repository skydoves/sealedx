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

package com.skydoves.sealedx.processor.declaration

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.impl.binary.KSAnnotationDescriptorImpl
import com.skydoves.sealedx.core.annotations.ExtensiveModel
import com.skydoves.sealedx.core.annotations.ExtensiveSealed
import com.skydoves.sealedx.processor.model.ExtensiveModelBag

/**
 * @author skydoves (Jaewoong Eum)
 * @since 1.0.0
 *
 * A declaration of the [ExtensiveSealed] annotated sealed classes/interfaces.
 * This contains the [KSClassDeclaration] of the sealed classes/interfaces that are annotated
 * with [ExtensiveSealed] and the compile-time value of the arguments.
 *
 * This class also extracts the values of arguments and put them together into the list of [ExtensiveModelBag]
 * that could be used in generators.
 *
 * @property declaration The sealed classes/interfaces declaration that are annotated with [ExtensiveSealed].
 */
internal class ExtensiveDeclaration(
  val declaration: KSClassDeclaration
) {

  val models: List<ExtensiveModelBag>

  init {
    // Extracts `models` from the `ExtensiveSealed` annotation.
    // TODO: replace with getAnnotationsByType method; https://github.com/google/ksp/issues/888
    val arguments = declaration.annotations.first {
      it.annotationType.resolve().declaration.qualifiedName?.asString() == ExtensiveSealed::class.qualifiedName
    }.arguments.first { it.name?.asString() == ExtensiveSealed.PARAM_MODELS }.value

    // Extract a list of KSType from the class type of the array of `ExtensiveModel`.
    val modelsKSTypesDescriptor =
      (arguments as ArrayList<*>).map { it as KSAnnotation }
    models = modelsKSTypesDescriptor.map { kSAnnotationDescriptor ->
      val name = kSAnnotationDescriptor.arguments.first { KSValueArgument ->
        KSValueArgument.name?.asString() == ExtensiveModel.PARAM_NAME
      }.value as String

      val type = kSAnnotationDescriptor.arguments.first { KSValueArgument ->
        KSValueArgument.name?.asString() == ExtensiveModel.PARAM_TYPE
      }.value as KSType

      ExtensiveModelBag(name = name, type = type)
    }.distinct()
  }
}
