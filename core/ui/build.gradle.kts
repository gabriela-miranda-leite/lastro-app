plugins {
    id("lastro.android.library")
    id("lastro.android.compose")
}

android {
    namespace = "com.lastro.core.ui"
}

dependencies {
    implementation(project(":core:common"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
