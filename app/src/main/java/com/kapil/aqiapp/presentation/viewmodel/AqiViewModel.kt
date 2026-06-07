package com.kapil.aqiapp.presentation.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapil.aqiapp.data.remote.LocationHelper
import com.kapil.aqiapp.domain.usecase.GetAqiByLocationUseCase
import com.kapil.aqiapp.domain.usecase.GetAqiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AqiViewModel @Inject constructor(
    private val getAqiUseCase: GetAqiUseCase,
    private val getAqiByLocationUseCase: GetAqiByLocationUseCase,
    private val locationHelper: LocationHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow<AqiUiState>(AqiUiState.Loading)
    val uiState: StateFlow<AqiUiState> = _uiState

    private val _locationPermissionNeeded = MutableStateFlow(false)
    val locationPermissionNeeded: StateFlow<Boolean> = _locationPermissionNeeded

    fun fetchAqi(city: String) {
        viewModelScope.launch {
            _uiState.value = AqiUiState.Loading
            try {
                val data = getAqiUseCase(city)
                android.util.Log.d("AQI", "Forecast size: ${data.forecast.size}")
                android.util.Log.d("AQI", "Forecast: ${data.forecast}")
                _uiState.value = AqiUiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = AqiUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun fetchAqiByLocation() {
        viewModelScope.launch {
            _uiState.value = AqiUiState.Loading
            try {
                val (lat, lng) = locationHelper.getCurrentLatLng()
                val data = getAqiByLocationUseCase(lat, lng)
                _uiState.value = AqiUiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = AqiUiState.Error(e.message ?: "Location fetch failed")
            }
        }
    }

    fun onLocationPermissionGranted() {
        _locationPermissionNeeded.value = false
        fetchAqiByLocation()
    }

    fun onLocationPermissionDenied() {
        _locationPermissionNeeded.value = false
        fetchAqi("bareilly")
    }
}