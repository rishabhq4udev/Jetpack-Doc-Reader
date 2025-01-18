package com.example.jetpackpdfreader.presentation.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jetpackpdfreader.domain.model.FetchStatus
import com.example.jetpackpdfreader.presentation.viewmodel.DocViewModel

@Composable
fun HomeScreen(docViewModel: DocViewModel, modifier: Modifier = Modifier) {
    val stateFlow = docViewModel.state.collectAsStateWithLifecycle()
    val state = stateFlow.value
    when (state.fetchStatus) {
        is FetchStatus.Fetching -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is FetchStatus.Completed -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    state.files,
                    key = { it.mediaPath }
                ) { file ->
                    FileListItem(
                        file = file,
                        onFileClick = { /* Handle file click */ }
                    )
                }
            }
        }

        else -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No files found")
            }
        }
    }
}