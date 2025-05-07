package team.retum.jobis.interests.ui

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.toPersistentList
import team.retum.jobis.interests.R
import team.retum.jobis.interests.viewmodel.InterestsViewmodel
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.button.JobisDialogButton
import team.retum.jobisdesignsystemv2.button.JobisMediumButton
import team.retum.jobisdesignsystemv2.button.JobisSmallButton
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
            "iOS",
            "Android",
            "FrontEnd",
            "BackEnd",
            "Embedded",
            "Security",
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
        InterestsTitle()
        InterestsInput(
            content = { content },
            onContentChange = { content = it },
            categories = categories,
            selectedCategoryIndex = selectedCategoryIndex,
            onSelectCategory = { selectedCategoryIndex = it },
            checkedSkills = checkedSkills,
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisButton(
            text = stringResource(R.string.select_interests_button),
            color = ButtonColor.Primary,
            onClick = {},
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun InterestsInput(
    content: () -> String,
    onContentChange: (String) -> Unit,
    selectedCategoryIndex: Int,
    categories: SnapshotStateList<String>,
    onSelectCategory: (Int) -> Unit,
    checkedSkills: SnapshotStateList<String>,
) {
    FlowRow(
        modifier = Modifier.padding(
            horizontal = 24.dp,
            vertical = 36.dp,
        ),
        maxItemsInEachRow = 5,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        categories.forEach {
            MajorContent(
                major = it,
                selected = true,
                onClick = {},
            )
        }
    }
}

@Composable
private fun InterestsTitle() {
    Column(
        modifier = Modifier.padding(
            top = 34.dp,
        )
    ) {
        JobisText(
            modifier = Modifier.padding(
                top = 20.dp,
                start = 24.dp,
                end = 24.dp,
            ),
            text = stringResource(R.string.interests_select_title, "윤유섭"),
            style = JobisTypography.PageTitle,
            color = JobisTheme.colors.onBackground,
        )
        JobisText(
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 20.dp,
                start = 24.dp,
                end = 24.dp,
            ),
            text = stringResource(R.string.interests_alarm),
            style = JobisTypography.SubBody
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
                horizontal = 16.dp,
                vertical = 6.dp,
            ),
            text = major,
            style = JobisTypography.Body,
            color = textColor,
        )
    }
}
