package au.edu.jcu.assessment.utilityapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.jcu.assessment.utilityapp.ui.screens.SettingsScreen
import au.edu.jcu.assessment.utilityapp.ui.screens.UtilityScreen
import au.edu.jcu.assessment.utilityapp.ui.viewmodel.SettingsViewModel
import au.edu.jcu.assessment.utilityapp.ui.viewmodel.UtilityViewModel

// Enum defining bottom navigation screen targets
enum class AppScreen { UTILITY, SETTINGS }

// Root Composable holding app navigation and bottom tab bar
@Composable
fun UtilityApp(
    utilityViewModel: UtilityViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel()
) {
    // Currently active screen state
    var currentScreen by remember { mutableStateOf(AppScreen.UTILITY) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentScreen == AppScreen.UTILITY,
                    onClick = { currentScreen = AppScreen.UTILITY },
                    icon = { Icon(Icons.Default.Air, contentDescription = "Air Quality") },
                    label = { Text("AirPulse") }
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.SETTINGS,
                    onClick = { currentScreen = AppScreen.SETTINGS },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") }
                )
            }
        }
    ) { innerPadding ->
        // Render screen component based on active tab
        when (currentScreen) {
            AppScreen.UTILITY -> UtilityScreen(
                viewModel = utilityViewModel,
                settingsViewModel = settingsViewModel,
                modifier = Modifier.padding(innerPadding)
            )
            AppScreen.SETTINGS -> SettingsScreen(
                settingsViewModel = settingsViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}