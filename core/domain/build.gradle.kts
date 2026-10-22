plugins {
    id("lastro.android.library")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.lastro.core.domain"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
