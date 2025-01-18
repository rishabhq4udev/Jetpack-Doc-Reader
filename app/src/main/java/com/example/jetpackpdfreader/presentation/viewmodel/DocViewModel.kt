package com.example.jetpackpdfreader.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackpdfreader.data.local.repository.DocRepository
import com.example.jetpackpdfreader.presentation.state.DocViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocViewModel @Inject constructor(
    private val docRepository: DocRepository,
    private val savedStateHandle : SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DocViewState())
    val state = _state.asStateFlow()


    init {
        observeFetchStatus()
    }
    private fun observeFetchStatus() {
        viewModelScope.launch {
            docRepository.fetchingStatus.collect { status ->
                _state.update { it.copy(fetchStatus = status) }
            }
        }
    }

    fun fetchFiles(fileType: String) {
        viewModelScope.launch {
            try {
                docRepository.fetchFiles(fileType)
                    .catch { error ->
                        _state.update {
                            it.copy(error = error.message)
                        }
                    }
                    .collect { files ->
                        _state.update {
                            it.copy(
                                files = files,
                                error = null
                            )
                        }
                    }
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message)
                }
            }
        }
    }
}