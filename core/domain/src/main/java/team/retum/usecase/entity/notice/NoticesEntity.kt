package team.retum.usecase.entity.notice

import android.annotation.SuppressLint
import androidx.compose.runtime.Immutable
import team.retum.network.model.response.notice.FetchNoticesResponse
import java.text.SimpleDateFormat

data class NoticesEntity(
    val notices: List<NoticeEntity>,
) {
    @Immutable
    data class NoticeEntity(
        val id: Long,
        val title: String,
        val createdAt: String,
    )
}

internal fun FetchNoticesResponse.toNoticesEntity() = NoticesEntity(
    notices = this.notices.map { it.toEntity() },
)

@SuppressLint("SimpleDateFormat")
private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

@SuppressLint("SimpleDateFormat")
private val outputFormat = SimpleDateFormat("yyyy-MM-dd")

private fun FetchNoticesResponse.NoticeResponse.toEntity() =
    NoticesEntity.NoticeEntity(
        id = this.id,
        title = this.title,
        createdAt = outputFormat.format(inputFormat.parse(this.createdAt)!!),
    )
