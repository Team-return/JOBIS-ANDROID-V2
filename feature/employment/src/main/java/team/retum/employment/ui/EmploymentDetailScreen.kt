package team.retum.employment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import team.retum.employment.R
import team.retum.employment.viewmodel.EmploymentDetailViewModel
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.usecase.entity.application.EmploymentStatusEntity

const val MAX_STUDENT = 16

@Composable
internal fun EmploymentDetail(
    classId: Long,
    onBackPressed: () -> Unit,
    employmentDetailViewModel: EmploymentDetailViewModel = hiltViewModel(),
) {
    val state by employmentDetailViewModel.state.collectAsStateWithLifecycle()
    val classNameList = listOf(stringResource(R.string.soft_ware_first_class), stringResource(R.string.soft_ware_second_class), stringResource(R.string.soft_ware_embedded), stringResource(R.string.ai_class))

    LaunchedEffect(Unit) {
        with(employmentDetailViewModel) {
            setClassId(classId = classId.toInt() - 1)
            fetchEmploymentStatus()
        }
    }

    EmploymentDetailScreen(
        classId = classId,
        passStudent = state.passStudent,
        totalStudent = state.totalStudent,
        classNameList = classNameList,
        classInfoList = state.classInfoList.toMutableList(),
        onBackPressed = onBackPressed,
    )
}

@Composable
private fun EmploymentDetailScreen(
    classId: Long,
    passStudent: Int,
    totalStudent: Int,
    classNameList: List<String>,
    classInfoList: MutableList<EmploymentStatusEntity.ClassEmploymentStatusEntity.GetEmploymentRateList>,
    onBackPressed: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(
            title = classNameList[classId.toInt() - 1],
            onBackPressed = onBackPressed,
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            contentPadding = PaddingValues(top = 32.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
        ) {
            classInfoList.apply {
                repeat(MAX_STUDENT - passStudent) {
                    add(
                        EmploymentStatusEntity.ClassEmploymentStatusEntity.GetEmploymentRateList(
                            id = 0,
                            companyName = "",
                            logoUrl = "",
                        ),
                    )
                }
            }
            if (classInfoList.isNotEmpty()) {
                items(items = classInfoList) { company ->
                    CompanyCard(
                        companyName = company.companyName,
                        imageUrl = company.logoUrl,
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            JobisText(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 24.dp),
                text = "$passStudent/$totalStudent",
                style = JobisTypography.Body,
                color = JobisTheme.colors.onPrimary,
            )
        }
    }
}

@Composable
private fun CompanyCard(
    imageUrl: String,
    companyName: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .shadow(elevation = 18.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(color = JobisTheme.colors.inverseSurface),
        ) {
            AsyncImage(
                modifier = Modifier
                    .align(Alignment.Center),
                model = imageUrl,
                contentDescription = companyName,
                contentScale = ContentScale.Crop,
            )
        }
        JobisText(
            modifier = Modifier
                .padding(top = 8.dp)
                .basicMarquee(),
            text = companyName,
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}
