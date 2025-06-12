package com.sujeong.pillo.domain.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

fun <Source, Destination> DataResult<Source>.mapResult(
    mapper: (Source) -> Destination
): DataResult<Destination> = when (this) {
    is DataResult.Success -> DataResult.success(mapper(this.data))
    is DataResult.Error -> DataResult.error(this.error)
}

fun <Source, Destination> Flow<DataResult<Source>>.mapResult(
    mapper: (Source) -> Destination
): Flow<DataResult<Destination>> = map {
    when (it) {
        is DataResult.Success -> DataResult.success(mapper(it.data))
        is DataResult.Error -> DataResult.error(it.error)
    }
}

fun <T> DataResult<T>.onSuccess(action: (T) -> Unit): DataResult<T> {
    if (this is DataResult.Success) action(data)
    return this
}

fun <T> DataResult<T>.onError(action: (Throwable) -> Unit): DataResult<T> {
    if (this is DataResult.Error) action(error)
    return this
}

fun <T> Flow<DataResult<T>>.onError(action: (Throwable) -> Unit): Flow<DataResult<T>> =
    onEach { resource ->
        if (resource is DataResult.Error) {
            action(resource.error)
        }
    }