import org.gradle.api.Plugin
import org.gradle.api.Project

class LastroKtlintPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply("org.jlleitschuh.gradle.ktlint")
    }
}
