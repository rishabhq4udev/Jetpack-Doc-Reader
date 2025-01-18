package com.example.jetpackpdfreader.domain.model

sealed class FetchStatus {
    object Idle : FetchStatus()
    object Fetching : FetchStatus()
    object Completed : FetchStatus()
    data class Error(val message: String) : FetchStatus()
}