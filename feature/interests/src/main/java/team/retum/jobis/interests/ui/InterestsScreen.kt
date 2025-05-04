package team.retum.jobis.interests.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.toPersistentList
import team.retum.interests.R
import team.retum.jobis.interests.viewmodel.InterestsViewmodel
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.skills.Skills
import team.retum.jobisdesignsystemv2.tab.TabBar
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.jobisdesignsystemv2.utils.clickable

@Composable
internal fun Interests(
    onBackPressed: () -> Unit,
    interestsViewmodel: InterestsViewmodel = hiltViewModel(),
) {
    InterestsScreen(
        onBackPressed = onBackPressed,
    )
}

@Composable
private fun InterestsScreen(onBackPressed: () -> Unit) {
    // TODO 뷰모델로 옮기기
    var content by remember { mutableStateOf("") }
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    val checkedSkills = remember { mutableStateListOf<String>() }
    val categories = remember {
        mutableStateListOf(
            "Android",
            "Back-end",
            "Front-end",
        )
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
        InterestsInput(
            content = { content },
            onContentChange = { content = it },
            categories = categories,
            selectedCategoryIndex = selectedCategoryIndex,
            onSelectCategory = { selectedCategoryIndex = it },
            checkedSkills = checkedSkills,
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun InterestsInput(
    content: () -> String,
    onContentChange: (String) -> Unit,
    selectedCategoryIndex: Int,
    categories: SnapshotStateList<String>,
    onSelectCategory: (Int) -> Unit,
    checkedSkills: SnapshotStateList<String>,
) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        JobisTextField(
            hint = stringResource(id = R.string.hint_keyword),
            value = content,
            onValueChange = onContentChange,
        )
        TabBar(
            selectedTabIndex = selectedCategoryIndex,
            tabs = categories.toPersistentList(),
            onSelectTab = onSelectCategory,
        )
        // TODO 더미 데이터 제거
        Skills(
            skills = listOf(
                "Kotlin",
                "Java",
            ).toMutableStateList(),
            checkedSkills = checkedSkills.toPersistentList(),
            onCheckedChange = { index, checked, _ ->
                // TODO 뷰모델로 함수 옮기기
                checkedSkills.run {
                    when (checked) {
                        true -> add(index)
                        false -> remove(index)
                    }
                }
            },
        )
        MajorContent(
            major = "채도훈과",
            selected = true,
            onClick = {},
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
