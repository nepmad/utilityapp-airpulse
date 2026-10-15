package au.edu.jcu.assessment.utilityapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import au.edu.jcu.assessment.utilityapp.data.model.AqiStandard
import au.edu.jcu.assessment.utilityapp.data.model.TemperatureUnit
import au.edu.jcu.assessment.utilityapp.ui.viewmodel.SettingsViewModel

// Settings screen providing controls for default city, AQI format, and temperature metrics
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val settings by settingsViewModel.settings.collectAsState()
    var expandedCityDropdown by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("App Preferences", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            // Location selection dropdown
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
                                .menuAnchor()
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

            // AQI format configuration
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
        }
    }
}