// Repository data provider for Air Quality metrics
package au.edu.jcu.assessment.utilityapp.data.repository

import au.edu.jcu.assessment.utilityapp.data.api.AirQualityApiService
import au.edu.jcu.assessment.utilityapp.data.model.AirQualityResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AirQualityRepository(
    private val apiService: AirQualityApiService
) {
    suspend fun fetchAirQuality(lat: Double, lon: Double): Result<AirQualityResponse> {
        return withContext(Dispatchers.IO) {
            runCatching {
                apiService.getAirQuality(latitude = lat, longitude = lon)
            }
        }
    }
}