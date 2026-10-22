plugins {
    id("lastro.android.library")
    id("lastro.android.compose")
}

android {
    namespace = "com.lastro.core.ui"
}

dependencies {
    implementation(project(":core:common"))
}
