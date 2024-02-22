package team.retum.jobisdesignsystemv2.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import team.retum.jobisdesignsystemv2.foundation.JobisColor

data class ButtonColorSet(
    val background: Color,
    val pressed: Color,
    val text: Color,
)

enum class ButtonColor {
    Primary,
    Secondary,
    Default,
    Error,
}

internal object ButtonColors {
    object Light {
        @Composable
        fun primary() = ButtonColorSet(
            background = JobisColor.Light.primary200,
            pressed = JobisColor.Light.primary400,
            text = JobisColor.Light.gray100,
        )

        @Composable
        fun secondary() = ButtonColorSet(
            background = JobisColor.Light.primary100,
            pressed = JobisColor.Light.gray400,
            text = JobisColor.Light.primary200,
        )

        @Composable
        fun default() = ButtonColorSet(
            background = JobisColor.Light.gray300,
            pressed = JobisColor.Light.gray400,
            text = JobisColor.Light.gray900,
        )

        @Composable
        fun error() = ButtonColorSet(
            background = JobisColor.Light.red200,
            pressed = JobisColor.Light.red200,
            text = JobisColor.Light.gray100,
        )
    }

    object Dark {
        @Composable
        fun primary() = ButtonColorSet(
            background = JobisColor.Dark.primary200,
            pressed = JobisColor.Dark.primary200,
            text = JobisColor.Dark.gray100,
        )

        @Composable
        fun secondary() = ButtonColorSet(
            background = JobisColor.Dark.primary100,
            pressed = JobisColor.Dark.gray300,
            text = JobisColor.Dark.primary200,
        )

        @Composable
        fun default() = ButtonColorSet(
            background = JobisColor.Light.gray300,
            pressed = JobisColor.Light.gray300,
            text = JobisColor.Dark.green100,
        )

        @Composable
        fun error() = ButtonColorSet(
            background = JobisColor.Light.red200,
            pressed = JobisColor.Light.red200,
            text = JobisColor.Light.gray100,
        )
    }
}
