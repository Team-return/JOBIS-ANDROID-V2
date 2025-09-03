package team.retum.review.navigation.company

//const val NAVIGATION_REVIEW_DETAILS = "reviewDetails"
//const val WRITER = "writer"
//
//fun NavGraphBuilder.reviewDetails(
//    onBackPressed: () -> Unit,
//) {
//    composable(
//        route = "$NAVIGATION_REVIEW_DETAILS/{${ResourceKeys.REVIEW_ID}}/{$WRITER}",
//        arguments = listOf(
//            navArgument(ResourceKeys.REVIEW_ID) { NavType.StringType },
//            navArgument(WRITER) { NavType.StringType },
//        ),
//    ) {
//        val reviewId = it.arguments?.getString(ResourceKeys.REVIEW_ID) ?: ""
//        val writer = it.arguments?.getString(WRITER) ?: ""
//        ReviewDetails(
//            onBackPressed = onBackPressed,
//            writer = writer,
//            reviewId = reviewId,
//        )
//    }
//}
//
//fun NavController.navigateToReviewDetails(
//    reviewId: String,
//    writer: String,
//) {
//    navigate("$NAVIGATION_REVIEW_DETAILS/$reviewId/$writer")
//}
