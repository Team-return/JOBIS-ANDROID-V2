import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CommonGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        setProjectConfig(project)
        setDependencies(project)
    }

    private fun setProjectConfig(project: Project) {
        project.android().apply {
            compileSdk = ProjectProperties.COMPILE_SDK

            defaultConfig {
                minSdk = ProjectProperties.MIN_SDK
                testInstrumentationRunner = "team.retum.common.HiltTestRunner"
                consumerProguardFiles("consumer-rules.pro")
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_18
                targetCompatibility = JavaVersion.VERSION_18
            }
        }
    }

    private fun setDependencies(project: Project) {
        val libs = project.libs
        project.dependencies {
            implementation(libs.findLibrary("hilt.android").get())
            kapt(libs.findLibrary(("hilt.android.compiler")).get())
            testImplementation(libs.findLibrary("junit").get())
            androidTestImplementation(libs.findLibrary("androidx.junit").get())
            androidTestImplementation(libs.findLibrary("androidx.espresso.core").get())
            implementation(libs.findLibrary("hilt-android-testing").get())
            implementation(libs.findLibrary("androidx.test.runner").get())
        }
    }
}
