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
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.common.enums.Gender
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.signup.R
import team.retum.signup.model.SignUpData
import team.retum.signup.viewmodel.SelectGenderSideEffect
import team.retum.signup.viewmodel.SelectGenderState
import team.retum.signup.viewmodel.SelectGenderViewModel

@Composable
internal fun SelectGender(
    onBackPressed: () -> Unit,
    signUpData: SignUpData,
    navigateToSetProfile: (SignUpData) -> Unit,
    selectGenderViewModel: SelectGenderViewModel = hiltViewModel(),
) {
    val state by selectGenderViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        selectGenderViewModel.sideEffect.collect {
            when (it) {
                is SelectGenderSideEffect.MoveToNext -> {
                    navigateToSetProfile(signUpData.copy(gender = it.gender))
                }
            }
        }
    }

    SelectGenderScreen(
        onBackPressed = onBackPressed,
        onNextClick = selectGenderViewModel::onNextClick,
        state = state,
        setGender = selectGenderViewModel::setGender,
    )
}

@Composable
private fun SelectGenderScreen(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
    state: SelectGenderState,
    setGender: (Gender) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JobisLargeTopAppBar(
            title = stringResource(id = R.string.select_gender),
            onBackPressed = onBackPressed,
        )
        Genders(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 16.dp,
                ),
            gender = state.gender,
            onClick = setGender,
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisButton(
            text = stringResource(id = R.string.next),
            onClick = onNextClick,
            color = ButtonColor.Primary,
            enabled = state.buttonEnabled,
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
        targetValue = if (selected) {
            JobisTheme.colors.onPrimary
        } else {
            JobisTheme.colors.inverseSurface
        },
        label = "",
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) {
            JobisTheme.colors.background
        } else {
            JobisTheme.colors.onBackground
        },
        label = "",
    )
    val tint by animateColorAsState(
        targetValue = if (selected) {
            JobisTheme.colors.background
        } else {
            JobisTheme.colors.onSurfaceVariant
        },
        label = "",
    )

    Column(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                onClick = onClick,
                indication = ripple(),
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
        JobisText(
            text = text,
            color = textColor,
            style = JobisTypography.HeadLine,
        )
    }
}
