package team.retum.recruitment.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.recruitment.ui.Recruitments

const val NAVIGATION_RECRUITMENTS = "recruitments"

fun NavGraphBuilder.recruitments(onRecruitmentDetailClick: () -> Unit) {
    composable(NAVIGATION_RECRUITMENTS) {
        Recruitments(onRecruitmentDetailClick = onRecruitmentDetailClick)
    }
}
