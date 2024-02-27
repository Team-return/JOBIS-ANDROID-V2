package team.retum.data.repository.student

import team.retum.network.datasource.student.RemoteStudentDataSource
import javax.inject.Inject

class StudentRepositoryImpl @Inject constructor(
    private val remoteStudentDataSource: RemoteStudentDataSource,
) : StudentRepository {
    override suspend fun checkStudentExists(
        gcn: String,
        name: String,
    ) {
        remoteStudentDataSource.checkStudentExists(
            gcn = gcn,
            name = name,
        )
    }
}
