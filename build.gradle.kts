plugins {
    id("org.sonarqube") version "4.4.1.3373"
}

sonar {
    properties {
        property("sonar.projectKey", "Group-B-DKT_DKT_Group_Beta")
        property("sonar.organization", "group-b-dkt")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}