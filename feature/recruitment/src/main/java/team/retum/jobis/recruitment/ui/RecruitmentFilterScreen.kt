package team.retum.jobis.recruitment.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import team.retum.common.enums.CodeType
import team.retum.common.enums.RecruitmentStatus
import team.retum.jobis.recruitment.R
import team.retum.jobis.recruitment.viewmodel.RecruitmentFilterState
import team.retum.jobis.recruitment.viewmodel.RecruitmentFilterViewModel
import team.retum.jobis.recruitment.viewmodel.RecruitmentFilterViewModel.Companion.jobCode
import team.retum.jobis.recruitment.viewmodel.RecruitmentFilterViewModel.Companion.techCode
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.skills.Skills
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.usecase.entity.CodesEntity

const val SEARCH_DELAY: Long = 200

@Composable
internal fun RecruitmentFilter(
    onBackPressed: () -> Unit,
    recruitmentFilterViewModel: RecruitmentFilterViewModel = hiltViewModel(),
) {
    val state by recruitmentFilterViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        recruitmentFilterViewModel.fetchCodes()
    }

    LaunchedEffect(state.keyword, state.type, state.parentCode) {
        delay(SEARCH_DELAY)
        if (state.type == CodeType.TECH) {
            recruitmentFilterViewModel.fetchCodes()
        }
    }

    RecruitmentFilterScreen(
        onBackPressed = onBackPressed,
        state = state,
        setKeyword = recruitmentFilterViewModel::setKeyword,
        setSelectedMajor = recruitmentFilterViewModel::setSelectedMajor,
        majors = recruitmentFilterViewModel.majors.toPersistentList(),
        techs = recruitmentFilterViewModel.techs,
        onCheckSkill = recruitmentFilterViewModel::addSkill,
        checkedSkills = recruitmentFilterViewModel.checkedSkills,
        setYear = recruitmentFilterViewModel::setYear,
        setStatus = recruitmentFilterViewModel::setStatus,
    )
}

@Composable
private fun RecruitmentFilterScreen(
    onBackPressed: () -> Unit,
    state: RecruitmentFilterState,
    setYear: (Int?) -> Unit,
    setStatus: (String) -> Unit,
    setKeyword: (String) -> Unit,
    setSelectedMajor: (String, Long?) -> Unit,
    majors: ImmutableList<CodesEntity.CodeEntity>,
    techs: SnapshotStateList<CodesEntity.CodeEntity>,
    onCheckSkill: (CodesEntity.CodeEntity, Boolean) -> Unit,
    checkedSkills: SnapshotStateList<CodesEntity.CodeEntity>,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(JobisTheme.colors.background),
        ) {
            JobisSmallTopAppBar(
                onBackPressed = onBackPressed,
                title = stringResource(id = R.string.setting_filter),
            )
            FilterInputs(
                keyword = { state.keyword ?: "" },
                onKeywordChange = setKeyword,
                majors = majors,
                techs = techs.toPersistentList(),
                selectedMajor = state.selectedMajor,
                onMajorSelected = setSelectedMajor,
                onMajorUnselected = { setSelectedMajor("", null) },
                checkedSkills = checkedSkills,
                onCheckSkill = onCheckSkill,
                years = state.years,
                selectedYear = state.selectedYear,
                setYear = setYear,
                selectedStatus = state.selectedStatus,
                setStatus = setStatus,
            )
        }
        JobisButton(
            text = stringResource(id = R.string.appliance),
            onClick = {
                jobCode = state.parentCode
                techCode = checkedSkills.joinToString(separator = ",") { it.code.toString() }
                onBackPressed()
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            color = ButtonColor.Primary,
        )
    }
}

