package com.sohail.errorhandling.presentation

import com.sohail.errorhandling.R
import com.sohail.errorhandling.domain.DataError
import com.sohail.errorhandling.domain.Result


fun DataError.asUiText(): UiText {
    return when (this) {
        DataError.Network.REQUEST_TIMEOUT -> UiText.StringResource(
            R.string.the_request_timed_out
        )

        DataError.Network.TOO_MANY_REQUESTS -> UiText.StringResource(
            R.string.youve_hit_your_rate_limit
        )

        DataError.Network.SERVER_ERROR -> UiText.StringResource(
            R.string.server_error
        )

        DataError.Local.DISK_FULL -> UiText.StringResource(
            R.string.error_disk_full
        )

        DataError.Network.NO_INTERNET_CONNECTION -> TODO()
        DataError.Network.UNKNOWN_ERROR -> TODO()
    }
}

fun Result.Error<*, DataError>.asErrorUiText(): UiText {
    return error.asUiText()
}