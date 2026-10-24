plugins {
    id("lastro.android.feature")
}

android {
    namespace = "com.lastro.feature.auth"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                if (project.hasProperty("roborazzi.record")) {
                    it.systemProperty("roborazzi.test.record", "true")
                }
            }
        }
    }
}

dependencies {
    implementation(project(":core:network"))
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
    testImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(project(":core:testing"))
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
