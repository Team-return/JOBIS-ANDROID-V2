package team.retum.data.repository

interface ServerStatusCheckRepository {
    suspend fun serverStatusCheck()
}
