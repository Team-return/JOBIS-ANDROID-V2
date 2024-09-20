package team.retum.data.repository.intern

interface WinterInterRepository {
    suspend fun fetchWinterIntern(): Boolean
}
