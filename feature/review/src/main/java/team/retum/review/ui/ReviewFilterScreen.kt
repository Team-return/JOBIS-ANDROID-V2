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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.jobis.review.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.checkbox.JobisCheckBox
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.review.viewmodel.ReviewFilterViewModel
import team.retum.review.viewmodel.ReviewFilterViewModel.Companion.code
import team.retum.review.viewmodel.ReviewFilterViewModel.Companion.interviewType
import team.retum.review.viewmodel.ReviewFilterViewModel.Companion.location
import team.retum.review.viewmodel.ReviewFilterViewModel.Companion.year
import team.retum.review.viewmodel.ReviewsFilterState
import team.retum.usecase.entity.CodesEntity

/**
 * Hosts the review filter UI: observes the view model state, requests available years once on first composition, and renders ReviewFilterScreen with event bindings.
 *
 * @param onBackPressed Callback invoked to navigate back from the screen.
 */
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
    )
}

/**
 * Renders the review filter screen with sections for major, year, interview type, and location,
 * and a persistent action button that confirms the current selections and navigates back.
 *
 * @param state Current UI state containing available majors, years, and selected values.
 * @param onBackPressed Callback invoked to navigate back; also called after confirming selections.
 * @param onMajorSelected Called when the user selects or clears a major; receives the selected major id or `null`.
 * @param onYearSelected Called when the user selects or clears a year; receives the selected year or `null`.
 * @param onInterviewTypeSelected Called when the user selects or clears an interview type; receives the selected `InterviewType` or `null`.
 * @param onLocationSelected Called when the user selects or clears a location; receives the selected `InterviewLocation` or `null`.
 */
@Composable
private fun ReviewFilterScreen(
    state: ReviewsFilterState,
    onBackPressed: () -> Unit,
    onMajorSelected: (Long?) -> Unit,
    onYearSelected: (Int?) -> Unit,
    onInterviewTypeSelected: (InterviewType?) -> Unit,
    onLocationSelected: (InterviewLocation?) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            JobisSmallTopAppBar(
                onBackPressed = onBackPressed,
                title = "필터 설정",
            )
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {
                Skills(
                    majorList = state.majorList,
                    selectedMajorCode = state.selectedMajorCode,
                    onMajorSelected = onMajorSelected,
                )
                Years(
                    years = state.years,
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
                code = state.selectedMajorCode
                year = state.selectedYear
                location = state.selectedLocation
                interviewType = state.selectedInterviewType
                onBackPressed()
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            color = ButtonColor.Primary,
        )
    }
}

/**
 * Displays the "전공" section with selectable chips for each major.
 *
 * Renders a heading and a responsive grid of MajorContent chips. Tapping a chip invokes
 * the provided callback with that major's code or `null` to clear selection.
 *
 * @param majorList List of major entries to display; each provides a display keyword and a numeric code.
 * @param selectedMajorCode The code of the currently selected major, or `null` if none is selected.
 * @param onMajorSelected Callback invoked with the selected major code, or `null` to clear selection.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Skills(
    majorList: List<CodesEntity.CodeEntity>,
    selectedMajorCode: Long?,
    onMajorSelected: (Long?) -> Unit,
) {
    Column(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp),
    ) {
        JobisText(
            modifier = Modifier.padding(vertical = 12.dp),
            text = "전공",
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

/**
 * Renders a "년도" section containing selectable year chips arranged in a responsive flow.
 *
 * @param years The list of years to display as selectable chips.
 * @param selectedYear The currently selected year, or `null` if none is selected.
 * @param onYearSelected Callback invoked when a year chip is clicked; receives the selected year or `null` to clear the selection.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Years(
    years: List<Int>,
    selectedYear: Int?,
    onYearSelected: (Int?) -> Unit,
) {
    Column(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp),
    ) {
        JobisText(
            modifier = Modifier.padding(vertical = 12.dp),
            text = "년도",
            style = JobisTypography.SubHeadLine,
            color = JobisTheme.colors.inverseOnSurface,
        )
        FlowRow(
            modifier = Modifier.padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 5,
        ) {
            years.forEach { year ->
                YearContent(
                    year = "$year",
                    selected = selectedYear == year,
                    onClick = { onYearSelected(it) },
                )
            }
        }
    }
}

/**
 * Displays the "면접 구분" section with selectable interview type options.
 *
 * @param selectedInterviewType The currently selected interview type, or `null` if none is selected.
 * @param onInterviewTypeSelected Callback invoked when an option is selected; receives the chosen `InterviewType` or `null`.
 */
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
            text = "면접 구분",
            style = JobisTypography.SubHeadLine,
            color = JobisTheme.colors.inverseOnSurface,
        )
        ReviewCheckBox(
            title = "개인 면접",
            checked = selectedInterviewType == InterviewType.INDIVIDUAL,
            onClick = { onInterviewTypeSelected(InterviewType.INDIVIDUAL) },
        )
        ReviewCheckBox(
            title = "단체 면접",
            checked = selectedInterviewType == InterviewType.GROUP,
            onClick = { onInterviewTypeSelected(InterviewType.GROUP) },
        )
        ReviewCheckBox(
            title = "기타 면접",
            checked = selectedInterviewType == InterviewType.OTHER,
            onClick = { onInterviewTypeSelected(InterviewType.OTHER) },
        )
    }
}

