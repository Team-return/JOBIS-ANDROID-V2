package team.retum.employment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import team.retum.employment.model.CompanyItem
import team.retum.employment.viewmodel.EmploymentDetailViewModel
import team.retum.jobis.employment.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisDesignSystemV2Theme
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText

@Composable
internal fun EmploymentDetail(
    employmentDetailViewModel: EmploymentDetailViewModel = hiltViewModel(),
) {
    val state by employmentDetailViewModel.state.collectAsStateWithLifecycle()

    EmploymentDetailScreen(
        companyList = listOf(state.companyInfo[0]),
        onBackPressed = {}
    )
}

@Composable
private fun EmploymentDetailScreen(
    companyList: List<CompanyItem>,
    onBackPressed: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(
            title = "소프트웨어 개발 1반",
            onBackPressed = onBackPressed
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            contentPadding = PaddingValues(top = 32.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
        ) {
            items(items = companyList) { company ->
                CompanyCard(
                    imageUrl = company.logoUrl,
                    companyName = company.companyName,
                )
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.End)
                .fillMaxWidth(),
        ) {
            JobisText(
                modifier = Modifier
                    .padding(vertical = 10.dp),
                text = "16/16",
                style = JobisTypography.Body,
                color = JobisTheme.colors.onPrimary
            )
        }
    }
}

@Composable
private fun CompanyCard(
    imageUrl: String,
    companyName: String
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .shadow(elevation = 4.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(color = JobisTheme.colors.inverseSurface),
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = companyName,
        )
    }
}
