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
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

// ViewModel provides HomeScreen with:
// uiState   → determines what to display
// functions → handles user actions
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
                _uiState.value = AqiUiState.Success(data)
            } catch (e: UnknownHostException) {
                _uiState.value = AqiUiState.NoInternet
            } catch (e: SocketTimeoutException) {
                _uiState.value = AqiUiState.NoInternet
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
            } catch (e: UnknownHostException) {
                _uiState.value = AqiUiState.NoInternet
            } catch (e: SocketTimeoutException) {
                _uiState.value = AqiUiState.NoInternet
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