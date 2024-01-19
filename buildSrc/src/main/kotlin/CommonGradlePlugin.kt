import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project

class CommonGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        setProjectConfig(project)
    }

    private fun setProjectConfig(project: Project) {
        project.android().apply {
            compileSdk = ProjectProperties.COMPILE_SDK

            defaultConfig {
                minSdk = ProjectProperties.MIN_SDK
                testInstrumentationRunner = "androidx.test.runner.AndroidJunitRunner"
                consumerProguardFiles("consumer-rules.pro")
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_18
                targetCompatibility = JavaVersion.VERSION_18
            }
        }
    }

    private fun Project.android(): LibraryExtension {
        return extensions.getByType(LibraryExtension::class.java)
    }
}
