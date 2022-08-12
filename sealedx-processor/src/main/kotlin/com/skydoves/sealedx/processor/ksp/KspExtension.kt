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

package com.skydoves.sealedx.processor.ksp

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.skydoves.sealedx.core.annotations.ExtensiveSealed
import com.skydoves.sealedx.processor.model.ConstructorProperty
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toAnnotationSpec
import com.squareup.kotlinpoet.ksp.toKModifier

/**
 * @author skydoves (Jaewoong Eum)
 * @since 1.0.0
 *
 * Returns the simple string name of the [KSType].
 */
internal inline val KSType.name: String
  get() = declaration.simpleName.asString()

/**
 * @author skydoves (Jaewoong Eum)
 * @since 1.0.0
 *
 * Override entire annotations from a [declaration] to the [TypeSpec.Builder].
 *
 * @param declaration The target [KSClassDeclaration] that provides annotations.
 */
internal fun TypeSpec.Builder.overrideAnnotations(declaration: KSClassDeclaration): TypeSpec.Builder =
  apply {
    declaration.annotations
      .filter { it.shortName.asString() != ExtensiveSealed::class.simpleName }
      .forEach { addAnnotation(it.toAnnotationSpec()) }
  }

/**
 * @author skydoves (Jaewoong Eum)
 * @since 1.0.0
 *
 * Override entire modifiers from a [declaration] to the [TypeSpec.Builder].
 *
 * @param declaration The target [KSClassDeclaration] that provides modifiers.
 */
internal fun TypeSpec.Builder.overrideModifiers(declaration: KSClassDeclaration): TypeSpec.Builder =
  apply {
    addModifiers(declaration.modifiers.mapNotNull { it.toKModifier() })
  }

/**
 * @author skydoves (Jaewoong Eum)
 * @since 1.0.0
 *
 * Override the primary constructor from a list of [ConstructorProperty] to the [TypeSpec.Builder].
 *
 * @param propertyList The a list of [ConstructorProperty] that provide constructor specs.
 */
internal fun TypeSpec.Builder.overridePrimaryConstructor(
  propertyList: List<ConstructorProperty>
): TypeSpec.Builder = apply {
  primaryConstructor(
    FunSpec.constructorBuilder()
      .addParameters(propertyList.map { it.parameterSpec })
      .build()
  )
  addProperties(propertyList.map { it.propertySpec })
}

/**
 * @author skydoves (Jaewoong Eum)
 * @since 1.0.0
 *
 * Super class or interface by the given [ClassName] depending on the [ClassKind] of the [declaration].
 *
 * @param declaration The target [KSClassDeclaration] that provides super class specs.
 * @param className The full package information to super class or interface.
 */
internal fun TypeSpec.Builder.superClassName(
  declaration: KSClassDeclaration,
  className: ClassName
): TypeSpec.Builder =
  apply {
    val classKind = declaration.classKind
    if (classKind == ClassKind.CLASS) {
      superclass(className)
    } else if (classKind == ClassKind.INTERFACE) {
      addSuperinterface(className)
    }
  }
