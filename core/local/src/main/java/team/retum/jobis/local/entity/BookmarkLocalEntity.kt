package team.retum.jobis.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkLocalEntity(
    @PrimaryKey
    val recruitmentId: Long,

    val companyLogoUrl: String = "",

    val companyName: String = "",

    val createdAt: String = "",

    val isBookmarked: Boolean,
)