@Composable
private fun FilterInputs(
    keyword: () -> String,
    onKeywordChange: (String) -> Unit,
    years: ImmutableList<Int>,
    selectedYear: Int?,
    setYear: (Int?) -> Unit,
    selectedStatus: String?,
    setStatus: (String) -> Unit,
    majors: ImmutableList<CodesEntity.CodeEntity>,
    techs: ImmutableList<CodesEntity.CodeEntity>,
    selectedMajor: String,
    onMajorSelected: (String, Long?) -> Unit,
    onMajorUnselected: () -> Unit,
    checkedSkills: SnapshotStateList<CodesEntity.CodeEntity>,
    onCheckSkill: (CodesEntity.CodeEntity, Boolean) -> Unit,
) {
    JobisTextField(
        value = keyword,
        hint = stringResource(id = R.string.recruitment_search_hint),
        onValueChange = onKeywordChange,
        drawableResId = JobisIcon.Search,
    )
    Years(
        title = "연도 조회",
        onYearSelected = setYear,
        selectedYear = selectedYear,
        years = years,
    )
    Statuses(
        title = "모집 상태",
        selectedStatus = selectedStatus,
        onStatusSelected = setStatus,
    )
    Majors(
        majors = majors,
        techs = techs,
        selectedMajor = selectedMajor,
        onMajorSelected = onMajorSelected,
        onMajorUnselected = onMajorUnselected,
        checkedSkills = checkedSkills,
        onCheckSkill = onCheckSkill,
    )
    // TODO :: 3. 뷰모델 구현(쿼리, 선택 ui)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Majors(
    majors: ImmutableList<CodesEntity.CodeEntity>,
    techs: ImmutableList<CodesEntity.CodeEntity>,
    selectedMajor: String,
    onMajorSelected: (String, Long?) -> Unit,
    onMajorUnselected: () -> Unit,
    checkedSkills: SnapshotStateList<CodesEntity.CodeEntity>,
    onCheckSkill: (CodesEntity.CodeEntity, Boolean) -> Unit,
) {
    JobisText(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        text = "모집 분야",
        style = JobisTypography.SubHeadLine,
        color = JobisTheme.colors.inverseOnSurface,
    )
    FlowRow(
        maxItemsInEachRow = 5,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 8.dp,
            )
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        majors.forEach {
            MajorContent(
                major = it.keyword,
                selected = selectedMajor == it.keyword,
                onClick = { major ->
                    when (selectedMajor == it.keyword) {
                        true -> onMajorUnselected()
                        false -> onMajorSelected(major, it.code)
                    }
                },
            )
        }
    }
    Skills(
        skills = techs.map { it.keyword }.toMutableStateList(),
        checkedSkills = checkedSkills.map { it.keyword }.toPersistentList(),
        checkSkillsId = techs.map { it.code }.toPersistentList(),
    ) { index, checked, id ->
        onCheckSkill(
            CodesEntity.CodeEntity(
                code = id,
                keyword = index,
            ),
            checked,
        )
    }
}

@Composable
private fun MajorContent(
    major: String,
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
        modifier = Modifier
            .clickable(
                enabled = true,
                onClick = { onClick(major) },
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Years( // TODO :: 2. 이거 참고해서 구현
    title: String = "",
    years: ImmutableList<Int>,
    selectedYear: Int?,
    onYearSelected: (Int?) -> Unit,
) {
    Column(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp),
    ) {
        JobisText(
            modifier = Modifier.padding(vertical = 8.dp),
            text = title,
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
                    selected = selectedYear == year,
                    onClick = { onYearSelected(year) },
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Statuses(
    title: String = "",
    selectedStatus: String?,
    onStatusSelected: (String) -> Unit,
) {
    JobisText(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        text = title,
        style = JobisTypography.SubHeadLine,
        color = JobisTheme.colors.inverseOnSurface,
    )
    FlowRow(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 5,
    ) {
        RecruitmentStatus.entries.forEach { status ->
            JobisChip (
                text = status.value,
                selected = selectedStatus == status.value,
                onClick = { onStatusSelected(status.value) },
            )
        }
    }
}

@Composable
private fun JobisChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val background by animateColorAsState(
        targetValue = if (selected) JobisTheme.colors.onPrimary else JobisTheme.colors.inverseSurface,
        label = "",
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) JobisTheme.colors.background else JobisTheme.colors.onPrimaryContainer,
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
            text = text,
            style = JobisTypography.Body,
            color = textColor,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> JobisChipGroup(
    title: String,
    items: ImmutableList<T>,
    itemText: (T) -> String,
    selectedItem: T?,
    onItemClick: (T) -> Unit,
    maxItemsInEachRow: Int = 5,
) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
    ) {
        if (title.isNotBlank()) {
            JobisText(
                modifier = Modifier.padding(vertical = 8.dp),
                text = title,
                style = JobisTypography.SubHeadLine,
                color = JobisTheme.colors.inverseOnSurface,
            )
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = maxItemsInEachRow,
        ) {
            items.forEach { item ->
                JobisChip(
                    text = itemText(item),
                    selected = selectedItem == item,
                    onClick = { onItemClick(item) },
                )
            }
        }
    }
}
