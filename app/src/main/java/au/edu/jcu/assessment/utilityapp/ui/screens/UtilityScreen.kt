package au.edu.jcu.assessment.utilityapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import au.edu.jcu.assessment.utilityapp.data.model.AqiStandard
import au.edu.jcu.assessment.utilityapp.data.model.TemperatureUnit
import au.edu.jcu.assessment.utilityapp.ui.viewmodel.SettingsViewModel
import au.edu.jcu.assessment.utilityapp.ui.viewmodel.UtilityUiState
import au.edu.jcu.assessment.utilityapp.ui.viewmodel.UtilityViewModel

// Main dashboard screen presenting live air quality metrics and health advice
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtilityScreen(
    viewModel: UtilityViewModel,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    // Observe state from view models
    val uiState by viewModel.uiState.collectAsState()
    val settings by settingsViewModel.settings.collectAsState()
    var expandedCityDropdown by remember { mutableStateOf(false) }

    // Fetch data whenever selected city changes
    LaunchedEffect(settings.selectedCity) {
        viewModel.loadDataForCity(settings.selectedCity)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AirPulse", fontWeight = FontWeight.Bold) },
                actions = {
                    // Manual refresh action button
                    IconButton(onClick = { viewModel.loadDataForCity(settings.selectedCity) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh Data")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Render UI state variations: Loading, Error, or Success
            when (val state = uiState) {
                is UtilityUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Fetching live atmospheric data...", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                is UtilityUiState.Error -> {
                    // Connection error state card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Connection Error", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onErrorContainer)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(state.message, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.loadDataForCity(settings.selectedCity) }) {
                                Text("Retry Connection")
                            }
                        }
                    }
                }
                is UtilityUiState.Success -> {
                    val current = state.data.current
                    val usAqi = current?.usAqi ?: 0
                    val (aqiStatus, aqiColor) = getAqiCategory(usAqi)

                    // City header card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(state.cityName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            Text("Last updated: ${state.lastUpdated}", style = MaterialTheme.typography.labelMedium)
                        }
                    }

                    // Main AQI gauge card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = aqiColor.copy(alpha = 0.15f))
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("AIR QUALITY INDEX", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
                            Text("$usAqi", fontSize = 64.sp, fontWeight = FontWeight.ExtraBold, color = aqiColor)
                            Surface(
                                color = aqiColor,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = aqiStatus,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Key metrics row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        MetricCard(
                            title = "PM2.5",
                            value = "${current?.pm25 ?: 0.0} µg/m³",
                            icon = Icons.Default.Air,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            title = "UV Index",
                            value = "${current?.uvIndex ?: 0.0}",
                            icon = Icons.Default.WbSunny,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Health recommendations section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Health Advisory", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(getAqiHealthAdvice(usAqi), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text("App Preferences", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            // Location dropdown selector
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Default Location", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = expandedCityDropdown,
                        onExpandedChange = { expandedCityDropdown = !expandedCityDropdown }
                    ) {
                        OutlinedTextField(
                            value = settings.selectedCity.name,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCityDropdown) },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedCityDropdown,
                            onDismissRequest = { expandedCityDropdown = false }
                        ) {
                            settingsViewModel.availableCities.forEach { city ->
                                DropdownMenuItem(
                                    text = { Text(city.name) },
                                    onClick = {
                                        settingsViewModel.updateCity(city)
                                        expandedCityDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // AQI format selection
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("AQI Standard Format", style = MaterialTheme.typography.titleSmall)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Use European CAQI Standard")
                        Switch(
                            checked = settings.aqiStandard == AqiStandard.EUROPEAN_CAQI,
                            onCheckedChange = { checked ->
                                settingsViewModel.updateAqiStandard(
                                    if (checked) AqiStandard.EUROPEAN_CAQI else AqiStandard.US_AQI
                                )
                            }
                        )
                    }
                }
            }

            // Temperature unit selection
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Temperature Metric", style = MaterialTheme.typography.titleSmall)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = settings.tempUnit == TemperatureUnit.CELSIUS,
                            onClick = { settingsViewModel.updateTemperatureUnit(TemperatureUnit.CELSIUS) }
                        )
                        Text("Celsius (°C)")
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(
                            selected = settings.tempUnit == TemperatureUnit.FAHRENHEIT,
                            onClick = { settingsViewModel.updateTemperatureUnit(TemperatureUnit.FAHRENHEIT) }
                        )
                        Text("Fahrenheit (°F)")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Reusable card component for displaying individual metric values
@Composable
fun MetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, style = MaterialTheme.typography.labelMedium)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

// Helper mapping AQI score to label and associated color code
fun getAqiCategory(aqi: Int): Pair<String, Color> {
    return when {
        aqi <= 50 -> "Good" to Color(0xFF4CAF50)
        aqi <= 100 -> "Moderate" to Color(0xFFFFC107)
        aqi <= 150 -> "Unhealthy for Sensitive Groups" to Color(0xFFFF9800)
        aqi <= 200 -> "Unhealthy" to Color(0xFFF44336)
        else -> "Hazardous" to Color(0xFF9C27B0)
    }
}

// Helper mapping AQI level to practical health recommendations
fun getAqiHealthAdvice(aqi: Int): String {
    return when {
        aqi <= 50 -> "Air quality is satisfactory. Enjoy outdoor activities!"
        aqi <= 100 -> "Air quality is acceptable. Sensitive individuals should consider reducing prolonged outdoor exertion."
        aqi <= 150 -> "Sensitive groups may experience health effects. General public is less likely to be affected."
        else -> "Health alert! Everyone may experience more serious health effects. Limit outdoor activity."
    }
}