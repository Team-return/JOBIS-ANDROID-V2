package team.retum.jobisdesignsystemv2.foundation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

private val lightColorScheme = lightColorScheme(
    primary = JobisColor.Light.primary100,
    onPrimary = JobisColor.Light.primary200,
    primaryContainer = JobisColor.Light.primary300,
    onPrimaryContainer = JobisColor.Light.primary400,
    onError = JobisColor.Light.red100,
    error = JobisColor.Light.red200,
    onSecondary = JobisColor.Light.blue100,
    secondary = JobisColor.Light.blue200,
    onTertiary = JobisColor.Light.yellow100,
    tertiary = JobisColor.Light.yellow200,
    outline = JobisColor.Light.green100,
    outlineVariant = JobisColor.Light.green200,
    background = JobisColor.Light.gray100,
    surface = JobisColor.Light.gray200,
    inverseSurface = JobisColor.Light.gray300,
    surfaceVariant = JobisColor.Light.gray400,
    surfaceTint = JobisColor.Light.gray500,
    onSurfaceVariant = JobisColor.Light.gray600,
    inverseOnSurface = JobisColor.Light.gray700,
    onSurface = JobisColor.Light.gray800,
    onBackground = JobisColor.Light.gray900,
)

private val darkColorScheme = darkColorScheme(
    primary = JobisColor.Dark.primary100,
    onPrimary = JobisColor.Dark.primary200,
    primaryContainer = JobisColor.Dark.primary300,
    onPrimaryContainer = JobisColor.Dark.primary400,
    onError = JobisColor.Dark.red100,
    error = JobisColor.Dark.red200,
    onSecondary = JobisColor.Dark.blue100,
    secondary = JobisColor.Dark.blue200,
    onTertiary = JobisColor.Dark.yellow100,
    tertiary = JobisColor.Dark.yellow200,
    outline = JobisColor.Dark.green100,
    outlineVariant = JobisColor.Dark.green200,
    background = JobisColor.Dark.gray100,
    surface = JobisColor.Dark.gray200,
    inverseSurface = JobisColor.Dark.gray300,
    surfaceVariant = JobisColor.Dark.gray400,
    surfaceTint = JobisColor.Dark.gray500,
    onSurfaceVariant = JobisColor.Dark.gray600,
    inverseOnSurface = JobisColor.Dark.gray700,
    onSurface = JobisColor.Dark.gray800,
    onBackground = JobisColor.Dark.gray900,
)

@Composable
fun JobisDesignSystemV2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        darkColorScheme
    } else {
        lightColorScheme
    }

    CompositionLocalProvider(LocalColors provides colors) {
        content()
    }
}

object JobisTheme {
    val colors
        @Composable get() = LocalColors.current
}

val LocalColors = staticCompositionLocalOf { lightColorScheme() }
