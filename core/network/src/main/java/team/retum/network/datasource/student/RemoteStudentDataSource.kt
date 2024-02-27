package team.retum.network.datasource.student

interface RemoteStudentDataSource {
    suspend fun checkStudentExists(
        gcn: String,
        name: String,
    )
}
