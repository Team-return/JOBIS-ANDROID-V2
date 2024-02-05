package team.retum.signup.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import team.retum.common.enums.Gender
import team.retum.signup.R
import team.returm.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.returm.jobisdesignsystemv2.button.ButtonColor
import team.returm.jobisdesignsystemv2.button.JobisButton
import team.returm.jobisdesignsystemv2.foundation.JobisTheme
import team.returm.jobisdesignsystemv2.foundation.JobisTypography

@Composable
fun SelectGender(onBackPressed: () -> Unit) {
    SelectGenderScreen(onBackPressed = onBackPressed)
}

@Composable
private fun SelectGenderScreen(
    onBackPressed: () -> Unit,
) {
    var gender: Gender? by remember { mutableStateOf(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JobisLargeTopAppBar(
            title = stringResource(id = R.string.select_gender),
            onBackPressed = onBackPressed,
        )
        Genders(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            gender = gender,
            onClick = { gender = it },
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisButton(
            modifier = Modifier.padding(bottom = 24.dp),
            text = stringResource(id = R.string.next),
            onClick = {},
            color = ButtonColor.Primary,
        )
    }
}

@Composable
private fun Genders(
    modifier: Modifier = Modifier,
    gender: Gender?,
    onClick: (Gender) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        GenderCard(
            selected = gender == Gender.MAN,
            icon = R.drawable.ic_man,
            text = stringResource(id = R.string.gender_man),
            onClick = { onClick(Gender.MAN) },
        )
        GenderCard(
            selected = gender == Gender.WOMAN,
            icon = R.drawable.ic_woman,
            text = stringResource(id = R.string.gender_woman),
            onClick = { onClick(Gender.WOMAN) },
        )
    }
}

@Composable
private fun RowScope.GenderCard(
    selected: Boolean,
    icon: Int,
    text: String,
    onClick: () -> Unit,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) JobisTheme.colors.onPrimary
        else JobisTheme.colors.inverseSurface,
        label = "",
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) JobisTheme.colors.background
        else JobisTheme.colors.onBackground,
        label = "",
    )
    val tint by animateColorAsState(
        targetValue = if (selected) JobisTheme.colors.background
        else JobisTheme.colors.onSurfaceVariant,
        label = "",
    )

    Column(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                onClick = onClick,
                indication = rememberRipple(),
                interactionSource = remember { MutableInteractionSource() },
            )
            .background(backgroundColor)
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "gender",
            tint = tint,
        )
        Text(
            text = text,
            color = textColor,
            style = JobisTypography.HeadLine,
        )
    }
}
