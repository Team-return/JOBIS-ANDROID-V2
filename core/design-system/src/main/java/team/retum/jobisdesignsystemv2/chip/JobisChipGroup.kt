package team.retum.jobisdesignsystemv2.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText

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
