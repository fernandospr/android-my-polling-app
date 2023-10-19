package com.github.fernandospr.mypollingapplication

import kotlinx.coroutines.delay

data class Response<T>(
    val transaction: Transaction,
    val payload: T? = null
)

data class Transaction(
    val code: String,
    val status: String,
    val retry: Int,
    val timeout: Long
)

enum class TransactionStatus {
    CREATED, APPROVED, REJECTED, PENDING
}

sealed class Result<out T> {
    data class Approved<T>(val payload: T?) : Result<T>()
    object Pending : Result<Nothing>()
    object Rejected : Result<Nothing>()
}

suspend fun <T> Response<Unit>.withPolling(
    statusRequest: suspend (code: String) -> Response<T>
): Result<T> {
    val code = transaction.code
    val retries = transaction.retry
    val timeout = transaction.timeout
    for (i in 0 until retries) {
        // TODO: Should try/catch statusRequest result to continue retrying?
        val statusResponse = statusRequest(code)
        if (TransactionStatus.APPROVED.name.equals(statusResponse.transaction.status, true)) {
            return Result.Approved(statusResponse.payload)
        } else if (TransactionStatus.REJECTED.name.equals(statusResponse.transaction.status, true)) {
            return Result.Rejected
        }
        if (i < retries - 1) {
            delay(timeout)
        }
    }
    return Result.Pending
}