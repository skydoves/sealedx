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
import com.skydoves.sealedx.core.annotations.ExtensiveModel
import com.skydoves.sealedx.core.annotations.ExtensiveSealed
import com.skydoves.sealedx.processor.model.ExtensiveModelBag
import com.skydoves.sealedx.processor.model.modelName
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec

/**
 * @author skydoves (Jaewoong Eum)
 * @since 1.0.0
 *
 * The primary file generator for the sealed classes and interfaces respectively that are annotated
 * with the [ExtensiveSealed] annotation.
 *
 * @property declaration The annotated declaration with the [ExtensiveSealed] annotation.
 * @property extensiveModelBag The compile-time information from the [ExtensiveModel] annotation.
 */
internal class ExtensiveFileGenerator(
  private val declaration: KSClassDeclaration,
  private val extensiveModelBag: ExtensiveModelBag
) {

  fun generate(): FileSpec {
    val packageName = declaration.packageName.asString()
    val name = extensiveModelBag.modelName
    val fileName = "$name${declaration.simpleName.asString()}"
    val className = ClassName.bestGuess("$packageName.$fileName")

    val extensiveSealedClassSpec = ExtensiveSealedClassGenerator(
      declaration = declaration,
      className = className,
      extensiveModelBag = extensiveModelBag
    ).generate()

    return FileSpec.builder(
      packageName = packageName,
      fileName = fileName
    )
      .addFileComment("This class was generated by sealedx (https://github.com/skydoves/sealedx).\n")
      .addFileComment("Do not modify this class.")
      .addType(extensiveSealedClassSpec)
      .build()
  }
}