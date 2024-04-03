// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.sonarqube") version "4.4.1.3373"

    alias(libs.plugins.androidApplication) apply false
}

sonar {
    properties {
        property("sonar.projectKey", "Group-B-DKT_DKT_Group_Beta")
        property("sonar.organization", "group-b-dkt")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}