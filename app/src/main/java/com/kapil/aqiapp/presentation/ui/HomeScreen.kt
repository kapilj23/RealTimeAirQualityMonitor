import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kapil.aqiapp.presentation.viewmodel.AqiUiState
import com.kapil.aqiapp.presentation.viewmodel.AqiViewModel

@Composable
fun HomeScreen(
    viewModel: AqiViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var cityInput by remember { mutableStateOf("") }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) viewModel.onLocationPermissionGranted()
        else viewModel.onLocationPermissionDenied()
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    val aqi = if (uiState is AqiUiState.Success) {
        (uiState as AqiUiState.Success).data.aqi
    } else 0

    val aqiColor by animateColorAsState(
        targetValue = when {
            aqi <= 50 -> Color(0xFF4CAF50)
            aqi <= 100 -> Color(0xFFFFEB3B)
            aqi <= 150 -> Color(0xFFFF9800)
            aqi <= 200 -> Color(0xFFF44336)
            aqi <= 300 -> Color(0xFF9C27B0)
            else -> Color(0xFF7B1FA2)
        },
        animationSpec = tween(1000),
        label = "aqi_color"
    )

    val aqiLabel = when {
        aqi <= 50 -> "Good 😊"
        aqi <= 100 -> "Moderate 😐"
        aqi <= 150 -> "Unhealthy 😷"
        aqi <= 200 -> "Very Unhealthy 🤢"
        aqi <= 300 -> "Hazardous ☠️"
        else -> "Severe ☣️"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Top Search Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = cityInput,
                    onValueChange = { cityInput = it },
                    placeholder = {
                        Text("Search city...", color = Color.Gray, fontSize = 14.sp)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                locationPermissionLauncher.launch(
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "My Location",
                                tint = aqiColor
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = aqiColor,
                        unfocusedBorderColor = Color(0xFF3A3A4E),
                        focusedContainerColor = Color(0xFF2A2A3E),
                        unfocusedContainerColor = Color(0xFF2A2A3E)
                    ),
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        if (cityInput.isNotBlank()) {
                            viewModel.fetchAqi(cityInput)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = aqiColor
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Text("Go", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            // Content
            when (uiState) {
                is AqiUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = Color.White)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Fetching AQI...", color = Color.White)
                        }
                    }
                }

                is AqiUiState.Success -> {
                    val data = (uiState as AqiUiState.Success).data
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 24.dp)
                    ) {
                        // City Name
                        Text(
                            text = data.cityName,
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        // AQI Circle
                        Box(
                            modifier = Modifier
                                .size(180.dp)
                                .clip(CircleShape)
                                .background(aqiColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${data.aqi}",
                                    color = Color.White,
                                    fontSize = 60.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "AQI",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }

                        // AQI Label
                        Text(
                            text = aqiLabel,
                            color = aqiColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        // Health Tip Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF2A2A3E)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "💡 Health Tip",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = when {
                                        aqi <= 50 -> "Air quality is great! Perfect time for outdoor activities."
                                        aqi <= 100 -> "Acceptable air quality. Sensitive people should limit outdoor exposure."
                                        aqi <= 150 -> "Sensitive groups should reduce outdoor activities. Consider wearing a mask."
                                        aqi <= 200 -> "Reduce outdoor activities. Keep windows closed."
                                        aqi <= 300 -> "Health alert! Avoid all outdoor activities. Wear N95 mask if necessary."
                                        else -> "Emergency! Stay indoors, seal windows. Seek medical help if unwell."
                                    },
                                    color = Color.LightGray,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        // Pollutants Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF2A2A3E)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "🧪 Pollutants",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                listOf(
                                    Triple("PM2.5", data.pollutants.pm25, "μg/m³"),
                                    Triple("PM10", data.pollutants.pm10, "μg/m³"),
                                    Triple("NO₂", data.pollutants.no2, "ppb"),
                                    Triple("O₃", data.pollutants.o3, "ppb"),
                                    Triple("CO", data.pollutants.co, "ppm"),
                                    Triple("SO₂", data.pollutants.so2, "ppb")
                                ).forEach { (name, value, unit) ->
                                    if (value != null) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(name, color = Color.LightGray, fontSize = 14.sp)
                                            Text(
                                                "$value $unit",
                                                color = Color.White,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        HorizontalDivider(color = Color(0xFF3A3A4E))
                                    }
                                }
                            }
                        }

                        // Forecast Card
                        if (data.forecast.isNotEmpty()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF2A2A3E)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "📅 5-Day Forecast",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    data.forecast.take(5).forEach { day ->
                                        val dayColor = when {
                                            day.avgAqi <= 50 -> Color(0xFF4CAF50)
                                            day.avgAqi <= 100 -> Color(0xFFFFEB3B)
                                            day.avgAqi <= 150 -> Color(0xFFFF9800)
                                            day.avgAqi <= 200 -> Color(0xFFF44336)
                                            day.avgAqi <= 300 -> Color(0xFF9C27B0)
                                            else -> Color(0xFF7B1FA2)
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = day.day,
                                                color = Color.LightGray,
                                                fontSize = 14.sp,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Text("↓${day.minAqi}", color = Color(0xFF4CAF50), fontSize = 13.sp)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("↑${day.maxAqi}", color = Color(0xFFF44336), fontSize = 13.sp)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(dayColor)
                                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = "${day.avgAqi}",
                                                    color = Color.White,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                        HorizontalDivider(color = Color(0xFF3A3A4E))
                                    }
                                }
                            }
                        }
                    }
                }

                is AqiUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(text = "🔍", fontSize = 48.sp)
                            Text(
                                text = "City Not Found",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Please check the city name and try again",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                is AqiUiState.NoInternet -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(text = "📡", fontSize = 64.sp)
                            Text(
                                text = "No Internet Connection",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Please check your connection and try again",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    locationPermissionLauncher.launch(
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2A2A3E)
                                )
                            ) {
                                Text("Retry", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}