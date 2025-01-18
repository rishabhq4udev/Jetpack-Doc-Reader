package com.example.jetpackpdfreader.data.local.repository

import com.example.jetpackpdfreader.domain.model.DocData
import com.example.jetpackpdfreader.domain.model.FetchStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface DocRepository {
    fun fetchFiles(fileType: String): Flow<List<DocData>>
    val fetchingStatus: StateFlow<FetchStatus>
}