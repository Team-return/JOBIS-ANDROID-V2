import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

internal fun Project.android(): LibraryExtension {
    return extensions.getByType(LibraryExtension::class.java)
}

internal val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun DependencyHandler.implementation(dependencyNotation: Provider<MinimalExternalModuleDependency>) {
    add("implementation", dependencyNotation)
}

internal fun DependencyHandler.testImplementation(dependencyNotation: Provider<MinimalExternalModuleDependency>) {
    add("testImplementation", dependencyNotation)
}

internal fun DependencyHandler.androidTestImplementation(dependencyNotation: Provider<MinimalExternalModuleDependency>) {
    add("androidTestImplementation", dependencyNotation)
}

internal fun DependencyHandler.kapt(dependencyNotation: Provider<MinimalExternalModuleDependency>) {
    add("kapt", dependencyNotation)
}
