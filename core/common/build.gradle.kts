plugins {
    id("lastro.android.library")
}

android {
    namespace = "com.lastro.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    api(libs.androidx.navigation.compose)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
