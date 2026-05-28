# Releasing

Releases are automated via [release-please](https://github.com/googleapis/release-please). Manual tagging, changelog editing, and version bumps are no longer required.

## How It Works

Every push to `main` triggers `.github/workflows/release.yml`, which runs release-please. It opens (or updates) a **Release PR** that:

- Bumps the version in `gradle.properties`
- Updates `CHANGELOG.md` based on conventional commits since the last release

Merging the Release PR creates a git tag and GitHub Release. That tag then triggers:

- `.github/workflows/publish.yml` — publishes to Maven Central
- `.github/workflows/firebase-app-distribution.yml` — distributes the catalog APK to Firebase and attaches it to the GitHub Release

Conventional commit prefixes control the version bump: `feat:` bumps minor, `fix:` bumps patch, `feat!:` / `fix!:` (breaking change) bumps major. Commits without a recognised prefix are ignored for versioning.

## Icon Updates

Icon changes are automated via `.github/workflows/pr-icon-updates.yml`, which opens a PR titled `feat(icons): update icons`. By default this is treated as a minor bump (new icons added).

If the update contains a breaking change (icon removal, rename, or visual change to an existing icon), the reviewer must edit the PR title before merging to signal a breaking change:

```
feat(icons)!: update icons
```

The `!` suffix tells release-please this is a breaking change and triggers a major version bump.

## Hotfix Workflow

1. Create the hotfix branch from the release tag:
   ```bash
   git branch hotfix/X.Y.Z+1 refs/tags/X.Y.Z
   git push origin hotfix/X.Y.Z+1
   ```
2. Create a working branch, commit fixes, and open a PR targeting `hotfix/X.Y.Z+1`:
   ```bash
   git switch --create fix-hotfix-X.Y.Z+1 hotfix/X.Y.Z+1
   ```
3. This creates a Release PR targeting the hotfix branch.
4. Merge the Release PR. The tag and publish workflows fire automatically.
5. Open a PR to merge `hotfix/X.Y.Z+1` back into `main`.
