package team.retum.jobisdesignsystemv2.skills

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import team.retum.jobisdesignsystemv2.checkbox.JobisCheckBox
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable

@Composable
fun Skills(
    skills: SnapshotStateList<String>,
    checkSkillsId: ImmutableList<Long>? = null,
    checkedSkills: ImmutableList<String>,
    onCheckedChange: (String, Boolean, Long) -> Unit,
) {
    LazyColumn {
        items(skills.size) { skill ->
            val checked = checkedSkills.contains(skills[skill])
            SkillContent(
                skill = skills[skill],
                checked = checked,
                onClick = { onCheckedChange(skills[skill], it, checkSkillsId?.get(skill) ?: 0) },
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
        JobisCheckBox(
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
