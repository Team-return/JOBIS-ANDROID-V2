package team.retum.jobis.interests.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import team.retum.jobis.interests.R

@Composable
fun InterestsComplete(
    onBackPressed: () -> Unit,
) {
    InterestsCompleteScreen(
        onBackPressed = onBackPressed,
    )
}

@Composable
fun InterestsCompleteScreen(
    onBackPressed: () -> Unit,
) {
    Image(
        painter = painterResource(id = team.retum.design_system.R.drawable.success),
        contentDescription = null,
    )
}
