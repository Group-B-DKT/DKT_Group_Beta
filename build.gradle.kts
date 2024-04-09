// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.sonarqube") version "4.4.1.3373"
    id("jacoco")
    alias(libs.plugins.androidApplication) apply false
}

sonar {
    properties {
        property("sonar.projectKey", "Group-B-DKT_DKT_Group_Beta")
        property("sonar.organization", "group-b-dkt")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.coverage.jacoco.xmlReportPaths", "${projectDir}/build/test-results/testDebugUnitTest")
        property("sonar.core.codeCoveragePlugin", "jacoco")
    }
}
jacoco {
    toolVersion = "0.8.7"
}
tasks.withType<JacocoReport> {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
