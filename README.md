# Spark Android Design System

<p align="center">
<picture>
    <source media="(prefers-color-scheme: dark)" srcset="art/spark-logo-dark.svg">
    <img alt="Spark Design System logo" src="art/spark-logo-light.svg">
  </picture>
</p>

[![👷 Build → 🧑‍🔬 Test → 🕵️ Lint](https://github.com/leboncoin/spark-android/actions/workflows/ci.yml/badge.svg)](https://github.com/leboncoin/spark-android/actions/workflows/ci.yml)

Spark Design System is based on Material 3 Compose artifact described
on the [official documentation](https://material.io/) and maintained by Google developers
and designers.

But these native components and tokens are overridden to respect Spark's Visual Identity. You'll
find
the design specifications and technical information for supported platforms by Adevinta on
[spark.adevinta.com](https://spark.adevinta.com).

The demo app is not currently available, but we plan to publish it in the near future.

## 🚀 Getting Started

A `SparkTheme` is available from where you can get all
colors, typographies and shapes in your composable hierarchy. Note that this theme is
mandatory if you want to use any Spark composable.
Otherwise, a runtime error will be thrown.

```kotlin
SparkTheme {
    // Your composable declarations
}
```

## Installation

Add the main Spark dependency: [![Maven Central](https://img.shields.io/maven-central/v/com.adevinta.spark/spark-bom?label=%20&color=success)](https://central.sonatype.com/namespace/com.adevinta.spark)

```kotlin
dependencies {
    // Import the Spark BoM
    implementation(platform("com.adevinta.spark:spark-bom:<version>"))

    // Declare dependencies without versions
    implementation("com.adevinta.spark:spark")
    implementation("com.adevinta.spark:spark-icons")
}
```

## Contributing

Please take a look at the [contribution guide](docs/CONTRIBUTING.md) to setup your dev environment and get a list of common tasks used in this project, as well as the [Code of conduct](docs/CODE_OF_CONDUCT.md).

## License

    Copyright (c) 2023 Adevinta
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
