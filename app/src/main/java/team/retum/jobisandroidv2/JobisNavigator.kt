package team.retum.jobisandroidv2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import team.retum.bug.navigation.navigateToReportBug
import team.retum.common.enums.ResetPasswordNavigationArgumentType
import team.retum.common.model.ApplicationData
import team.retum.company.navigation.navigateToCompanies
import team.retum.company.navigation.navigateToCompanyDetails
import team.retum.company.navigation.navigateToSearchCompanies
import team.retum.jobis.application.navigation.navigateToApplication
import team.retum.jobis.change.password.navigation.navigateToComparePassword
import team.retum.jobis.change.password.navigation.navigateToResetPassword
import team.retum.jobis.interests.navigation.navigateToInterests
import team.retum.jobis.notice.navigation.navigateToNoticeDetails
import team.retum.jobis.notice.navigation.navigateToNotices
import team.retum.jobis.recruitment.navigation.navigateToRecruitmentDetails
import team.retum.jobis.recruitment.navigation.navigateToRecruitmentFilter
import team.retum.jobis.recruitment.navigation.navigateToSearchRecruitment
import team.retum.jobis.verify.email.navigation.navigateToVerifyEmail
import team.retum.jobisandroidv2.root.APPLICATION_ID
import team.retum.jobisandroidv2.root.NAVIGATION_ROOT
import team.retum.jobisandroidv2.root.navigateToRoot
import team.retum.landing.navigation.navigateToLanding
import team.retum.notification.navigation.NAVIGATION_NOTIFICATIONS
import team.retum.notification.navigation.navigateToNotification
import team.retum.notification.navigation.navigateToNotificationSetting
import team.retum.review.navigation.navigateToPostReview
import team.retum.review.navigation.navigateToReviewDetails
import team.retum.review.navigation.navigateToReviews
import team.retum.signin.navigation.navigateToSignIn
import team.retum.signup.model.SignUpData
import team.retum.signup.navigation.navigateToInputEmail
import team.retum.signup.navigation.navigateToSelectGender
import team.retum.signup.navigation.navigateToSetPassword
import team.retum.signup.navigation.navigateToSetProfile
import team.retum.signup.navigation.navigateToSignUp
import team.retum.signup.navigation.navigateToTerms

internal class JobisNavigator(
    val navController: NavHostController,
) {

    fun navigateToSignIn() {
        navController.navigateToSignIn()
    }

    fun navigateToSignUp() {
        navController.navigateToSignUp()
    }

    fun navigateToVerifyEmail() {
        navController.navigateToVerifyEmail()
    }

    fun navigateToInputEmail(signUpData: SignUpData) {
        navController.navigateToInputEmail(signUpData = signUpData)
    }

    fun navigateToSetPassword(signUpData: SignUpData) {
        navController.navigateToSetPassword(signUpData = signUpData)
    }

    fun navigateToSelectGender(signUpData: SignUpData) {
        navController.navigateToSelectGender(signUpData = signUpData)
    }

    fun navigateToSetProfile(signUpData: SignUpData) {
        navController.navigateToSetProfile(signUpData = signUpData)
    }

    fun navigateToTerms(signUpData: SignUpData) {
        navController.navigateToTerms(signUpData = signUpData)
    }

    fun navigateToResetPassword(
        type: ResetPasswordNavigationArgumentType,
        value: String,
    ) {
        navController.navigateToResetPassword(
            type = type,
            value = value,
        )
    }

    fun navigateToNotification() {
        navController.navigateToNotification()
    }

    fun navigateToCompanies() {
        navController.navigateToCompanies()
    }

    fun navigateToRecruitmentFilter() {
        navController.navigateToRecruitmentFilter()
    }

    fun navigateToInterests() {
        navController.navigateToInterests()
    }

    fun navigateToComparePassword() {
        navController.navigateToComparePassword()
    }

    fun navigateToReportBug() {
        navController.navigateToReportBug()
    }

    fun navigateToSearchRecruitment() {
        navController.navigateToSearchRecruitment()
    }

    fun navigateToNotificationSetting() {
        navController.navigateToNotificationSetting()
    }

    fun navigateToNotices() {
        navController.navigateToNotices()
    }

    fun navigateToLanding(popUpRoute: String) {
        navController.navigateToLanding(popUpRoute = popUpRoute)
    }

    fun navigateToPostReview(companyId: Long) {
        navController.navigateToPostReview(companyId = companyId)
    }

    fun navigateToRoot(applicationId: Long = 0) {
        navController.navigateToRoot(applicationId = applicationId)
    }

    fun navigateToRecruitmentDetails(
        recruitmentId: Long,
        isMovedCompanyDetails: Boolean = false,
    ) {
        navController.navigateToRecruitmentDetails(
            recruitmentId = recruitmentId,
            isMovedCompanyDetails = isMovedCompanyDetails,
        )
    }

    fun navigateToApplication(applicationData: ApplicationData) {
        navController.navigateToApplication(applicationData = applicationData)
    }

    fun navigateToCompanyDetails(
        companyId: Long,
        isMovedRecruitmentDetails: Boolean = false,
    ) {
        navController.navigateToCompanyDetails(
            companyId = companyId,
            isMovedRecruitmentDetails = isMovedRecruitmentDetails,
        )
    }

    fun navigateToSearchCompanies() {
        navController.navigateToSearchCompanies()
    }

    fun navigateToNoticeDetails(noticeId: Long) {
        navController.navigateToNoticeDetails(noticeId = noticeId)
    }

    fun navigateToReviewDetails(
        reviewId: String,
        writer: String,
    ) {
        navController.navigateToReviewDetails(
            reviewId = reviewId,
            writer = writer,
        )
    }

    fun navigateToReviews(
        companyId: Long,
        companyName: String,
    ) {
        navController.navigateToReviews(
            companyId = companyId,
            companyName = companyName,
        )
    }

    fun navigatedFromNotifications(): Boolean {
        return navController.previousBackStackEntry?.destination?.route == NAVIGATION_NOTIFICATIONS
    }

    private fun popBackStack() {
        navController.popBackStack()
    }

    fun popBackStackIfNotHome() {
        if (!isSameCurrentDestination()) {
            popBackStack()
        }
    }

    private fun isSameCurrentDestination(): Boolean {
        return navController.currentDestination?.route == "$NAVIGATION_ROOT{$APPLICATION_ID}"
    }
}

@Composable
internal fun rememberJobisNavigator(
    navController: NavHostController = rememberNavController(),
): JobisNavigator = remember(navController) {
    JobisNavigator(navController)
}
