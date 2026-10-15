package au.edu.jcu.assessment.utilityapp.data.repository

import au.edu.jcu.assessment.utilityapp.data.api.AirQualityApiService
import au.edu.jcu.assessment.utilityapp.data.model.AirQualityResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Repository layer handling network data fetching and error capturing
class AirQualityRepository(
    private val apiService: AirQualityApiService
) {
    // Fetch air quality data on IO thread pool dispatcher
    suspend fun fetchAirQuality(lat: Double, lon: Double): Result<AirQualityResponse> {
        return withContext(Dispatchers.IO) {
            runCatching {
                apiService.getAirQuality(latitude = lat, longitude = lon)
            }
        }
    }
}