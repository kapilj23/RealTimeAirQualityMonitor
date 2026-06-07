package com.kapil.aqiapp.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kapil.aqiapp.presentation.viewmodel.AqiUiState
import com.kapil.aqiapp.presentation.viewmodel.AqiViewModel

@Composable
fun HomeScreen(
    viewModel: AqiViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAqi("bareilly")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (uiState) {
            is AqiUiState.Loading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Fetching AQI...")
            }
            is AqiUiState.Success -> {
                val data = (uiState as AqiUiState.Success).data
                Text(
                    text = data.cityName,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "AQI: ${data.aqi}",
                    style = MaterialTheme.typography.displayLarge
                )
            }
            is AqiUiState.Error -> {
                val message = (uiState as AqiUiState.Error).message
                Text("Error: $message")
            }
        }
    }
}