package team.retum.usecase.usecase.user

import team.retum.data.repository.user.UserRepository
import javax.inject.Inject

class GetDeviceTokenUseCase @Inject constructor(
    private val userRepository: UserRepository,
){
    suspend operator fun invoke() = runCatching {
        userRepository
    }
}
