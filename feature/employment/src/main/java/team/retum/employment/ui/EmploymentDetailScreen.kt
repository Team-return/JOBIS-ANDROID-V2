package team.retum.employment.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import team.retum.employment.model.CompanyItem
import team.retum.employment.viewmodel.EmploymentDetailViewModel

@Composable
fun EmploymentDetail(
    employmentDetailViewModel: EmploymentDetailViewModel = hiltViewModel()
) {
    EmploymentDetailScreen(
        companyItem = emptyList()
    )
}


@Composable
fun EmploymentDetailScreen(
    companyItem: List<CompanyItem>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val arr: List<CompanyItem> = listOf(CompanyItem(
            companyName = "",
            logoUrl = ""
        ))
        LazyHorizontalGrid(
            rows = GridCells.Fixed(4)
        ) {
            items(items = arr) { person ->

            }
        }
    }
}

@Composable
fun CompanyIcon() {
    
}
