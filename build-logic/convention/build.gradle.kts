plugins {
    `kotlin-dsl`
}

group = "com.lastro.buildlogic"

kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.ktlint.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApp") {
            id = "lastro.android.app"
            implementationClass = "LastroAndroidAppPlugin"
        }
        register("androidLibrary") {
            id = "lastro.android.library"
            implementationClass = "LastroAndroidLibraryPlugin"
        }
        register("androidFeature") {
            id = "lastro.android.feature"
            implementationClass = "LastroAndroidFeaturePlugin"
        }
        register("androidCompose") {
            id = "lastro.android.compose"
            implementationClass = "LastroComposePlugin"
        }
        register("ktlint") {
            id = "lastro.ktlint"
            implementationClass = "LastroKtlintPlugin"
        }
    }
}
