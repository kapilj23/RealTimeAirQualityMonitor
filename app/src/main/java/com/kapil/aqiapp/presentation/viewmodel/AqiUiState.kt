package com.kapil.aqiapp.presentation.viewmodel

import com.kapil.aqiapp.domain.model.AqiData

sealed class AqiUiState {
    object Loading : AqiUiState()
    data class Success(val data: AqiData) : AqiUiState()
    data class Error(val message: String) : AqiUiState()
}