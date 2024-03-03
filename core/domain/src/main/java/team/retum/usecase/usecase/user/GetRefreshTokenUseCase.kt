package team.retum.usecase.usecase.user

import team.retum.jobis.local.datasource.user.LocalUserDataSource
import javax.inject.Inject

class GetRefreshTokenUseCase @Inject constructor(
    private val localUserDataSource: LocalUserDataSource,
) {
    operator fun invoke() = runCatching {
        localUserDataSource.getRefreshToken()
    }
}
