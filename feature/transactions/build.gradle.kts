plugins {
    id("lastro.android.feature")
}

android {
    namespace = "com.lastro.feature.transactions"
}

dependencies {
    implementation(project(":core:ui"))

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
