package team.retum.network.datasource.winter

interface WinterInternDataSource {
    suspend fun fetchWinterIntern(): Boolean
}
