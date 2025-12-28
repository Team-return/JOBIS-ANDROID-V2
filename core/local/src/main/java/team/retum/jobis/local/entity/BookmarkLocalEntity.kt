package team.retum.jobis.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkLocalEntity(
    @PrimaryKey
    val recruitmentId: Long,

    val isBookmarked: Boolean,

    val updatedAt: Long = System.currentTimeMillis()
)
