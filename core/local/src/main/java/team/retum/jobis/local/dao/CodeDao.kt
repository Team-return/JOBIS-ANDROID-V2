package team.retum.jobis.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import team.retum.jobis.local.entity.CodeLocalEntity

@Dao
interface CodeDao {

    @Query("SELECT * FROM codes WHERE type = :type AND parentCode IS :parentCode")
    suspend fun getCodes(
        type: String,
        parentCode: Long?,
    ): List<CodeLocalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(codes: List<CodeLocalEntity>)

    @Query("DELETE FROM codes WHERE type = :type AND parentCode IS :parentCode")
    suspend fun deleteByGroup(
        type: String,
        parentCode: Long?,
    )

    @Query("DELETE FROM codes")
    suspend fun deleteAll()
}
