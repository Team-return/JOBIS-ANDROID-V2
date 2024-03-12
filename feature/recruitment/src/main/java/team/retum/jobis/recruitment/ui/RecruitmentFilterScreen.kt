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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import team.retum.common.component.Skills
import team.retum.common.enums.CodeType
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
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.usecase.entity.CodesEntity

@Composable
internal fun RecruitmentFilter(
    onBackPressed: () -> Unit,
    recruitmentFilterViewModel: RecruitmentFilterViewModel = hiltViewModel(),
) {
    val state by recruitmentFilterViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.keyword, state.type, state.parentCode) {
        delay(300)
        if (state.type == CodeType.TECH) {
            recruitmentFilterViewModel.fetchCodes()
        }
    }

    RecruitmentFilterScreen(
        onBackPressed = onBackPressed,
        state = state,
        setKeyword = recruitmentFilterViewModel::setKeyword,
        setSelectedMajor = recruitmentFilterViewModel::setSelectedMajor,
        majors = recruitmentFilterViewModel.majors,
        techs = recruitmentFilterViewModel.techs,
        onCheckSkill = recruitmentFilterViewModel::addSkill,
        checkedSkills = recruitmentFilterViewModel.checkedSkills,
    )
}

@Composable
private fun RecruitmentFilterScreen(
    onBackPressed: () -> Unit,
    state: RecruitmentFilterState,
    setKeyword: (String) -> Unit,
    setSelectedMajor: (String, Long?) -> Unit,
    majors: List<CodesEntity.CodeEntity>,
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
                techs = techs,
                selectedMajor = state.selectedMajor,
                onMajorSelected = setSelectedMajor,
                onMajorUnselected = { setSelectedMajor("", null) },
                checkedSkills = checkedSkills,
                onCheckSkill = onCheckSkill,
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterInputs(
    keyword: () -> String,
    onKeywordChange: (String) -> Unit,
    majors: List<CodesEntity.CodeEntity>,
    techs: List<CodesEntity.CodeEntity>,
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
        checkedSkills = checkedSkills.map { it.keyword },
        checkSkillsId = techs.map { it.code },
    ) { index, checked, id ->
        checkedSkills.run {
            onCheckSkill(
                CodesEntity.CodeEntity(
                    code = id,
                    keyword = index,
                ),
                checked,
            )
        }
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
