plugins {
    id("lastro.android.app")
    id("lastro.android.compose")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.lastro.app"
    defaultConfig {
        applicationId = "com.lastro.app"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.navigation.compose)
    implementation(project(":core:common"))

    testImplementation(libs.junit)

    // Features
    implementation(project(":feature:auth"))
    implementation(project(":feature:dashboard"))
    implementation(project(":feature:transactions"))
    implementation(project(":feature:couple"))
    implementation(project(":feature:profile"))
}
