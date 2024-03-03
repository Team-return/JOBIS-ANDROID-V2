package team.retum.usecase.usecase.user

import team.retum.data.repository.user.UserRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke() = runCatching {
        userRepository.signOut()
    }
}
