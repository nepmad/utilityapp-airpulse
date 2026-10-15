package au.edu.jcu.assessment.utilityapp.data.model

import com.google.gson.annotations.SerializedName

// Response DTO matching root API JSON object
data class AirQualityResponse(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("current") val current: CurrentAirQuality?
)

// DTO containing live atmospheric readings
data class CurrentAirQuality(
    @SerializedName("time") val time: String,
    @SerializedName("interval") val interval: Int,
    @SerializedName("us_aqi") val usAqi: Int?,
    @SerializedName("pm2_5") val pm25: Double?,
    @SerializedName("pm10") val pm10: Double?,
    @SerializedName("dust") val dust: Double?,
    @SerializedName("uv_index") val uvIndex: Double?
)

// Supported temperature metrics
enum class TemperatureUnit { CELSIUS, FAHRENHEIT }

// Supported AQI scale formats
enum class AqiStandard { US_AQI, EUROPEAN_CAQI }

// Model holding location name and GPS coordinates
data class CityLocation(
    val name: String,
    val lat: Double,
    val lon: Double
)

// User settings data model
data class UserSettings(
    val selectedCity: CityLocation = CityLocation("Kathmandu", 27.7172, 85.3240),
    val tempUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val aqiStandard: AqiStandard = AqiStandard.US_AQI,
    val autoRefresh: Boolean = true
)