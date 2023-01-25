@file:Suppress("UnstableApiUsage")

plugins {
    id("com.gladed.androidgitversion")
}

androidGitVersion {
    prefix = "v"
    hideBranches = listOf("master", "release.*", "stable.*")
}