/**
 * Displays the location filter section with selectable location options.
 *
 * Shows a titled section ("지역") and four choices; tapping a choice calls `onLocationSelected`
 * with the selected `InterviewLocation`.
 *
 * @param selectedLocation The currently selected `InterviewLocation`, or `null` if none is selected.
 * @param onLocationSelected Callback invoked with the chosen `InterviewLocation` when the user selects an option.
 */
@Composable
private fun Location(
    selectedLocation: InterviewLocation?,
    onLocationSelected: (InterviewLocation?) -> Unit,
) {
    Column(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp),
    ) {
        JobisText(
            modifier = Modifier.padding(vertical = 12.dp),
            text = "지역",
            style = JobisTypography.SubHeadLine,
            color = JobisTheme.colors.inverseOnSurface,
        )
        ReviewCheckBox(
            title = "대전",
            checked = selectedLocation == InterviewLocation.DAEJEON,
            onClick = { onLocationSelected(InterviewLocation.DAEJEON) },
        )
        ReviewCheckBox(
            title = "서울",
            checked = selectedLocation == InterviewLocation.SEOUL,
            onClick = { onLocationSelected(InterviewLocation.SEOUL) },
        )
        ReviewCheckBox(
            title = "경기",
            checked = selectedLocation == InterviewLocation.GYEONGGI,
            onClick = { onLocationSelected(InterviewLocation.GYEONGGI) },
        )
        ReviewCheckBox(
            title = "기타",
            checked = selectedLocation == InterviewLocation.OTHER,
            onClick = { onLocationSelected(InterviewLocation.OTHER) },
        )
    }
}

/**
 * Displays a selectable pill for a major, updating its colors with animation when selected.
 *
 * @param major The display name of the major.
 * @param majorId The identifier for the major; passed to `onClick` when the pill is pressed.
 * @param selected Whether the pill is currently selected.
 * @param onClick Callback invoked with `majorId` when the pill is clicked (use `null` to clear selection).
 */
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

/**
 * Displays a selectable pill for a year.
 *
 * The chip shows the provided year string and visually reflects the `selected` state. When tapped,
 * the `year` string is parsed to an `Int` and passed to `onClick`.
 *
 * @param year The year text to display; must be parseable as an `Int`.
 * @param onClick Callback invoked with the parsed year `Int` when the chip is clicked.
 */
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

/**
 * Renders a horizontal row containing a checkbox and a label for a single filter option.
 *
 * @param title The label text displayed next to the checkbox.
 * @param checked `true` if the checkbox is selected, `false` otherwise.
 * @param onClick Callback invoked when the checkbox is clicked.
 */
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