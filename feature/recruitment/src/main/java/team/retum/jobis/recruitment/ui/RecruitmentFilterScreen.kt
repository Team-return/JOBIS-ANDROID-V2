package team.retum.jobis.recruitment.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.common.component.Skills
import team.retum.jobis.recruitment.viewmodel.RecruitmentFilterState
import team.retum.jobis.recruitment.viewmodel.RecruitmentFilterViewModel
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.jobisdesignsystemv2.utils.clickable

@Composable
internal fun RecruitmentFilter(
    onBackPressed: () -> Unit,
    recruitmentFilterViewModel: RecruitmentFilterViewModel = hiltViewModel(),
) {
    val state by recruitmentFilterViewModel.state.collectAsStateWithLifecycle()

    RecruitmentFilterScreen(
        onBackPressed = onBackPressed,
        recruitmentFilterViewModel = recruitmentFilterViewModel,
        state = state,
        setKeyword = recruitmentFilterViewModel::setKeyword,
    )
}

@Composable
private fun RecruitmentFilterScreen(
    onBackPressed: () -> Unit,
    recruitmentFilterViewModel: RecruitmentFilterViewModel,
    state: RecruitmentFilterState,
    setKeyword: (String) -> Unit,
) {
    val majors = remember { mutableStateListOf<String>() }
    val selectedMajors = remember { mutableStateListOf<String>() }
    val checkedSkills = remember { mutableStateListOf<String>() }
    majors.run {
        add("iOS")
        add("Android")
        add("Front-end")
        add("Back-end")
        add("Embedded")
        add("Security")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(
            onBackPressed = onBackPressed,
            title = "필터 설정",
        )
        FilterInputs(
            keyword = { state.keyword ?: "" },
            onKeywordChange = setKeyword,
            majors = majors,
            selectedMajors = selectedMajors,
            onMajorSelected = { selectedMajors.add(it) },
            onMajorUnselected = { selectedMajors.remove(it) },
            checkedSkills = checkedSkills,
        )
        LazyColumn {
            items(recruitmentFilterViewModel.techs) {
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterInputs(
    keyword: () -> String,
    onKeywordChange: (String) -> Unit,
    majors: SnapshotStateList<String>,
    selectedMajors: SnapshotStateList<String>,
    onMajorSelected: (String) -> Unit,
    onMajorUnselected: (String) -> Unit,
    checkedSkills: MutableList<String>,
) {
    JobisTextField(
        value = keyword,
        hint = "검색어를 입력해주세요",
        onValueChange = onKeywordChange,
        leadingIcon = painterResource(id = JobisIcon.Search),
    )
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 8.dp,
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        majors.forEach {
            val selected = selectedMajors.contains(it)
            MajorContent(
                major = it,
                selected = { selected },
                onClick = {
                    when (selected) {
                        true -> onMajorUnselected(it)
                        false -> onMajorSelected(it)
                    }
                },
            )
        }
    }
    // TODO 더미 데이터 제거
    Skills(
        skills = listOf(
            "Kotlin",
            "Java",
        ).toMutableStateList(),
        checkedSkills = checkedSkills,
        onCheckedChange = { index, checked ->
            // TODO 뷰모델로 함수 옮기기
            checkedSkills.run {
                when (checked) {
                    true -> add(index)
                    false -> remove(index)
                }
            }
        },
    )
}

@Composable
private fun MajorContent(
    major: String,
    selected: () -> Boolean,
    onClick: () -> Unit,
) {
    val background by animateColorAsState(
        targetValue = if (selected()) {
            JobisTheme.colors.onPrimary
        } else {
            JobisTheme.colors.inverseSurface
        },
        label = "",
    )
    val textColor by animateColorAsState(
        targetValue = if (selected()) {
            JobisTheme.colors.background
        } else {
            JobisTheme.colors.onPrimaryContainer
        },
        label = "",
    )

    Box(
        modifier = Modifier
            .clickable(
                enabled = true,
                onClick = onClick,
                onPressed = {},
            )
            .clip(RoundedCornerShape(30.dp))
            .background(background),
        contentAlignment = Alignment.Center,
    ) {
        JobisText(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 4.dp,
            ),
            text = major,
            style = JobisTypography.Body,
            color = textColor,
        )
    }
}
