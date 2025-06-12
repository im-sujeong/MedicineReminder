package com.sujeong.pillo.domain.result

sealed class DataResult<out T> {
    class Success<T>(val data: T) : DataResult<T>()
    class Error(val error: Throwable) : DataResult<Nothing>()

    companion object {
        fun <T> success(data: T) =
            Success(data)

        fun error(throwable: Throwable) =
            Error(throwable)
    }
}