plugins {
    alias(libs.plugins.androidApplication)
    jacoco
    id("org.sonarqube") version "4.4.1.3373"
}

android {
    namespace = "com.example.dkt_group_beta"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dkt_group_beta"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}


sonar {
    properties {
        property("sonar.projectKey", "Group-B-DKT_DKT_Group_Beta")
        property("sonar.organization", "group-b-dkt")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.java.coveragePlugin", "jacoco")
        property("sonar.coverage.jacoco.xmlReportPaths", "${project.projectDir}/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation ("androidx.security:security-crypto:1.1.0-alpha06")
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    testImplementation("org.mockito:mockito-core:3.11.2")
    testImplementation("org.robolectric:robolectric:4.7.3")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation ("androidx.test:core:1.5.0")
    compileOnly(libs.lombok)

}


tasks.withType<JacocoReport> {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}