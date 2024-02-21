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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.jobisdesignsystemv2.utils.clickable

@Composable
fun RecruitmentFilter(onBackPressed: () -> Unit) {
    RecruitmentFilterScreen(onBackPressed = onBackPressed)
}

@Composable
private fun RecruitmentFilterScreen(onBackPressed: () -> Unit) {
    // TODO 뷰모델로 옮기기
    var keyword by remember { mutableStateOf("") }
    val majors = remember { mutableStateListOf<String>() }
    val selectedMajors = remember { mutableStateListOf<String>() }
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
            keyword = { keyword },
            onKeywordChange = { keyword = it },
            majors = majors,
            selectedMajors = selectedMajors,
            onMajorSelected = { selectedMajors.add(it) },
            onMajorUnselected = { selectedMajors.remove(it) },
        )
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
) {
    JobisTextField(
        title = "title",
        value = keyword,
        hint = "검색어를 입력해주세요",
        onValueChange = onKeywordChange,
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
