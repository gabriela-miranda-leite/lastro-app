plugins {
    id("lastro.android.app")
    id("lastro.android.compose")
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
}
