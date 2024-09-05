package team.retum.jobisandroidv2.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import team.retum.bug.navigation.reportBug
import team.retum.company.navigation.companies
import team.retum.company.navigation.companyDetails
import team.retum.company.navigation.searchCompanies
import team.retum.jobis.application.navigation.application
import team.retum.jobis.interests.navigation.interests
import team.retum.jobis.notice.navigation.noticeDetails
import team.retum.jobis.notice.navigation.notices
import team.retum.jobis.recruitment.navigation.recruitmentDetails
import team.retum.jobis.recruitment.navigation.recruitmentFilter
import team.retum.jobis.recruitment.navigation.searchRecruitment
import team.retum.jobisandroidv2.JobisNavigator
import team.retum.jobisandroidv2.root.NAVIGATION_ROOT
import team.retum.jobisandroidv2.root.root
import team.retum.notification.navigation.notifications
import team.retum.review.navigation.postReview
import team.retum.review.navigation.reviewDetails
import team.retum.review.navigation.reviews

const val NAVIGATION_MAIN = "main"

internal fun NavGraphBuilder.mainNavigation(
    navigator: JobisNavigator,
) {
    navigation(
        route = NAVIGATION_MAIN,
        startDestination = NAVIGATION_ROOT,
    ) {
        root(
            onAlarmClick = navigator::navigateToNotification,
            onRecruitmentDetailsClick = navigator::navigateToRecruitmentDetails,
            onCompaniesClick = navigator::navigateToCompanies,
            onRecruitmentFilterClick = navigator::navigateToRecruitmentFilter,
            onSelectInterestClick = navigator::navigateToInterests,
            onChangePasswordClick = navigator::navigateToComparePassword,
            onReportBugClick = navigator::navigateToReportBug,
            onSearchRecruitmentClick = navigator::navigateToSearchRecruitment,
            onNoticeClick = navigator::navigateToNotices,
            navigateToLanding = { navigator.navigateToLanding(NAVIGATION_ROOT) },
            onPostReviewClick = navigator::navigateToPostReview,
            navigateToApplication = navigator::navigateToApplication,
            navigateToRecruitmentDetails = navigator::navigateToRecruitmentDetails,
            navigatedFromNotifications = navigator.navigatedFromNotifications(),
        )
        notificationSetting(onBackPressed = navController::popBackStack)
        notifications(
            onBackPressed = {
                navigator.navigateToRoot()
            },
            navigateToRecruitment = navigator::navigateToRecruitmentDetails,
            navigateToHome = navigator::navigateToRoot,
        )
        recruitmentDetails(
            onBackPressed = navigator::popBackStackIfNotHome,
            onApplyClick = navigator::navigateToApplication,
            navigateToCompanyDetails = navigator::navigateToCompanyDetails,
        )
        reportBug(onBackPressed = navigator::popBackStackIfNotHome)
        interests(onBackPressed = navigator::popBackStackIfNotHome)
        noticeDetails(onBackPressed = navigator::popBackStackIfNotHome)
        companies(
            onBackPressed = navigator::popBackStackIfNotHome,
            onSearchClick = navigator::navigateToSearchCompanies,
            onCompanyContentClick = navigator::navigateToCompanyDetails,
        )
        searchCompanies(
            onBackPressed = navigator::popBackStackIfNotHome,
            onCompanyContentClick = navigator::navigateToCompanyDetails,
        )
        recruitmentFilter(onBackPressed = navigator::popBackStackIfNotHome)
        searchRecruitment(
            onBackPressed = navigator::popBackStackIfNotHome,
            onRecruitmentDetailsClick = navigator::navigateToRecruitmentDetails,
        )
        application(onBackPressed = navigator::popBackStackIfNotHome)
        notices(
            onBackPressed = navigator::popBackStackIfNotHome,
            navigateToDetails = navigator::navigateToNoticeDetails,
        )
        postReview(onBackPressed = navigator::popBackStackIfNotHome)
        companyDetails(
            onBackPressed = navigator::popBackStackIfNotHome,
            navigateToReviewDetails = navigator::navigateToReviewDetails,
            navigateToReviews = navigator::navigateToReviews,
            navigateToRecruitmentDetails = navigator::navigateToRecruitmentDetails,
        )
        reviewDetails(navigator::popBackStackIfNotHome)
        reviews(
            navigator::popBackStackIfNotHome,
            navigateToReviewDetails = navigator::navigateToReviewDetails,
        )
    }
}
