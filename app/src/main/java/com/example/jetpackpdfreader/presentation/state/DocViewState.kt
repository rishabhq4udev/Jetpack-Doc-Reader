package com.example.jetpackpdfreader.presentation.state

import com.example.jetpackpdfreader.domain.model.DocData
import com.example.jetpackpdfreader.domain.model.FetchStatus

data class DocViewState(
    val files: List<DocData> = emptyList(),
    val fetchStatus: FetchStatus = FetchStatus.Idle,
    val error: String? = null
)
