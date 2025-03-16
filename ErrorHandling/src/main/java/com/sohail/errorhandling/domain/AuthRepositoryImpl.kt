package com.sohail.errorhandling.domain

class AuthRepositoryImpl : AuthRepository {
    override suspend fun register(password: String): Result<User, DataError.Network> {
        //Api call logic
        return try {
            val user = User("", "", "")
            Result.Success(user)
        } catch (e: Exception) {
            return when (e.hashCode()) {
                408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
                413 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
                else -> Result.Error(DataError.Network.UNKNOWN_ERROR)
            }
        }
    }
}