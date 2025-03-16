package com.sohail.errorhandling.domain

sealed interface DataError : Error {
    enum class Network: DataError{
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        SERVER_ERROR,
        NO_INTERNET_CONNECTION,
        UNKNOWN_ERROR
    }
    enum class Local: DataError{
        DISK_FULL
    }
}