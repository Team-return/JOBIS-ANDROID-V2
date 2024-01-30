package team.retum.network.util

import retrofit2.HttpException
import team.retum.network.exception.BadRequestException
import team.retum.network.exception.ConflictException
import team.retum.network.exception.ForbiddenException
import team.retum.network.exception.MethodNotAllowedException
import team.retum.network.exception.NotFoundException
import team.retum.network.exception.ServerException
import team.retum.network.exception.TooManyRequestException
import team.retum.network.exception.UnAuthorizedException

class RequestHandler<T> {
    suspend fun request(block: suspend () -> T): T =
        try {
            block()
        } catch (e: HttpException) {
            throw when (e.code()) {
                400 -> BadRequestException
                401 -> UnAuthorizedException
                403 -> ForbiddenException
                404 -> NotFoundException
                405 -> MethodNotAllowedException
                409 -> ConflictException
                429 -> TooManyRequestException
                in 500..599 -> ServerException
                else -> e
            }
        } catch (e: Throwable) {
            throw e
        }
}
