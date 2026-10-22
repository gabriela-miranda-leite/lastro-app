plugins {
    id("lastro.android.library")
}

android {
    namespace = "com.lastro.core.domain"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.android)
}
