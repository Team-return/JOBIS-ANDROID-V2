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
import team.retum.employment.navigation.navigateToEmployment
import team.retum.employment.navigation.navigateToEmploymentDetail
import team.retum.jobis.application.navigation.navigateToApplication
import team.retum.jobis.change.password.navigation.navigateToComparePassword
import team.retum.jobis.change.password.navigation.navigateToResetPassword
import team.retum.jobis.interests.navigation.navigateToInterests
import team.retum.jobis.notice.navigation.navigateToNoticeDetails
import team.retum.jobis.notice.navigation.navigateToNotices
import team.retum.jobis.recruitment.navigation.navigateToRecruitmentDetails
import team.retum.jobis.recruitment.navigation.navigateToRecruitmentFilter
import team.retum.jobis.recruitment.navigation.navigateToSearchRecruitment
import team.retum.jobis.recruitment.navigation.navigateToWinterIntern
import team.retum.jobis.verify.email.navigation.navigateToVerifyEmail
import team.retum.jobisandroidv2.root.APPLICATION_ID
import team.retum.jobisandroidv2.root.NAVIGATION_ROOT
import team.retum.jobisandroidv2.root.navigateToRoot
import team.retum.landing.navigation.navigateToLanding
import team.retum.notification.navigation.NAVIGATION_NOTIFICATIONS
import team.retum.notification.navigation.navigateToNotification
import team.retum.notification.navigation.navigateToNotificationSetting
import team.retum.post.review.model.PostReviewData
import team.retum.post.review.navigation.navigateToPostExpectReview
import team.retum.post.review.navigation.navigateToPostNextReview
import team.retum.post.review.navigation.navigateToPostReview
import team.retum.post.review.navigation.navigateToPostReviewComplete
import team.retum.review.navigation.navigateToReview
import team.retum.review.navigation.navigateToReviewDetails
import team.retum.review.navigation.navigateToReviewFilter
import team.retum.review.navigation.navigateToSearchReview
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

    fun navigateToSearchRecruitment(isWinterIntern: Boolean) {
        navController.navigateToSearchRecruitment(isWinterIntern = isWinterIntern)
    }

    fun navigateToNotificationSetting() {
        navController.navigateToNotificationSetting()
    }

    fun navigateToNotices() {
        navController.navigateToNotices()
    }

    /**
     * Navigates to the landing screen using the provided pop-up route.
     *
     * @param popUpRoute The navigation route to pop up to when navigating to the landing screen.
     */
    fun navigateToLanding(popUpRoute: String) {
        navController.navigateToLanding(popUpRoute = popUpRoute)
    }

    /**
     * Navigate to the post-review screen for a specific company.
     *
     * @param companyName The company's display name to prefill or show on the post-review screen.
     * @param companyId The unique identifier of the company to associate the review with.
     */
    fun navigateToPostReview(companyName: String, companyId: Long) {
        navController.navigateToPostReview(companyName = companyName, companyId = companyId)
    }

    /**
     * Navigates to the post-next-review screen, forwarding the given review data.
     *
     * @param reviewData The `PostReviewData` to pass to the destination.
     */
    fun navigateToPostNextReview(reviewData: PostReviewData) {
        navController.navigateToPostNextReview(reviewData = reviewData)
    }

    /**
     * Navigates to the post-review completion screen.
     */
    fun navigateToPostReviewComplete() {
        navController.navigateToPostReviewComplete()
    }

    /**
     * Navigate to the post-expected-review screen with the provided review data.
     *
     * @param reviewData Data used to populate the post-expected-review screen.
     */
    fun navigateToPostExpectReview(reviewData: PostReviewData) {
        navController.navigateToPostExpectReview(reviewData = reviewData)
    }

    /**
     * Navigates to the app's root (landing/home) screen, optionally targeting a specific application.
     *
     * @param applicationId The application ID to include in the navigation target; pass 0 to navigate to the root without targeting a specific application.
     */
    fun navigateToRoot(applicationId: Long = 0) {
        navController.navigateToRoot(applicationId = applicationId)
    }

    fun navigateToEmployment() {
        navController.navigateToEmployment()
    }

    fun navigateToEmploymentDetail(classId: Long) {
        navController.navigateToEmploymentDetail(classId = classId)
    }

    fun navigateToWinterIntern() {
        navController.navigateToWinterIntern()
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

    /**
     * Navigate to the notice details screen for the specified notice.
     *
     * @param noticeId The identifier of the notice to display.
     */
    fun navigateToNoticeDetails(noticeId: Long) {
        navController.navigateToNoticeDetails(noticeId = noticeId)
    }

    /**
     * Navigates to the review details screen for the specified review.
     *
     * @param reviewId The identifier of the review to display.
     */
    fun navigateToReviewDetails(reviewId: Long) {
        navController.navigateToReviewDetails(reviewId = reviewId)
    }

    /**
     * Navigates to the review search screen.
     */
    fun navigateToSearchReview() {
        navController.navigateToSearchReview()
    }

    /**
     * Navigates to the review filter screen.
     */
    fun navigateToReviewFilter() {
        navController.navigateToReviewFilter()
    }

    /**
     * Navigates to the review screen for the given company.
     *
     * @param companyId The company's unique identifier.
     * @param companyName The company's display name.
     */
    fun navigateToReview(
        companyId: Long,
        companyName: String,
    ) {
        navController.navigateToReview()
    }

    /**
     * Determine whether navigation originated from the notifications screen.
     *
     * @return `true` if the previous back-stack entry's route equals `NAVIGATION_NOTIFICATIONS`, `false` otherwise.
     */
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