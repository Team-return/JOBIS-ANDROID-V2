package team.retum.jobis.interests.ui

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.jobis.interests.R
import team.retum.jobis.interests.viewmodel.InterestsState
import team.retum.jobis.interests.viewmodel.InterestsViewmodel
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.usecase.entity.CodesEntity

@Composable
internal fun Interests(
    onBackPressed: () -> Unit,
    navigateToInterestsComplete: () -> Unit,
    interestsViewmodel: InterestsViewmodel = hiltViewModel(),
) {
    val state by interestsViewmodel.state.collectAsStateWithLifecycle()

    InterestsScreen(
        onBackPressed = onBackPressed,
        state = state,
        setSelectedMajor = interestsViewmodel::setMajor,
        patchInterestsMajor = interestsViewmodel::patchInterestsMajor,
        navigateToInterestsComplete = navigateToInterestsComplete,
    )
}

@Composable
private fun InterestsScreen(
    onBackPressed: () -> Unit,
    state: InterestsState,
    setSelectedMajor: (Long) -> Unit,
    patchInterestsMajor: () -> Unit,
    navigateToInterestsComplete: () -> Unit,
) {
    // TODO 뷰모델로 옮기기
    val selectedMajorCount = state.selectedMajorCodes.size
    val buttonEnable: Boolean
    val buttonText = if (selectedMajorCount > 0) {
        buttonEnable = true
        stringResource(R.string.select_interests_button_count, selectedMajorCount)
    } else {
        buttonEnable = false
        stringResource(R.string.select_interests_button)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(
            title = stringResource(id = R.string.set_interests),
            onBackPressed = onBackPressed,
        )
        InterestsTitle(studentName = state.studentName)
        InterestsInput(
            selectedMajor = state.selectedMajorId,
            categories = state.majorList,
            selectedMajorCodes = state.selectedMajorCodes,
            onSelectCategory = setSelectedMajor,
            onUnselectCategory = {
                setSelectedMajor(it)
            },
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisButton(
            text = buttonText,
            color = ButtonColor.Primary,
            onClick = {
                patchInterestsMajor()
                navigateToInterestsComplete()
            },
            enabled = buttonEnable,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun InterestsInput(
    selectedMajor: Long,
    categories: List<CodesEntity.CodeEntity>,
    selectedMajorCodes: List<Long>,
    onSelectCategory: (Long) -> Unit,
    onUnselectCategory: (Long) -> Unit,
) {
    FlowRow(
        modifier = Modifier.padding(
            horizontal = 24.dp,
            vertical = 36.dp,
        ),
        maxItemsInEachRow = 5,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Log.d("TEST", selectedMajorCodes.toString())
        categories.forEach {
            MajorContent(
                major = it.keyword,
                majorId = it.code,
                selected = selectedMajorCodes.contains(it.code),
                onClick = { major ->
                    when (selectedMajor == it.code) {
                        true -> { onUnselectCategory(major) }
                        false -> { onSelectCategory(major) }
                    }
                },
            )
        }
    }
}

@Composable
private fun InterestsTitle(
    studentName: String,
) {
    Column(
        modifier = Modifier.padding(top = 34.dp),
    ) {
        JobisText(
            modifier = Modifier.padding(
                top = 20.dp,
                start = 24.dp,
                end = 24.dp,
            ),
            text = stringResource(R.string.interests_select_title, studentName),
            style = JobisTypography.PageTitle,
            color = JobisTheme.colors.onBackground,
        )
        JobisText(
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 20.dp,
                start = 24.dp,
                end = 24.dp,
            ),
            text = stringResource(R.string.interests_alarm),
            style = JobisTypography.SubBody,
        )
    }
}

@Composable
private fun MajorContent(
    modifier: Modifier = Modifier,
    major: String,
    majorId: Long,
    selected: Boolean,
    onClick: (Long) -> Unit,
) {
    val background by animateColorAsState(
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
            JobisTheme.colors.onPrimaryContainer
        },
        label = "",
    )

    Box(
        modifier = modifier
            .clickable(
                enabled = true,
                onClick = { onClick(majorId) },
                onPressed = {},
            )
            .clip(RoundedCornerShape(30.dp))
            .background(background),
        contentAlignment = Alignment.Center,
    ) {
        JobisText(
            modifier = modifier.padding(
                horizontal = 16.dp,
                vertical = 6.dp,
            ),
            text = major,
            style = JobisTypography.Body,
            color = textColor,
        )
    }
}
