plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.kover)
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

dependencies {
    kover(project(":core:common"))
    kover(project(":core:domain"))
    kover(project(":core:network"))
    kover(project(":feature:auth"))
    kover(project(":feature:transactions"))
}

kover {
    reports {
        variant("debug") {
            filters {
                excludes {
                    classes(
                        "*.di.*",
                        "*.BuildConfig",
                        "*_Factory",
                        "*_HiltModules*",
                        "*.ComposableSingletons*",
                        "Hilt_*",
                        "*Module_*",
                    )
                }
            }
        }
    }
}