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
import com.skydoves.sealedx.processor.ksp.overridePrimaryConstructor
import com.skydoves.sealedx.processor.ksp.superClassName
import com.skydoves.sealedx.processor.model.ExtensiveModelBag
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName

/**
 * @author skydoves (Jaewoong Eum)
 * @since 1.0.0
 *
 * A sealed sub-classes and interfaces specifications generator for each [parent]'s sub-class.
 *
 * @property parent The annotated declaration with the [ExtensiveSealed] annotation.
 * @property className The full-package name of the target class.
 * @property extensiveModelBag The compile-time information from the [ExtensiveModel] annotation.
 */
internal class SealedSubClassGenerator(
  private val parent: KSClassDeclaration,
  private val className: ClassName,
  private val extensiveModelBag: ExtensiveModelBag
) {

  private val sealedClasses: List<KSClassDeclaration> = parent.getSealedSubclasses().toList()

  fun generate(): List<TypeSpec> {
    val typeSpecs = mutableListOf<TypeSpec>()
    sealedClasses.forEach {
      val sealedSpec = when (it.classKind) {
        ClassKind.CLASS -> generateSealedClassTypeSpec(it)
        ClassKind.INTERFACE -> generateSealedInterfaceTypeSpec(it)
        ClassKind.OBJECT -> generateSealedObjectTypeSpec(it)
        else -> error("The ${it.simpleName} class must be one of sealed class/interface or object class.")
      }
      typeSpecs.add(sealedSpec)
    }
    return typeSpecs
  }

  private fun generateSealedClassTypeSpec(declaration: KSClassDeclaration): TypeSpec {
    val propertyGenerator =
      ExtensiveConstructorPropertyGenerator(
        declaration = declaration,
        extensiveModelBag = extensiveModelBag
      )
    return TypeSpec.classBuilder(declaration.toClassName())
      .overridePrimaryConstructor(propertyGenerator.generate())
      .addExtensiveSpecs(declaration)
      .build()
  }

  private fun generateSealedInterfaceTypeSpec(declaration: KSClassDeclaration): TypeSpec {
    return TypeSpec.interfaceBuilder(declaration.toClassName())
      .addExtensiveSpecs(declaration)
      .build()
  }

  private fun generateSealedObjectTypeSpec(declaration: KSClassDeclaration): TypeSpec {
    return TypeSpec.objectBuilder(declaration.toClassName())
      .addExtensiveSpecs(declaration)
      .build()
  }

  private fun TypeSpec.Builder.addExtensiveSpecs(declaration: KSClassDeclaration): TypeSpec.Builder =
    apply {
      overrideAnnotations(declaration)
      overrideModifiers(declaration)
      superClassName(parent, className)
    }
}
