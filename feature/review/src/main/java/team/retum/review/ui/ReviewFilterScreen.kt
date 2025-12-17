package team.retum.review.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.common.enums.RecruitmentStatus
import team.retum.jobis.review.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.checkbox.JobisCheckBox
import team.retum.jobisdesignsystemv2.chip.JobisChip
import team.retum.jobisdesignsystemv2.chip.JobisChipGroup
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.review.viewmodel.ReviewFilterViewModel
import team.retum.review.viewmodel.ReviewsFilterState
import team.retum.usecase.entity.CodesEntity

@Composable
internal fun ReviewFilter(
    onBackPressed: () -> Unit,
    reviewFilterViewModel: ReviewFilterViewModel = hiltViewModel(),
) {
    val state by reviewFilterViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        reviewFilterViewModel.getLocalYears()
    }

    ReviewFilterScreen(
        state = state,
        onBackPressed = onBackPressed,
        onMajorSelected = reviewFilterViewModel::setSelectedMajor,
        onYearSelected = reviewFilterViewModel::setSelectedYear,
        onInterviewTypeSelected = reviewFilterViewModel::setSelectedInterviewType,
        onLocationSelected = reviewFilterViewModel::setSelectedLocation,
        onApplyFilter = { code, year, interviewType, location ->
            ReviewFilterViewModel.code = code
            ReviewFilterViewModel.year = year
            ReviewFilterViewModel.interviewType = interviewType
            ReviewFilterViewModel.location = location
            onBackPressed()
        },
    )
}

@Composable
private fun ReviewFilterScreen(
    state: ReviewsFilterState,
    onBackPressed: () -> Unit,
    onMajorSelected: (Long?) -> Unit,
    onYearSelected: (Int) -> Unit,
    onInterviewTypeSelected: (InterviewType?) -> Unit,
    onLocationSelected: (InterviewLocation?) -> Unit,
    onApplyFilter: (Long?, ImmutableList<Int>, InterviewType?, InterviewLocation?) -> Unit,
) {
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                return Offset.Zero
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            JobisSmallTopAppBar(
                onBackPressed = onBackPressed,
                title = stringResource(id = R.string.filter_setting),
            )
            Column(
                modifier = Modifier.nestedScroll(nestedScrollConnection)
            ) {
                Skills(
                    majorList = state.majorList.toPersistentList(),
                    selectedMajorCode = state.selectedMajorCode,
                    onMajorSelected = onMajorSelected,
                )
                Years(
                    years = state.years.toPersistentList(),
                    selectedYear = state.selectedYear,
                    onYearSelected = onYearSelected,
                )
                InterviewType(
                    selectedInterviewType = state.selectedInterviewType,
                    onInterviewTypeSelected = onInterviewTypeSelected,
                )
                Location(
                    selectedLocation = state.selectedLocation,
                    onLocationSelected = onLocationSelected,
                )
            }
        }
        JobisButton(
            text = stringResource(id = R.string.appliance),
            onClick = {
                onApplyFilter(
                    state.selectedMajorCode,
                    state.selectedYear,
                    state.selectedInterviewType,
                    state.selectedLocation,
                )
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            color = ButtonColor.Primary,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Skills(
    majorList: ImmutableList<CodesEntity.CodeEntity>,
    selectedMajorCode: Long?,
    onMajorSelected: (Long?) -> Unit,
) {
    Column(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp),
    ) {
        JobisText(
            modifier = Modifier.padding(vertical = 12.dp),
            text = stringResource(id = R.string.major),
            style = JobisTypography.SubHeadLine,
            color = JobisTheme.colors.inverseOnSurface,
        )
        FlowRow(
            modifier = Modifier.padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 5,
        ) {
            majorList.forEach { codes ->
                MajorContent(
                    major = codes.keyword,
                    majorId = codes.code,
                    selected = selectedMajorCode == codes.code,
                    onClick = { onMajorSelected(it) },
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Years(
    years: ImmutableList<Int>,
    selectedYear: ImmutableList<Int>,
    onYearSelected: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
    ) {
        JobisText(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(R.string.year),
            style = JobisTypography.SubHeadLine,
            color = JobisTheme.colors.inverseOnSurface,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 5,
        ) {
            years.forEach { year ->
                JobisChip(
                    text = year.toString(),
                    selected = selectedYear.contains(year),
                    onClick = { onYearSelected(year) },
                )
            }
        }
    }
}

@Composable
private fun InterviewType(
    selectedInterviewType: InterviewType?,
    onInterviewTypeSelected: (InterviewType?) -> Unit,
) {
    Column(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp),
    ) {
        JobisText(
            modifier = Modifier.padding(vertical = 12.dp),
            text = stringResource(id = R.string.interview_category),
            style = JobisTypography.SubHeadLine,
            color = JobisTheme.colors.inverseOnSurface,
        )
        InterviewType.entries.forEach { interviewType ->
            ReviewCheckBox(
                title = interviewType.value,
                checked = selectedInterviewType == interviewType,
                onClick = { onInterviewTypeSelected(interviewType) },
            )
        }
    }
}

@Composable
private fun Location(
    selectedLocation: InterviewLocation?,
    onLocationSelected: (InterviewLocation?) -> Unit,
) {
    Column(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp),
    ) {
        InterviewLocation.entries.forEach { interviewLocation ->
            ReviewCheckBox(
                title = interviewLocation.value,
                checked = selectedLocation == interviewLocation,
                onClick = { onLocationSelected(interviewLocation) },
            )
        }
    }
}

@Composable
private fun MajorContent(
    modifier: Modifier = Modifier,
    major: String,
    majorId: Long,
    selected: Boolean,
    onClick: (Long?) -> Unit,
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
                horizontal = 12.dp,
                vertical = 4.dp,
            ),
            text = major,
            style = JobisTypography.Body,
            color = textColor,
        )
    }
}

@Composable
private fun YearContent(
    modifier: Modifier = Modifier,
    year: String,
    selected: Boolean,
    onClick: (Int) -> Unit,
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
                onClick = { onClick(year.toInt()) },
                onPressed = {},
            )
            .clip(RoundedCornerShape(30.dp))
            .background(background),
        contentAlignment = Alignment.Center,
    ) {
        JobisText(
            modifier = modifier.padding(
                horizontal = 12.dp,
                vertical = 4.dp,
            ),
            text = year,
            style = JobisTypography.Body,
            color = textColor,
        )
    }
}

@Composable
private fun ReviewCheckBox(
    title: String,
    checked: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(vertical = 12.dp),
    ) {
        JobisCheckBox(
            checked = checked,
            onClick = { onClick() },
        )
        Spacer(modifier = Modifier.width(8.dp))
        JobisText(
            text = title,
            style = JobisTypography.Body,
            color = JobisTheme.colors.inverseOnSurface,
            modifier = Modifier.align(Alignment.CenterVertically),
        )
    }
}
