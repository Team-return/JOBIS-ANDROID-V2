package team.retum.signup.ui

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import team.retum.signup.R
import team.returm.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.returm.jobisdesignsystemv2.button.ButtonColor
import team.returm.jobisdesignsystemv2.button.JobisButton
import team.returm.jobisdesignsystemv2.foundation.JobisIcon
import team.returm.jobisdesignsystemv2.foundation.JobisTheme
import team.returm.jobisdesignsystemv2.foundation.JobisTypography

@Composable
fun SetProfile(
    onBackPressed: () -> Unit,
) {
    // TODO viewModel로 옮기기
    var uri: Uri? by remember { mutableStateOf(null) }
    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri = it },
    )

    SetProfileScreen(
        onBackPressed = onBackPressed,
        activityResultLauncher = activityResultLauncher,
        uri = uri,
    )
}

@Composable
private fun SetProfileScreen(
    onBackPressed: () -> Unit,
    activityResultLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    uri: Uri?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background)
            .padding(horizontal = 24.dp),
    ) {
        JobisLargeTopAppBar(
            title = stringResource(id = R.string.set_profile),
            onBackPressed = onBackPressed,
        )
        SetImage(
            uri = uri,
            onClick = {
                val mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                val request = PickVisualMediaRequest(mediaType)
                activityResultLauncher.launch(request)
            },
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisButton(
            modifier = Modifier.padding(bottom = 24.dp),
            text = stringResource(id = R.string.next),
            onClick = { },
            color = ButtonColor.Primary,
        )
    }
}

@Composable
private fun SetImage(
    uri: Uri?,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // TODO async image 사용
        Image(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            painter = painterResource(id = team.retum.common.R.drawable.ic_person),
            contentDescription = "user profile",
        )
        // TODO jobis medium button 구현하기
        SetImageButton(
            text = stringResource(id = R.string.edit_image),
            onClick = onClick,
        )
    }
}

@Composable
private fun SetImageButton(
    text: String,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.98f
        else 1f,
        label = "",
    )

    Row(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(12.dp))
            .background(JobisTheme.colors.primary)
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = rememberRipple(),
            )
            .padding(
                vertical = 8.dp,
                horizontal = 12.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_pencil),
            contentDescription = "pencil",
            tint = JobisTheme.colors.onPrimary,
        )
        Text(
            text = text,
            style = JobisTypography.SubHeadLine,
            color = JobisTheme.colors.onPrimary,
        )
    }
}
