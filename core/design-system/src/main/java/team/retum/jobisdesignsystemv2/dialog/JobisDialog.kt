package team.retum.jobisdesignsystemv2.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisDialogButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography

@Composable
fun JobisDialog(
    onDismissRequest: () -> Unit,
    title: String,
    description: String,
    subButtonText: String = "",
    mainButtonText: String,
    subButtonColor: ButtonColor = ButtonColor.Default,
    mainButtonColor: ButtonColor = ButtonColor.Error,
    onSubButtonClick: () -> Unit = { },
    onMainButtonClick: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(JobisTheme.colors.background)
                .padding(vertical = 8.dp),
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 12.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = title,
                    style = JobisTypography.HeadLine,
                )
                Text(
                    text = description,
                    style = JobisTypography.Body,
                )
            }
            Row(
                modifier = Modifier.padding(
                    vertical = 12.dp,
                    horizontal = 20.dp,
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (subButtonText.isNotEmpty()) {
                    JobisDialogButton(
                        modifier = Modifier.weight(1f),
                        text = subButtonText,
                        color = subButtonColor,
                        onClick = onSubButtonClick,
                    )
                }
                JobisDialogButton(
                    modifier = Modifier.weight(1f),
                    text = mainButtonText,
                    color = mainButtonColor,
                    onClick = onMainButtonClick,
                )
            }
        }
    }
}
