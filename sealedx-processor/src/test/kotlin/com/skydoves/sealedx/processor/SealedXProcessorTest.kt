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

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.SourceFile.Companion.kotlin
import com.tschuchort.compiletesting.kspWithCompilation
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

internal class SealedXProcessorTest {

  @Rule
  @JvmField
  val temporaryFolder: TemporaryFolder = TemporaryFolder()

  @Test
  fun `should compile success for sealed extensive class`() {
    val sealedXSource = """
                    package sealedx

                    import com.skydoves.sealedx.core.Extensive
                    import com.skydoves.sealedx.core.annotations.ExtensiveModel
                    import com.skydoves.sealedx.core.annotations.ExtensiveSealed

                    @ExtensiveSealed(
                      models = [
                        ExtensiveModel(type = String::class),
                        ExtensiveModel(type = Int::class),
                      ]
                    )
                    sealed class UIState {
                      data class Success(val name: Extensive) : UIState()
                      object Loading : UIState()
                      object Error : UIState()
                    }
    """.trimIndent()

    val kotlinSource = kotlin(name = "UIState.kt", contents = sealedXSource)
    val result = compile(kotlinSource)
    assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
  }

  @Test
  fun `should compile success for sealed extensive interface`() {
    val sealedXSource = """
                    package sealedx

                    import com.skydoves.sealedx.core.Extensive
                    import com.skydoves.sealedx.core.annotations.ExtensiveModel
                    import com.skydoves.sealedx.core.annotations.ExtensiveSealed

                    @ExtensiveSealed(
                      models = [
                        ExtensiveModel(type = String::class),
                        ExtensiveModel(type = Int::class),
                      ]
                    )
                    sealed interface UIState {
                      data class Success(val name: Extensive) : UIState
                      object Loading : UIState
                      object Error : UIState
                    }
    """.trimIndent()

    val kotlinSource = kotlin(name = "UIState.kt", contents = sealedXSource)
    val result = compile(kotlinSource)
    assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
  }

  private fun prepareCompilation(vararg sourceFiles: SourceFile): KotlinCompilation =
    KotlinCompilation()
      .apply {
        workingDir = temporaryFolder.root
        inheritClassPath = true
        symbolProcessorProviders = listOf(SealedXSymbolProcessorProvider())
        sources = sourceFiles.asList()
        verbose = false
        kspWithCompilation = true
      }

  private fun compile(vararg sourceFiles: SourceFile): KotlinCompilation.Result =
    prepareCompilation(*sourceFiles).compile()
}
