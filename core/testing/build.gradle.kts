plugins {
    id("lastro.android.library")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.lastro.core.testing"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.kotlinx.coroutines.android)
}
