package team.retum.jobis.interests.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import team.retum.jobis.interests.R
import team.returm.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.returm.jobisdesignsystemv2.foundation.JobisTheme
import team.returm.jobisdesignsystemv2.foundation.JobisTypography
import team.returm.jobisdesignsystemv2.text.JobisText
import team.returm.jobisdesignsystemv2.textfield.JobisTextField
import team.returm.jobisdesignsystemv2.utils.clickable

@Composable
internal fun Interests() {
    InterestsScreen()
}

@Composable
private fun InterestsScreen() {
    // TODO 뷰모델로 옮기기
    var content by remember { mutableStateOf("") }
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
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
) {
    Column(
        modifier = Modifier.padding(
            vertical = 16.dp,
        ),
    ) {
        JobisTextField(
            // TODO 디자인 시스템에서 title 옵셔널 처리하기
            title = "",
            hint = stringResource(id = R.string.hint_keyword),
            value = content,
            onValueChange = onContentChange,
        )
        CategoryTab(
            selectedCategoryIndex = selectedCategoryIndex,
            categories = categories,
            onSelectCategory = onSelectCategory,
        )
        // TODO 더미 데이터 제거
        Skills(
            skills = listOf(
                "Kotlin",
                "Java",
            ).toMutableStateList(),
            checkedSkills = mutableListOf(),
        )
    }
}

@Composable
private fun CategoryTab(
    selectedCategoryIndex: Int,
    categories: SnapshotStateList<String>,
    onSelectCategory: (Int) -> Unit,
) {
    LazyRow(
        modifier = Modifier
            .padding(
                start = 24.dp,
                top = 8.dp,
                bottom = 8.dp,
            )
            .clip(
                RoundedCornerShape(
                    topStart = 12.dp,
                    bottomStart = 12.dp,
                ),
            )
            .background(JobisTheme.colors.inverseSurface),
    ) {
        itemsIndexed(categories) { index, category ->
            val selected = index == selectedCategoryIndex
            val textColor by animateColorAsState(
                targetValue = if (selected) JobisTheme.colors.onBackground
                else JobisTheme.colors.onSurfaceVariant,
                label = "",

                )
            val backgroundColor by animateColorAsState(
                targetValue = if (selected) JobisTheme.colors.background
                else JobisTheme.colors.inverseSurface,
                label = "",
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessVeryLow,
                )
            )

            JobisText(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(
                        enabled = true,
                        onPressed = {},
                        onClick = { onSelectCategory(index) },
                    )
                    .background(backgroundColor)
                    .padding(
                        vertical = 8.dp,
                        horizontal = 10.dp,
                    ),
                text = category,
                style = JobisTypography.Body,
                color = textColor,
            )
        }
    }
}

@Composable
private fun Skills(
    skills: SnapshotStateList<String>,
    checkedSkills: MutableList<Int>
) {
    LazyColumn {
        itemsIndexed(skills) { index, skill ->
            var checked by remember { mutableStateOf(false) }

            SkillContent(
                skill = skill,
                checked = checked,
                onClick = {
                    checkedSkills.add(index)
                    checked = !checked
                },
            )
        }
    }
}

@Composable
private fun SkillContent(
    skill: String,
    checked: Boolean,
    onClick: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp,
            )
            .clickable(
                enabled = true,
                onPressed = {},
                onClick = { onClick(!checked) }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CheckBox(
            checked = checked,
            onClick = onClick,
        )
        JobisText(
            text = skill,
            style = JobisTypography.Body,
            color = JobisTheme.colors.inverseOnSurface,
        )

    }
}

@Composable
fun CheckBox(
    checked: Boolean,
    onClick: (Boolean) -> Unit,
) {
    val tint by animateColorAsState(
        targetValue = if (checked) JobisTheme.colors.background
        else JobisTheme.colors.surfaceTint,
        label = "",
    )
    val background by animateColorAsState(
        targetValue = if (checked) JobisTheme.colors.onPrimary
        else JobisTheme.colors.inverseSurface,
        label = "",
    )

    Box(
        modifier = Modifier
            .clickable(
                enabled = true,
                onPressed = {},
                onClick = { onClick(!checked) },
                pressDepth = 0.95f,
            )
            .clip(RoundedCornerShape(6.dp))
            .background(color = background),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.padding(
                horizontal = 6.dp,
                vertical = 8.dp,
            ),
            painter = painterResource(id = R.drawable.ic_check),
            contentDescription = "check",
            tint = tint
        )
    }
}
