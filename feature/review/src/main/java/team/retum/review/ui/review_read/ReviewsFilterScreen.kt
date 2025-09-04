package team.retum.review.ui.review_read

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.checkbox.JobisCheckBox
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.review.viewmodel.ReviewsFilterState
import team.retum.review.viewmodel.ReviewsFilterViewModel
import team.retum.usecase.entity.CodesEntity
import java.time.LocalDate

@Composable
internal fun ReviewsFilter(
    onBackPressed: () -> Unit,
    reviewsFilterViewModel: ReviewsFilterViewModel = hiltViewModel()
) {
    val state by reviewsFilterViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        reviewsFilterViewModel.getLocalYears()
    }

    ReviewsFilterScreen(
        state = state,
        onBackPressed = onBackPressed,
    )
}

@Composable
private fun ReviewsFilterScreen(
    state: ReviewsFilterState,
    onBackPressed: () -> Unit,
) {
    Column {
        JobisSmallTopAppBar(
            onBackPressed = onBackPressed,
            title = "필터 설정"
        )
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Skills(majorList = state.majorList)
            Years(years = state.years)
            InterviewType()
            Location()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Skills(
    majorList: List<CodesEntity.CodeEntity>,
) {
    Column(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp)
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
            majorList.forEach {
                MajorContent(
                    major = it.keyword,
                    majorId = it.code,
                    selected = false,
                    onClick = {}
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Years(years: List<Int>) {
    Column(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp)
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
            years.forEach {
                YearContent(
                    year = it.toString(),
                    selected = false,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
private fun InterviewType() {
    Column(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp)
    ) {
        JobisText(
            modifier = Modifier.padding(vertical = 12.dp),
            text = "면접 구분",
            style = JobisTypography.SubHeadLine,
            color = JobisTheme.colors.inverseOnSurface,
        )
        ReviewCheckBox(
            title = "개인 면접",
            checked = false,
            onClick = {}
        )
        ReviewCheckBox(
            title = "단체 면접",
            checked = false,
            onClick = {}
        )
        ReviewCheckBox(
            title = "기타 면접",
            checked = false,
            onClick = {}
        )
    }
}

@Composable
private fun Location() {
    Column(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp)
    ) {
        JobisText(
            modifier = Modifier.padding(vertical = 12.dp),
            text = "지역",
            style = JobisTypography.SubHeadLine,
            color = JobisTheme.colors.inverseOnSurface,
        )
        ReviewCheckBox(
            title = "대전",
            checked = false,
            onClick = {}
        )
        ReviewCheckBox(
            title = "서울",
            checked = false,
            onClick = {}
        )
        ReviewCheckBox(
            title = "경기",
            checked = false,
            onClick = {}
        )
        ReviewCheckBox(
            title = "기타",
            checked = false,
            onClick = {}
        )
    }
}

//MajorContent(
//    major = it.keyword,
//    majorId = it.code,
//    selected = selectedMajorCodes.contains(it.code),
//    onClick = { major ->
//        when (selectedMajorCodes.contains(it.code)) {
//            true -> { onUnselectCategory(major) }
//            false -> { onSelectCategory(major) }
//        }
//    },
//)

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
    onClick: (String) -> Unit,
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
                onClick = { onClick(year) },
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
    onClick: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        JobisCheckBox(
            checked = checked,
            onClick = onClick,
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
