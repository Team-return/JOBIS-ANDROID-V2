package team.retum.usecase.usecase.user

import team.retum.jobis.local.datasource.user.LocalUserDataSource
import javax.inject.Inject

class GetRefreshExpiresAtUseCase @Inject constructor(
    private val localUserDataSource: LocalUserDataSource,
) {
    operator fun invoke() = runCatching {
        localUserDataSource.getRefreshExpiresAt()
    }
}
