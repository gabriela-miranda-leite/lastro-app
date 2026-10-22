import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class LastroComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
            extensions.findByType(CommonExtension::class.java)?.apply {
                buildFeatures.compose = true
            }
            dependencies {
                val bom = platform(libs.findLibrary("androidx.compose.bom").get())
                "implementation"(bom)
                "androidTestImplementation"(bom)
                "implementation"(libs.findLibrary("androidx.compose.ui").get())
                "implementation"(libs.findLibrary("androidx.compose.ui.graphics").get())
                "implementation"(libs.findLibrary("androidx.compose.ui.tooling.preview").get())
                "implementation"(libs.findLibrary("androidx.compose.material3").get())
                "debugImplementation"(libs.findLibrary("androidx.compose.ui.tooling").get())
                "debugImplementation"(libs.findLibrary("androidx.compose.ui.test.manifest").get())
            }
        }
    }
}
