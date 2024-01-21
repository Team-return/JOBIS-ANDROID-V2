import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class ComposeGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        setProjectConfig(project)
        setDependencies(project)
    }

    private fun setProjectConfig(project: Project) {
        project.android().apply {
            buildFeatures {
                compose = true
            }
            composeOptions {
                kotlinCompilerExtensionVersion = ProjectProperties.COMPOSE_COMPILER_EXTENSION
            }
        }
    }

    private fun setDependencies(project: Project) {
        val libs = project.libs
        project.dependencies {
            implementation(libs.findLibrary("androidx.activity.compose").get())
            implementation(libs.findLibrary("androidx.navigation.compose").get())
            implementation(libs.findLibrary("androidx.core.ktx").get())
            implementation(libs.findLibrary("appcompat").get())
            implementation(libs.findLibrary("material").get())
        }
    }
}
