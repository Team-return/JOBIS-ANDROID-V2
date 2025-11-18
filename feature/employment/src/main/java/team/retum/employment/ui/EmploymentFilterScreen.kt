package team.retum.employment.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import team.retum.employment.R
import team.retum.employment.navigation.NAVIGATION_EMPLOYMENT
import team.retum.employment.viewmodel.EmploymentViewModel
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.checkbox.JobisCheckBox
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
internal fun EmploymentFilter(
    navController: NavHostController,
    onBackPressed: () -> Unit,
) {
    val parentEntry = remember(navController) {
        navController.getBackStackEntry(NAVIGATION_EMPLOYMENT)
    }
    val employmentViewModel: EmploymentViewModel = hiltViewModel(parentEntry)
    val state by employmentViewModel.state.collectAsStateWithLifecycle()
    EmploymentFilterScreen(
        onBackPressed = onBackPressed,
        years = state.yearList.toPersistentList(),
        setYear = employmentViewModel::setYear,
    )
}

@Composable
private fun EmploymentFilterScreen(
    onBackPressed: () -> Unit,
    years: ImmutableList<String>,
    setYear: (String) -> Unit,
) {
    Column {
        JobisSmallTopAppBar(
            title = stringResource(R.string.filter_year),
            onBackPressed = onBackPressed,
        )
        years.let { year ->
            LazyColumn {
                items(year) {
                    FilterYearContent(
                        title = it,
                        checked = true,
                        onClick = { setYear(it) },
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        JobisButton(
            text = stringResource(R.string.filter_apply),
            onClick = onBackPressed,
            color = ButtonColor.Primary,
        )
    }
}

@Composable
private fun FilterYearContent(
    title: String,
    checked: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp),
    ) {
        JobisCheckBox(
            checked = checked,
            onClick = { onClick() },
        )
        Spacer(modifier = Modifier.width(8.dp))
        JobisText(
            text = title,
            style = JobisTypography.Body,
            color = JobisTheme.colors.inverseOnSurface,
            modifier = Modifier.align(Alignment.CenterVertically),
        )
    }
}
