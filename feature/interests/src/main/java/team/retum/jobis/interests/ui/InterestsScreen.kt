package team.retum.jobis.interests.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import team.retum.common.component.Skills
import team.retum.jobis.interests.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.tab.TabBar
import team.retum.jobisdesignsystemv2.textfield.JobisTextField

@Composable
internal fun Interests() {
    InterestsScreen()
}

@Composable
private fun InterestsScreen() {
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
            onBackPressed = {},
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
    checkedSkills: MutableList<String>,
) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        JobisTextField(
            // TODO 디자인 시스템에서 title 옵셔널 처리하기
            title = "",
            hint = stringResource(id = R.string.hint_keyword),
            value = content,
            onValueChange = onContentChange,
        )
        TabBar(
            selectedTabIndex = selectedCategoryIndex,
            tabs = categories,
            onSelectTab = onSelectCategory,
        )
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
}




