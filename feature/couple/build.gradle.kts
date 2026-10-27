plugins {
    id("lastro.android.feature")
    id("app.cash.paparazzi")
}

android {
    namespace = "com.lastro.feature.couple"
}

dependencies {
    implementation(libs.androidx.compose.material.icons.extended)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(project(":core:testing"))
}
