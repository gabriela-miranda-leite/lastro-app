import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class LastroAndroidFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("lastro.android.library")
                apply("lastro.android.compose")
                apply("com.google.dagger.hilt.android")
                apply("com.google.devtools.ksp")
            }
            dependencies {
                add("implementation", project(":core:common"))
                add("implementation", project(":core:domain"))
                add("implementation", project(":core:ui"))
                add("implementation", libs.findLibrary("hilt.android").get())
                add("ksp", libs.findLibrary("hilt.compiler").get())
                add("implementation", libs.findLibrary("hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewmodel.compose").get())
                add("implementation", libs.findLibrary("androidx.navigation.compose").get())
            }
        }
    }
}
