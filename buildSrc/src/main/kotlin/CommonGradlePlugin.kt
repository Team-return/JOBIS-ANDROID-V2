import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * 각 모듈에서 사용되는 공통적인 gradle 설정 및 의존성 선언 모음
 */
class CommonGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        setProjectConfig(project)
        setDependencies(project)
    }

    /**
     * 모듈의 컴파일 옵션을 설정한다.
     *
     * @param project
     *
     * 다음과 같은 작업을 수행한다.
     * - 모듈 이름 설정
     * - compileSdk 및 minSdk 버전 설정
     * - testRunner 설정
     *     - 모듈의 테스트 코드에서 hilt 사용이 필요한 경우 HiltTestRunner를 사용한다.
     *     - hilt 사용이 필요하지 않은 경우 AndroidJunitRunner를 사용한다.
     */
    private fun setProjectConfig(project: Project) {
        val moduleName = project.projectDir.absolutePath.split("/").last()
        project.android().apply {
            compileSdk = ProjectProperties.COMPILE_SDK

            defaultConfig {
                minSdk = ProjectProperties.MIN_SDK
                if(moduleName != "design-system") {
                    testInstrumentationRunner = "team.retum.common.HiltTestRunner"
                }
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
            androidTestImplementation(libs.findLibrary("junit").get())
            androidTestImplementation(libs.findLibrary("androidx.junit").get())
            androidTestImplementation(libs.findLibrary("androidx.espresso.core").get())
            implementation(libs.findLibrary("hilt-android-testing").get())
            implementation(libs.findLibrary("androidx.test.runner").get())
        }
    }
}
