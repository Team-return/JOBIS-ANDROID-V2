package team.retum.jobis.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO 캐싱 작업시 삭제
@Entity
class TestLocalEntity(
    @PrimaryKey val key: Long,
)
