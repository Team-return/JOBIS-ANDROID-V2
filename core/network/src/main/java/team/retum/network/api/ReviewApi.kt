package team.retum.network.api

import retrofit2.http.Body
import retrofit2.http.POST
import team.retum.network.di.RequestUrls
import team.retum.network.model.request.PostReviewRequest

interface ReviewApi {
    @POST(RequestUrls.Reviews.post)
    suspend fun postReview(
        @Body reviewRequest: PostReviewRequest,
    )
}