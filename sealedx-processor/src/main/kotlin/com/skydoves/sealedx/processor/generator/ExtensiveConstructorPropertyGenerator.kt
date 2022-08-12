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

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.skydoves.sealedx.core.Extensive
import com.skydoves.sealedx.core.annotations.ExtensiveModel
import com.skydoves.sealedx.core.annotations.ExtensiveSealed
import com.skydoves.sealedx.processor.model.ConstructorProperty
import com.skydoves.sealedx.processor.model.ExtensiveModelBag
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toAnnotationSpec
import com.squareup.kotlinpoet.ksp.toTypeName

/**
 * @author skydoves (Jaewoong Eum)
 * @since 1.0.0
 *
 * A primary constructor of sealed sub-classes and interfaces specifications generator.
 *
 * @property declaration The annotated declaration with the [ExtensiveSealed] annotation.
 * @property extensiveModelBag The compile-time information from the [ExtensiveModel] annotation.
 */
internal class ExtensiveConstructorPropertyGenerator(
  private val declaration: KSClassDeclaration,
  private val extensiveModelBag: ExtensiveModelBag
) {

  fun generate(): List<ConstructorProperty> {
    val constructorProperties: MutableList<ConstructorProperty> = mutableListOf()
    val constructorParameters = declaration.primaryConstructor?.parameters
    constructorParameters?.forEach { parameter ->
      val parameterSpec = ParameterSpec.builder(
        name = parameter.name?.asString() ?: let {
          val index = constructorParameters.indexOf(parameter).takeIf { it != -1 } ?: 0
          "param$index"
        },
        type = parameter.extensiveTypeName
      ).build()

      val property = PropertySpec.builder(parameterSpec.name, parameterSpec.type)
        .addAnnotations(parameter.annotations.map { it.toAnnotationSpec() }.toList())
        .initializer("%N", parameterSpec)
        .mutable(parameter.isVar)
        .build()

      val constructorProperty = ConstructorProperty(parameterSpec, property)
      constructorProperties.add(constructorProperty)
    }
    return constructorProperties
  }

  private inline val KSValueParameter.extensiveTypeName: TypeName
    get() = if (type.resolve().declaration.qualifiedName?.asString() == Extensive::class.qualifiedName) {
      extensiveModelBag.type.toTypeName()
    } else {
      type.toTypeName()
    }
}
