package team.retum.jobisdesignsystemv2.foundation

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import team.retum.design_system.R

private val PretendardFamily = FontFamily(
    listOf(
        Font(
            resId = R.font.pretendard_bold,
            weight = FontWeight.Bold,
        ),
        Font(
            resId = R.font.pretendard_medium,
            weight = FontWeight.Medium,
        ),
        Font(
            resId = R.font.pretendard_semi_bold,
            weight = FontWeight.SemiBold,
        ),
    ),
)

private val platFormTextStyle = PlatformTextStyle(
    includeFontPadding = false,
)

/**
 * JobisTypography defines fontFamily, fontSize, fontWeight, lineHeight and platformStyle in TextStyle.
 */
object JobisTypography {
    val PageTitle
        @Composable get() = TextStyle(
            fontFamily = PretendardFamily,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 36.sp,
            platformStyle = platFormTextStyle,
            color = JobisTheme.colors.onBackground,
        )

    val HeadLine
        @Composable get() = TextStyle(
            fontFamily = PretendardFamily,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 28.sp,
            platformStyle = platFormTextStyle,
            color = JobisTheme.colors.onBackground,
        )

    val SubHeadLine
        @Composable get() = TextStyle(
            fontFamily = PretendardFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 24.sp,
            platformStyle = platFormTextStyle,
            color = JobisTheme.colors.onBackground,
        )

    val Body
        @Composable get() = TextStyle(
            fontFamily = PretendardFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 24.sp,
            platformStyle = platFormTextStyle,
            color = JobisTheme.colors.onBackground,
        )

    val SubBody
        @Composable get() = TextStyle(
            fontFamily = PretendardFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 20.sp,
            platformStyle = platFormTextStyle,
            color = JobisTheme.colors.onBackground,
        )

    val Description
        @Composable get() = TextStyle(
            fontFamily = PretendardFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 20.sp,
            platformStyle = platFormTextStyle,
            color = JobisTheme.colors.onBackground,
        )

    val Caption
        @Composable get() = TextStyle(
            fontFamily = PretendardFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 16.sp,
            platformStyle = platFormTextStyle,
            color = JobisTheme.colors.onBackground,
        )
}
