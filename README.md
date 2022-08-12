<h1 align="center">SealedX</h1></br>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/skydoves/sealedx/actions/workflows/android.yml"><img alt="Build Status" 
  src="https://github.com/skydoves/sealedx/actions/workflows/android.yml/badge.svg"/></a>
  <a href="https://github.com/skydoves"><img alt="Profile" src="https://skydoves.github.io/badges/skydoves.svg"/></a>
</p><br>

<p align="center">
🎲 Kotlin Symbol Processor to auto-generate extensive sealed classes and interfaces for Android and Kotlin.<br><br>
</p>

## Why SealedX?

SealedX generates extensive sealed classes & interfaces based on common sealed classes for each different model. You can reduce writing repeated sealed classes for every different model by auto-generating based on KSP ([Kotlin Symbol Processor](https://kotlinlang.org/docs/ksp-overview.html)).<br><br>
You can massively reduce writing repeated files such as `_UiState` sealed interfaces if your project is based on MVI architecture.

<p align="center">
<img src="https://user-images.githubusercontent.com/24237865/184303312-9df53e1e-9ec2-448c-9d93-3a7265ada7ec.png" width="760"/>
</p>

## Use Cases
You can see some use cases on GitHub repositories below:

- WhatsApp Clone Compose: [Introducing SealedX KSP library](https://github.com/GetStream/whatsApp-clone-compose/pull/1).

## Gradle Setup

To use [KSP (Kotlin Symbol Processing)](https://kotlinlang.org/docs/ksp-quickstart.html) and SealedX library in your project, you need to follow steps below.

### 1. Enable KSP in your module

Add the KSP plugin below into your **module**'s `build.gradle` file:

<details open>
  <summary>Kotlin (KTS)</summary>

```kotlin
plugins {
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
}
```
</details>

<details>
  <summary>Groovy</summary>

```kotlin
plugins {
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
}
```
</details>

> **Note**: Make sure your current Kotlin version and [KSP version](https://github.com/google/ksp/releases) is the same.

### 2. Add SealedX dependencies

[![Maven Central](https://img.shields.io/maven-central/v/com.github.skydoves/sealedx-core.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.skydoves%22%20AND%20a:%22sealedx-core%22)

Add the dependency below into your **module**'s `build.gradle` file:

```gradle
dependencies {
    implementation("com.github.skydoves:sealedx-core:1.0.0")
    ksp("com.github.skydoves:sealedx-processor:1.0.0")
}
```

### 3. Add KSP source path

To access generated codes from KSP, you need to set up the source path like the below into your **module**'s `build.gradle` file:

<details open>
  <summary>Android Kotlin (KTS)</summary>

```kotlin
kotlin {
  sourceSets.configureEach {
    kotlin.srcDir("$buildDir/generated/ksp/$name/kotlin/")
  }
}
```
</details>

<details>
  <summary>Android Groovy</summary>

```gradle
android {
    applicationVariants.all { variant ->
        kotlin.sourceSets {
            def name = variant.name
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}
```
</details>

<details>
  <summary>Pure Kotlin (KTS)</summary>

```gradle
kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}
```
</details>

<details>
  <summary>Pure Kotlin Groovy</summary>

```gradle
kotlin {
    sourceSets {
        main.kotlin.srcDirs += 'build/generated/ksp/main/kotlin'
        test.kotlin.srcDirs += 'build/generated/ksp/test/kotlin'
    }
}
```
</details>

## Usage

### ExtensiveSealed

`@ExtensiveSealed` annotation is the main trigger of the Kotlin Symbol Processor to run a sealed-extensive processor on compile time. 
- `@ExtensiveSealed` must be annotated to sealed classes or interfaces, which should be a common model to generate extensive sealed classes and interfaces. 
- `@ExtensiveSealed` receives an array of `@ExtensiveModel` annotations, which include the extensive model types. 
- If you build your project, extensive sealed classes or interfaces will be generated based on those extensive models.

Let's see a common `UiState` sealed interface below that is annotated with `@ExtensiveSealed` annotation:

```kotlin
@ExtensiveSealed(
  models = [
    ExtensiveModel(Poster::class),
    ExtensiveModel(PosterDetails::class)
  ]
)
sealed interface UiState {
  data class Success(val data: Extensive) : UiState
  object Loading : UiState
  object Error : UiState
}
```

The example codes above will generate `PosterUiState` and `PosterDetailsUiState` sealed interfaces below: <br>

**PosterUiState (generated)**:

```kotlin
public sealed interface PosterUiState {
  public object Error : PosterUiState

  public object Loading : PosterUiState

  public data class Success(
    public val `data`: Poster,
  ) : PosterUiState
}
```

**PosterDetailsUiState (generated)**:

```kotlin
public sealed interface PosterDetailsUiState {
  public object Error : PosterDetailsUiState

  public object Loading : PosterDetailsUiState

  public data class Success(
    public val `data`: PosterDetails,
  ) : PosterDetailsUiState
}
```

<details>
  <summary>See further sealed class examples</summary>

In the case of the sealed classes, it's not different fundamentally from sealed interface examples.

```kotlin
@ExtensiveSealed(
  models = [ ExtensiveModel(type = Poster::class) ]
)
sealed class UiState {
  data class Success(val data: Extensive) : UiState()
  object Loading : UiState()
  object Error : UiState()
}
```

The example codes above will generate the `PosterUiState` sealed class below: <br>

**PosterUiState (generated)**:

```kotlin
public sealed class PosterUiState {
  public object Error : PosterUiState()

  public object Loading : PosterUiState()

  public data class Success(
    public val `data`: Poster,
  ) : PosterUiState()
}
```
</details>

### ExtensiveModel

`@ExtensiveModel` annotation class contains information on extensive models like model type and a custom name, which decides the name of generated classes.
Basically, (the simple name of the `type`) + (the name of common sealed classes) will be used to name of generated classes, but you can modify the prefix with the `name` parameter like the example below:

```kotlin
@ExtensiveSealed(
  models = [ ExtensiveModel(type = PosterExtensive::class, name = "Movie") ]
)
sealed interface UiState {
  data class Success(val data: Extensive) : UiState
  object Loading : UiState
  object Error : UiState
}
```

The example codes above will generate `MovieUiState` file instead of `PosterExtensiveUiState` like the below:

**MovieUiState (generated)**:

```kotlin
public sealed interface MovieUiState {
  public object Error : MovieUiState

  public object Loading : MovieUiState

  public data class Success(
    public val `data`: PosterExtensive,
  ) : MovieUiState
}
```

#### Collection type in @ExtensiveModel

Basically, you can't set a collection type like a `List` to the `type` parameter of the `@ExtensiveModel` annotation.
So if you need to set a collection type as an extensive model, you need to write a wrapper class like the below:

```kotlin
data class PosterExtensive(
  val posters: List<Poster>
)

@ExtensiveSealed(
  models = [ ExtensiveModel(type = PosterExtensive::class) ]
)
sealed interface UiState {
    ..
}
```

### Extensive

`Extensive` is an interface that is used to represent extensive model types of sealed classes and interfaces. 
When you need to use an extensive model type in your primary constructor of data class, you can use the `Extensive` extensive type on your common sealed classes and interfaces. 
It will be replaced by the extensive model type on compile time.

```kotlin
@ExtensiveSealed(
  models = [ ExtensiveModel(type = PosterExtensive::class) ]
)
sealed interface UiState {
  // You need to use the Extensive type if you want to use an extensive model type in the generated code.
  data class Success(val data: Extensive) : UiState
  ..
}
```

The example codes above will generate `PosterExtensiveUiState` sealed interface like the below:

**PosterExtensiveUiState (generated)**:

```kotlin
public sealed interface PosterExtensiveUiState {
  public object Error : PosterExtensiveUiState

  public object Loading : PosterExtensiveUiState

  public data class Success(
    public val `data`: PosterExtensive,
  ) : PosterExtensiveUiState
}
```

As you can see from the example above, the `Extensive` interface type will be replaced with the extensive model by the SealedX processor on compile time.

## Find this repository useful? :heart:
Support it by joining __[stargazers](https://github.com/skydoves/sealedx/stargazers)__ for this repository. :star: <br>
Also, __[follow me](https://github.com/skydoves)__ on GitHub for my next creations! 🤩

# License
```xml
Designed and developed by 2022 skydoves (Jaewoong Eum)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
