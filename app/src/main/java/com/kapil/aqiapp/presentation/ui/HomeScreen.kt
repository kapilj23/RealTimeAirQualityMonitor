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

    LaunchedEffect(Unit) {
        viewModel.fetchAqi("bareilly")
    }

    val aqi = if (uiState is AqiUiState.Success) {
        (uiState as AqiUiState.Success).data.aqi
    } else 0

    val aqiColor by animateColorAsState(
        targetValue = when {
            aqi <= 50 -> Color(0xFF4CAF50)   // Green - Good
            aqi <= 100 -> Color(0xFFFFEB3B)  // Yellow - Moderate
            aqi <= 150 -> Color(0xFFFF9800)  // Orange - Unhealthy
            aqi <= 200 -> Color(0xFFF44336)  // Red - Very Unhealthy
            aqi <= 300 -> Color(0xFF9C27B0)  // Purple - Hazardous
            else -> Color(0xFF7B1FA2)         // Dark Purple - Severe
        },
        animationSpec = tween(1000),
        label = "aqi_color"
    )

    val aqiLabel = when {
        aqi <= 50 -> "Good"
        aqi <= 100 -> "Moderate"
        aqi <= 150 -> "Unhealthy"
        aqi <= 200 -> "Very Unhealthy"
        aqi <= 300 -> "Hazardous"
        else -> "Severe"
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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = data.cityName,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )

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

                    Text(
                        text = aqiLabel,
                        color = aqiColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            is AqiUiState.Error -> {
                val message = (uiState as AqiUiState.Error).message
                Text(
                    text = "Error: $message",
                    color = Color.Red
                )
            }
        }
    }
}