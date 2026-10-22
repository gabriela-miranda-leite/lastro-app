import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jlleitschuh.gradle.ktlint.KtlintExtension

class LastroKtlintPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jlleitschuh.gradle.ktlint")
            extensions.configure<KtlintExtension> {
                version.set("1.3.1")
                android.set(true)
                outputToConsole.set(true)
                filter {
                    exclude("**/generated/**")
                    include("**/*.kt")
                }
            }
        }
    }
}
