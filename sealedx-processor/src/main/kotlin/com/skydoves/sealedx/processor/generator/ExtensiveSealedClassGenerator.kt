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

package com.skydoves.sealedx.processor.generator

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.skydoves.sealedx.core.annotations.ExtensiveModel
import com.skydoves.sealedx.core.annotations.ExtensiveSealed
import com.skydoves.sealedx.processor.ksp.overrideAnnotations
import com.skydoves.sealedx.processor.ksp.overrideModifiers
import com.skydoves.sealedx.processor.model.ExtensiveModelBag
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName

/**
 * @author skydoves (Jaewoong Eum)
 * @since 1.0.0
 *
 * A sealed class and interface specifications generator for each [declaration].
 *
 * @property declaration The annotated declaration with the [ExtensiveSealed] annotation.
 * @property className The full-package name of the target class.
 * @property extensiveModelBag The compile-time information from the [ExtensiveModel] annotation.
 */
internal class ExtensiveSealedClassGenerator(
  private val declaration: KSClassDeclaration,
  private val className: ClassName,
  private val extensiveModelBag: ExtensiveModelBag
) {

  fun generate(): TypeSpec {
    return when (declaration.classKind) {
      ClassKind.CLASS -> {
        val subClassGenerator = SealedSubClassGenerator(
          parent = declaration,
          className = className,
          extensiveModelBag = extensiveModelBag
        )
        buildExtensiveSealedClassTypeSpec(subClassGenerator)
      }
      ClassKind.INTERFACE -> {
        val subClassGenerator = SealedSubClassGenerator(
          parent = declaration,
          className = className,
          extensiveModelBag = extensiveModelBag
        )
        buildExtensiveSealedInterfaceTypeSpec(subClassGenerator)
      }
      else -> error("The class annotated with `ExtensiveSealed` must be a sealed class or interface.")
    }
  }

  private fun buildExtensiveSealedClassTypeSpec(
    subClassGenerator: SealedSubClassGenerator
  ): TypeSpec {
    return TypeSpec.classBuilder(className)
      .addExtensiveSpecs(subClassGenerator)
      .build()
  }

  private fun buildExtensiveSealedInterfaceTypeSpec(
    subClassGenerator: SealedSubClassGenerator
  ): TypeSpec {
    return TypeSpec.interfaceBuilder(className)
      .addExtensiveSpecs(subClassGenerator)
      .build()
  }

  private fun TypeSpec.Builder.addExtensiveSpecs(
    subClassGenerator: SealedSubClassGenerator
  ): TypeSpec.Builder = apply {
    overrideAnnotations(declaration)
    overrideModifiers(declaration)
    addTypes(subClassGenerator.generate())
    addKdoc("An extensive sealed ${declaration.classKind.type} by [${declaration.toClassName()}].")
  }
}
