package team.retum.jobis.recruitment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import team.retum.jobis.recruitment.viewmodel.RecruitmentFilterViewModel.Companion.status
import team.retum.jobis.recruitment.viewmodel.RecruitmentFilterViewModel.Companion.techCode
import team.retum.jobis.recruitment.viewmodel.RecruitmentFilterViewModel.Companion.year
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.chip.JobisChip
import team.retum.jobisdesignsystemv2.chip.JobisChipGroup
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.skills.Skills
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
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
    setYear: (Int) -> Unit,
    setStatus: (RecruitmentStatus?) -> Unit,
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
                selectedYear = state.selectedYear.toPersistentList(),
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
                year = state.selectedYear
                status = state.selectedStatus
                onBackPressed()
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            color = ButtonColor.Primary,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterInputs(
    keyword: () -> String,
    onKeywordChange: (String) -> Unit,
    years: ImmutableList<Int>,
    selectedYear: ImmutableList<Int>,
    setYear: (Int) -> Unit,
    selectedStatus: RecruitmentStatus?,
    setStatus: (RecruitmentStatus?) -> Unit,
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
    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
    ) {
        JobisText(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(R.string.recruitment_fetching_year),
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
                    onClick = { setYear(year) },
                )
            }
        }
    }
    JobisChipGroup(
        title = stringResource(R.string.recruitment_status),
        onItemClick = setStatus,
        selectedItem = selectedStatus,
        items = RecruitmentStatus.entries.toPersistentList(),
        itemText = { it.value },
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
    JobisChipGroup(
        title = stringResource(R.string.job_position_spacing),
        onItemClick = { item ->
            if (selectedMajor == item.keyword) {
                onMajorUnselected()
            } else {
                onMajorSelected(item.keyword, item.code)
            }
        },
        selectedItem = majors.find { it.keyword == selectedMajor },
        items = majors,
        itemText = { it.keyword },
    )
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
