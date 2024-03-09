package team.retum.jobisandroidv2.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import team.retum.bug.navigateToReportBug
import team.retum.bug.reportBug
import team.retum.company.navigation.companies
import team.retum.company.navigation.companyDetails
import team.retum.company.navigation.navigateToCompanies
import team.retum.company.navigation.navigateToCompanyDetails
import team.retum.company.navigation.navigateToSearchCompanies
import team.retum.company.navigation.searchCompanies
import team.retum.jobis.application.navigation.application
import team.retum.jobis.application.navigation.navigateToApplication
import team.retum.jobis.change.password.navigation.navigateToComparePassword
import team.retum.jobis.interests.navigation.interests
import team.retum.jobis.interests.navigation.navigateToInterests
import team.retum.jobis.notice.navigation.navigateToNotices
import team.retum.jobis.notice.navigation.noticeDetails
import team.retum.jobis.notice.navigation.notices
import team.retum.jobis.recruitment.navigation.navigateToRecruitmentDetails
import team.retum.jobis.recruitment.navigation.navigateToRecruitmentFilter
import team.retum.jobis.recruitment.navigation.navigateToSearchRecruitment
import team.retum.jobis.recruitment.navigation.recruitmentDetails
import team.retum.jobis.recruitment.navigation.recruitmentFilter
import team.retum.jobis.recruitment.navigation.searchRecruitment
import team.retum.jobisandroidv2.root.NAVIGATION_ROOT
import team.retum.jobisandroidv2.root.root
import team.retum.landing.navigation.navigateToLanding
import team.retum.notification.navigation.navigateToNotification
import team.retum.notification.navigation.notifications
import team.retum.review.navigation.navigateToPostReview
import team.retum.review.navigation.postReview

const val NAVIGATION_MAIN = "main"

fun NavGraphBuilder.mainNavigation(navController: NavHostController) {
    navigation(
        route = NAVIGATION_MAIN,
        startDestination = NAVIGATION_ROOT,
    ) {
        root(
            onAlarmClick = navController::navigateToNotification,
            onRecruitmentDetailsClick = navController::navigateToRecruitmentDetails,
            onCompaniesClick = navController::navigateToCompanies,
            onRecruitmentFilterClick = navController::navigateToRecruitmentFilter,
            onSelectInterestClick = navController::navigateToInterests,
            onChangePasswordClick = navController::navigateToComparePassword,
            onReportBugClick = navController::navigateToReportBug,
            onSearchRecruitmentClick = navController::navigateToSearchRecruitment,
            onNoticeClick = navController::navigateToNotices,
            navigateToLanding = { navController.navigateToLanding(NAVIGATION_ROOT) },
            onPostReviewClick = navController::navigateToPostReview,
        )
        notifications(
            onBackPressed = navController::popBackStack,
            navigateToDetail = navController::navigateToRecruitmentDetails,
        )
        recruitmentDetails(
            onBackPressed = navController::navigateUp,
            onApplyClick = navController::navigateToApplication,
        )
        reportBug(onBackPressed = navController::popBackStack)
        interests(onBackPressed = navController::popBackStack)
        noticeDetails(onBackPressed = navController::navigateUp)
        companies(
            onBackPressed = navController::popBackStack,
            onSearchClick = navController::navigateToSearchCompanies,
            onCompanyContentClick = navController::navigateToCompanyDetails,
        )
        searchCompanies(onBackPressed = navController::popBackStack)
        recruitmentFilter(onBackPressed = navController::popBackStack)
        searchRecruitment(
            onBackPressed = navController::popBackStack,
            onRecruitmentDetailsClick = navController::navigateToRecruitmentDetails,
        )
        application(onBackPressed = navController::popBackStack)
        notices(onBackPressed = navController::popBackStack)
        postReview(onBackPressed = navController::popBackStack)
        companyDetails(onBackPressed = navController::popBackStack)
    }
}
