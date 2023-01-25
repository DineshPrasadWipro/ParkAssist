# Contributing

| Project    | Link                                                                               |
| ---------- | ---------------------------------------------------------------------------------- |
| homepage   | <https://gitlabee.dt.renault.com/partners/loire/hmi/apps/renault/ParkAssist>     |
| repository | <https://gitlabee.dt.renault.com/partners/loire/hmi/apps/renault/ParkAssist.git> |
| bugs       | <https://jira.dt.renault.com/secure/CreateIssue!default.jspa>                      |

## Bugs

Open a `Bug` ticket on the
[Alliance JIRA](https://jira.dt.renault.com/secure/CreateIssue!default.jspa)
with following Attributes

| Attribute   | Value            |
| ----------- | ---------------- |
| Project     | CCS EXTERNAL     |
| Issue Type  | Bug              |
| Component/s | Loire HMI Triage |

## Repo setup

> requires Android SDK and Node LTS. see
> [Contribution Guide](https://partners.gitlab-pages.dt.renault.com/loire/hmi/devops/devops-guide/guide/Contributing.html)
> for more.

```bash
# setup dev environment
$ npm i
# check environement
$ ./gradlew tasks
```

## Build & Test

```bash
$ ./gradlew build
$ ./gradlew check
```

### ViewModels Update

ViewModel specifications are located in `app/vm-specification`

To generate sources and documentation:

```bash
$ npm run docs:midl
$ npm run codegen:midl
```

## Coding Standard

### Java

Follow AOSP standard:

- <https://source.android.com/setup/contribute/code-style>

### Kotlin

Follow Android Developpers guidelines:

- https://developer.android.com/kotlin/style-guide
- https://developer.android.com/kotlin/common-patterns
- https://developer.android.com/kotlin/interop

And then the Kotlin standard

- https://kotlinlang.org/docs/reference/coding-conventions.html

### Check and auto-format code style

```bash
$ ./gradlew ktlint
$ ./gradlew ktlintFormat
```

## Commit Message

The `Commit Message` shall follow the
[Conventional Commits](https://www.conventionalcommits.org) specification.

```bash
<type>: <description>

[optional body]

[optional footer]
```

1. `fix`: a commit of the type fix patches a bug in your codebase (this
   correlates with PATCH in semantic versioning).

1. `feat`: a commit of the type feat introduces a new feature to the codebase
   (this correlates with MINOR in semantic versioning).

1. `BREAKING CHANGE`: a commit that has the text BREAKING CHANGE: at the
   beginning of its optional body or footer section introduces a breaking API
   change (correlating with MAJOR in semantic versioning). A BREAKING CHANGE can
   be part of commits of any type.

commit types other than fix: and feat: recommended types are chore, docs, test.
More @ <https://www.conventionalcommits.org>

> Use the [standard-commit](https://www.npmjs.com/package/standard-commit) CLI
> to assist with the commit message convention

## Merge Request

- Contributions are done on `feature` or `fix` or `int` branches
- [Merge Requests](https://partners.gitlab-pages.dt.renault.com/loire/hmi/devops/devops-guide/guide/Contributing.html#merge-request)
  shall be created for the `master` branch

Recommended branch naming:

| Naming                     | Purpose                      | Sample                   |
| -------------------------- | ---------------------------- | ------------------------ |
| `fix/<short-description>`  | Official bug fix development | fix/menu-button-xxx      |
| `feat/<short-description>` | Official feature development | feat/users-filtering-xxx |
| `int/<short-description>`  | Official integration branch  | int/sprint-3.4-xxx       |

> Recommended : add the JIRA ID as a suffix in the branch name.