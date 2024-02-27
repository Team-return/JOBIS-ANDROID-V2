package team.retum.data.repository.student

interface StudentRepository {
    suspend fun checkStudentExists(
        gcn: String,
        name: String,
    )
}
