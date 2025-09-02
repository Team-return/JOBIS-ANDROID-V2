package team.retum.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchQuestionsResponse(
    @Json(name = "questions") val questions: List<Question>
) {
    @JsonClass(generateAdapter = true)
    data class Question(
        @Json(name = "id") val id: Long,
        @Json(name = "question") val question: String,
    )
}
