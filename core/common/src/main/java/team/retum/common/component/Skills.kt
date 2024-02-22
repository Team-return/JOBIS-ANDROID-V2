package team.retum.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import team.retum.jobisdesignsystemv2.checkbox.CheckBox
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable

@Composable
fun Skills(
    skills: SnapshotStateList<String>,
    checkedSkills: List<String>,
    onCheckedChange: (String, Boolean) -> Unit,
) {
    LazyColumn {
        items(skills) { skill ->
            val checked = checkedSkills.contains(skill)
            SkillContent(
                skill = skill,
                checked = checked,
                onClick = { onCheckedChange(skill, it) },
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
                onClick = { onClick(!checked) },
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
