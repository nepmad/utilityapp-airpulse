package au.edu.jcu.assessment.utilityapp.ui.viewmodel

import au.edu.jcu.assessment.utilityapp.data.model.AirQualityResponse

// Sealed interface representing UI state hierarchy for air quality view
sealed interface UtilityUiState {
    // Data request in progress
    object Loading : UtilityUiState
    
    // Data successfully loaded from API
    data class Success(
        val data: AirQualityResponse,
        val cityName: String,
        val lastUpdated: String
    ) : UtilityUiState
    
    // Data request failed with error
    data class Error(val message: String) : UtilityUiState
}