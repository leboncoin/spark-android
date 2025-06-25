# Contributing

### Issues, Bugs, Feature or Component Requests

While we currently don't accept issues from people outside the Adevinta Platform to this repo, we
welcome [discussions and questions here](https://github.com/leboncoin/spark-android/discussions/categories/general)!

## If you're part of the Adevinta Platform ⬇️

### Requirements

#### [git-lfs](https://git-lfs.com/)

This repository uses git-lfs to handle [cashapp/paparazzi](https://github.com/cashapp/paparazzi)
screenshot testing.  
If you introduce visible changes, you'll likely have to update screenshots files for the tests to
pass.

```bash
# Install
brew install git-lfs
# Check
git lfs install
```

#### [Java 17](https://github.com/leboncoin/spark-android/issues/74)

We currently use **AGP 8.1.0** which requires developers to use **JDK 17** on Gradle JDK.

_If you're on macOS, you can install it with [brew](https://formulae.brew.sh/formula/openjdk@17)_

```bash
brew install openjdk@17
```

> ℹ️ If you're using a device with [Apple silicon (M1/M2)](https://support.apple.com/en-us/HT211814) then you might need to install
> a [zulu distribution](https://www.azul.com/downloads/zulu-community/?version=java-17-lts&architecture=x86-64-bit&package=jdk)

or install it directly from **Android Studio**:  
`File` → `Settings` → `Build, Execution, Deployment` → `Build Tools` → `Gradle` → `Gradle JDK`

## Gradle tasks

Here is a list of Gradle tasks commonly used in this project:

- Code formatting: run spotless formatter
  ```bash
  ./gradlew spotlessApply
  ```
- Building: assemble all modules in release mode
  ```bash
  ./gradlew assembleRelease
  ```
- Testing: run all unit tests and screenshot tests
  ```bash
  ./gradlew globalCiUnitTest verifyPaparazziRelease
  ```
- Screenshot testing: record golden images
  ```bash
  ./gradlew cleanRecordPaparazziRelease
  ```
- Binary Compatibility Validation: update the reference ABI
  ```bash
  ./gradlew updateLegacyAbi
  ```
- Linting: run Lint analysis
  ```bash
  ./gradlew lintRelease
  ```
- Deploying: publish all Maven publications to the local Maven cache `~/.m2`.
  ```bash
  ./gradlew publishToMavenLocal
  ```
- Dokka: generate Dokka documentation [website](/build/dokka/index.html)
  ```bash
  ./gradlew dokkaHtmlMultiModule --no-configuration-cache
  python3 -m http.server --directory build/dokka
  # Open http://[::]:8000
  ```

## Pull Requests

The best way to make an impact is by creating code submissions called pull requests. Pull requests
should be ‘solutions’ to GitHub issues.

To make a pull request:

1. Make sure there’s a GitHub issue for the change you’re proposing.
2. [Fork](https://github.com/leboncoin/spark-android/fork) the repo for the platform your code works
   in.
3. Write code in your fork, on a branch if you plan to make multiple changes.
4. [Create a pull request](https://help.github.com/articles/creating-a-pull-request/) to merge your
   branch’s contributions into the corresponding Spark repo by following the .
5. The pull request will be triaged and reviewed by a **[leboncoin/android](https://github.com/orgs/leboncoin/teams/android)** team member.
6. If the pull request is accepted, the accepting member will merge the pull request for you.

### What if I already made a pull request but want to add more commits?

If you correctly created a new branch for your changes, you can simply upload the new commits to
your fork and they will automatically appear in the PR.

## Code review

Regardless of language or platform, all code goes through code review before it can be merged into
main branches.

We use a set of tools to ensure that the code quality stays high and that the code is consistent
across the codebase.

So you might see :

- Some comments on your review from bots like automatic highlighting of lint warnings/errors
  introduced.
- We're using screenshot testing to ensure that the UI is not broken by changes. If you introduce
  visible changes, you'll likely have to update screenshots files for the test to pass.
- We're using commit convention for the PR title you can find more
  information [here](https://www.conventionalcommits.org/en/v1.0.0/).
