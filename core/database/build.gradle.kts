plugins {
    id("lastro.android.library")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.lastro.core.database"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
