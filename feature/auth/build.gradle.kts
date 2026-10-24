plugins {
    id("lastro.android.feature")
    id("app.cash.paparazzi")
}

android {
    namespace = "com.lastro.feature.auth"
}

dependencies {
    implementation(project(":core:network"))
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(project(":core:testing"))
}
