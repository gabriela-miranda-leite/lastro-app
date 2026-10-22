plugins {
    id("lastro.android.library")
}

android {
    namespace = "com.lastro.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}
