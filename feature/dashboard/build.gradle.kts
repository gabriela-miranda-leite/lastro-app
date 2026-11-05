plugins {
    id("lastro.android.feature")
    id("app.cash.paparazzi")
}

android {
    namespace = "com.lastro.feature.dashboard"
}

dependencies {
    implementation(libs.androidx.compose.material.icons.extended)
    testImplementation(libs.junit)
}
