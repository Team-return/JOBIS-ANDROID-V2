package team.retum.jobis.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "codes")
data class CodeLocalEntity(
    @PrimaryKey
    val code: Long,
    val keyword: String,
    val type: String,
    val parentCode: Long?,
    val cachedAt: Long,
)
