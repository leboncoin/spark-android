# Releasing

## Pre-release

> [!NOTE]
> This process will apply for the versions starting 1.4.0.

Before each release, we will publish one or more alpha versions and publish pre-releases on [Github Release page](https://github.com/leboncoin/spark-android/releases).

Consumers will be able to test new features and report breaking changes & bugs that can be fixed before it’s considered for a stable release.

Using the Github pre-release feature will allow Spark users to be notified that a new release is being prepared and test against it.
For example, they could set up hooks to post the changelog in their monitoring Slack channel or trigger a CI build to validate that this new release doesn’t break their build.

1. [Create a draft release](https://github.com/leboncoin/spark-android/releases/new?tag=X.Y.Z-alpha01&title=X.Y.Z-alpha01&prerelease=1) with a `*.*.*-alpha01` version tag.
2. Click on `Generate release notes` to automatically add all the merged pull requests from this diff and contributors of this release.
3. Remove logs from `@dependabot` except if they mention big version upgrades for libraries used by our consumers (like Compose or Kotlin). 
4. Reformat the changelog to be as close as possible to the format we describe in the [CHANGELOG STYLE GUIDE](./docs/CHANGELOG%20STYLE%20GUIDE.md).
5. If we’re satisfied with the draft, release it but make sure **`⚠️ Set as a pre-release`** is checked.
6. If we need to create a fix from feedbacks, then this cycle repeats.
7. Otherwise, follow the [stable release process](./RELEASING.md#Release)

## Release

1. Update the `version` in [gradle.properties](gradle.properties) to a non-`alpha`.
2. Update [CHANGELOG.md](CHANGELOG.md)
   - Add the new version section and move the *unreleased changes* into it.
   - Update the links at the end of the page.
3. Commit and push the changes to a new PR
   ```bash
   git commit -am "chore: prepare version X.Y.X"
   git push
   ```
4. Once the PR is merged, tag the release on the `main` branch
   ```bash
   git fetch
   git tag X.Y.Z origin/main
   git push origin X.Y.Z
   ```
5. Wait for the [publishing workflow](https://github.com/leboncoin/spark-android/actions/workflows/publish.yml) to build and publish the release.
6. Update the `version` in [gradle.properties](gradle.properties) to the next `alpha` version.
7. Commit and push the changes to a new PR
  ```bash
  git commit -am "chore: prepare next development version"
  ```
8. Trigger the manual workflow [![📋 Publish Dokka to GitHub Pages](https://github.com/leboncoin/spark-android/actions/workflows/dokka.yml/badge.svg)](https://github.com/leboncoin/spark-android/actions/workflows/dokka.yml) with the version tag.
9. Draft a [new release](https://github.com/leboncoin/spark-android/releases/new) with the version tag, add the corresponding [CHANGELOG.md](CHANGELOG.md) entries, and publish it when ready.

---

## Hotfix

Hotfixes can sometimes be a bit tricky to do right. Please follow these steps carefully:

1. Create the hotfix remote branch (the `hotfix` prefix is important)
   ```bash
   git branch hotfix/X.Y.Z+1 refs/tags/X.Y.Z
   git push origin hotfix/X.Y.Z+1
   ```
2. Create a local hotfix PR branch
   ```bash
   git switch --create patch-hotfix-X.Y.Z+1 refs/tags/X.Y.Z
   ```
3. Commit the necessary changes and open a PR targeting the hotfix branch.
4. Once the PR is merged, you can continue with the regular release process described above.
5. Finally, merge these changes back to the main branch with a new PR
   ```bash
   git merge --no-ff refs/tags/X.Y.Z+1
   ```
