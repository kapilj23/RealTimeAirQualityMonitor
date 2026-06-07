package com.kapil.aqiapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapil.aqiapp.domain.model.AqiData
import com.kapil.aqiapp.domain.usecase.GetAqiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AqiViewModel @Inject constructor(
    private val getAqiUseCase: GetAqiUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AqiUiState>(AqiUiState.Loading)
    val uiState: StateFlow<AqiUiState> = _uiState

    fun fetchAqi(city: String) {
        viewModelScope.launch {
            _uiState.value = AqiUiState.Loading
            try {
                val data = getAqiUseCase(city)
                _uiState.value = AqiUiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = AqiUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }
}

sealed class AqiUiState {
    object Loading : AqiUiState()
    data class Success(val data: AqiData) : AqiUiState()
    data class Error(val message: String) : AqiUiState()
}