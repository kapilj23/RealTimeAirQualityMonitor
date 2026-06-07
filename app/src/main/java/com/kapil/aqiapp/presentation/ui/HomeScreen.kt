import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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

    LaunchedEffect(Unit) {
        viewModel.fetchAqi("bareilly")
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
            .background(Color(0xFF1A1A2E)),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is AqiUiState.Loading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color.White)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Fetching AQI...", color = Color.White)
                }
            }

            is AqiUiState.Success -> {
                val data = (uiState as AqiUiState.Success).data
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = data.cityName,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )

                    // Search Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = cityInput,
                            onValueChange = { cityInput = it },
                            placeholder = {
                                Text("Search city...", color = Color.Gray)
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.Gray
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = {
                                if (cityInput.isNotBlank()) {
                                    viewModel.fetchAqi(cityInput)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            )
                        ) {
                            Text("Search", color = Color(0xFF1A1A2E))
                        }
                    }

                    // AQI Circle
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .background(aqiColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${data.aqi}",
                                color = Color.White,
                                fontSize = 64.sp,
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
                }
            }

            is AqiUiState.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "🔍",
                        fontSize = 48.sp
                    )
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

                    // Search bar error state mein bhi dikhao
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = cityInput,
                            onValueChange = { cityInput = it },
                            placeholder = {
                                Text("Search city...", color = Color.Gray)
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.Gray
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = {
                                if (cityInput.isNotBlank()) {
                                    viewModel.fetchAqi(cityInput)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            )
                        ) {
                            Text("Search", color = Color(0xFF1A1A2E))
                        }
                    }}}
        }
    }
}