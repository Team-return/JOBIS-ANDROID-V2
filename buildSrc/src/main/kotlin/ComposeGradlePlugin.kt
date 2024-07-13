import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

/**
 * compose 사용 모듈의 공통적인 gradle 설정 및 의존성 선언 모음
 *
 */
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
            implementation(project(":core:design-system"))
            implementation(project(":core:common"))
            implementation(project(":core:domain"))

            implementation(libs.findLibrary("androidx.compose.foundation").get())
            implementation(libs.findLibrary("androidx.compose.material3").get())
            implementation(libs.findLibrary("androidx.lifecycle.runtime.compose").get())
            implementation(libs.findLibrary("hilt.navigation").get())
            implementation(libs.findLibrary("androidx.navigation.compose").get())

            androidTestImplementation(libs.findLibrary("androidx.compose.ui.test").get())
            androidTestImplementation(libs.findLibrary("hilt.android.testing").get())
            kaptAndroidTest(libs.findLibrary("hilt-android-compiler").get())
        }
    }
}
