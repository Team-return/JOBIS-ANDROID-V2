package team.retum.jobisdesignsystemv2.card

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import team.retum.jobisdesignsystemv2.foundation.JobisDesignSystemV2Theme
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable

/**
 * JOBIS에서 사용하는 Card 컴포넌트
 *
 * @param modifier [JobisCard]에서 지원할 Modifier
 * @param shape 모양 지정
 * @param background 배경 색상
 * @param enabled 활성화 여부
 * @param onClick 클릭했을 때 동작할 함수
 */
@Composable
fun JobisCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    background: Color = JobisTheme.colors.inverseSurface,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    var pressed by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(
        targetValue = if (pressed) {
            JobisTheme.colors.surfaceVariant
        } else {
            background
        },
        label = "",
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        onClick = onClick,
                        enabled = enabled,
                        onPressed = { pressed = it },
                    )
                } else {
                    Modifier
                },
            ),
        shape = shape,
        color = backgroundColor,
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun JobisCardPreview() {
    JobisDesignSystemV2Theme {
        JobisCard {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                JobisText(
                    text = "체험형 현장실습 ->",
                    style = JobisTypography.HeadLine,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(JobisTheme.colors.background)
                        .padding(12.dp)
                        .align(Alignment.End),
                ) {
                    Image(
                        modifier = Modifier.background(JobisTheme.colors.background),
                        painter = painterResource(id = JobisIcon.MeetingRoom),
                        contentDescription = "menu icon",
                    )
                }
            }
        }
    }
}

