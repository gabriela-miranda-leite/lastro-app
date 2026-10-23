plugins {
    id("lastro.android.feature")
}

android {
    namespace = "com.lastro.feature.auth"
}

dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(project(":core:testing"))
}
