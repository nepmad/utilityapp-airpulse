# AirPulse - Live Air Quality Index and Atmospheric Tracker

## Executive Summary

AirPulse is a native Android utility application designed to provide users with the latest available Air Quality Index (AQI) metrics, atmospheric pollutant readings, and general health advisories. Built using Kotlin and Jetpack Compose, the application targets urban residents, travelers, and health-conscious individuals who need convenient access to environmental data.

The application follows Modern Android Development practices, incorporating an MVVM (Model-View-ViewModel) architecture, reactive data flows through StateFlow and Coroutines, and REST networking using Retrofit. Users can monitor atmospheric indicators such as US AQI, PM2.5, and UV index across major international cities.

## Project Architecture and Technology Stack

The project separates networking, data handling, UI rendering, and state management into dedicated packages.

### Technology Matrix

| Layer | Component / Library | Purpose |
| --- | --- | --- |
| Language | Kotlin 2.0+ | Concise, type-safe Android development |
| UI Framework | Jetpack Compose and Material 3 | Declarative UI rendering and adaptive layouts |
| Architecture | MVVM | Separation of UI and application logic |
| Networking | Retrofit 2 and Gson Converter | HTTP requests and JSON deserialization |
| Concurrency | Kotlin Coroutines and Flow | Asynchronous processing and reactive data flows |
| State Management | StateFlow and ViewModel | Observable screen state and retention across configuration changes |
| Data Source | Open-Meteo Air Quality API | Environmental readings |

### Package Directory Structure

```text
au.edu.jcu.assessment.utilityapp
├── data
│   ├── api
│   │   ├── AirQualityApiService.kt
│   │   └── NetworkClient.kt
│   ├── model
│   │   └── AirQualityResponse.kt
│   └── repository
│       └── AirQualityRepository.kt
├── ui
│   ├── screens
│   │   ├── SettingsScreen.kt
│   │   └── UtilityScreen.kt
│   ├── theme
│   ├── viewmodel
│   │   ├── SettingsViewModel.kt
│   │   ├── UiState.kt
│   │   └── UtilityViewModel.kt
│   └── UtilityApp.kt
└── MainActivity.kt
```

### Architecture Responsibilities

- **Data layer:** Defines API services, response models, and repository logic.
- **UI layer:** Implements the dashboard and settings screens using Compose.
- **ViewModel layer:** Coordinates requests and exposes observable UI state.
- **Theme layer:** Defines colors, typography, and Material 3 styling.
- **MainActivity:** Provides the Android application entry point.

## Key Features and User Interface

- **Environmental Monitoring:** Retrieve US AQI, PM2.5, PM10, dust concentration, and UV index readings.
- **Location Selection:** Switch between Kathmandu, Brisbane, Sydney, London, and Tokyo.
- **App Preferences:** Configure AQI standards and temperature units where supported by the implementation.
- **Dynamic Health Advisories:** Display color-coded categories and general guidance based on the selected AQI scale.
- **Material 3 Design:** Support adaptive layouts, dark-theme surfaces, and scrolling Compose components.
- **Reactive Updates:** Update the interface through Coroutines and StateFlow.
- **Error Handling:** Provide loading indicators, understandable error messages, and retry actions.

> Temperature preferences apply only when temperature data is available. European air quality reporting requires its own thresholds and must not use US AQI classifications.

## AQI Classification Tiers

The following categories apply specifically to the **US AQI scale**.

| US AQI Range | Classification | Indicator Color | General Health Advisory |
| --- | --- | --- | --- |
| 0–50 | Good | Green | Air quality is satisfactory. Enjoy outdoor activities. |
| 51–100 | Moderate | Yellow | Air quality is generally acceptable. Sensitive individuals should consider reducing prolonged or strenuous outdoor exertion if symptoms occur. |
| 101–150 | Unhealthy for Sensitive Groups | Orange | Sensitive groups should reduce prolonged or strenuous outdoor activities. |
| 151–200 | Unhealthy | Red | Everyone may experience health effects. Limit outdoor exertion. |
| 201–300 | Very Unhealthy | Purple | Reduce outdoor exposure and follow local health guidance. |
| 301–500 | Hazardous | Maroon | Avoid outdoor exposure where possible and follow official emergency advice. |

Health advisories provide general guidance and do not replace official local health advice.

## API Endpoint Reference

The application integrates with the [Open-Meteo Air Quality API](https://open-meteo.com/en/docs/air-quality-api).

### Endpoint Overview

| Setting | Value |
| --- | --- |
| Base URL | `https://air-quality-api.open-meteo.com/` |
| HTTP Method | `GET` |
| Endpoint | `/v1/air-quality` |

### Request Parameters

| Parameter | Type | Description |
| --- | --- | --- |
| `latitude` | Double | Latitude of the selected location |
| `longitude` | Double | Longitude of the selected location |
| `current` | String | Requested current metrics: `us_aqi,pm2_5,pm10,dust,uv_index` |

### Example Request

The following request uses Brisbane's coordinates:

```text
https://air-quality-api.open-meteo.com/v1/air-quality?latitude=-27.4698&longitude=153.0251&current=us_aqi,pm2_5,pm10,dust,uv_index
```

### Measurement Reference

| Metric | Description |
| --- | --- |
| `us_aqi` | United States Air Quality Index |
| `pm2_5` | Particulate matter with a diameter of 2.5 micrometers or less |
| `pm10` | Particulate matter with a diameter of 10 micrometers or less |
| `dust` | Atmospheric dust concentration |
| `uv_index` | Ultraviolet radiation index |

AQI is an index, while particulate matter and dust are concentrations. Display units using the API response metadata. UV index is a separate atmospheric indicator and is not part of the AQI calculation.

Data freshness depends on the provider's updates. Display the reading timestamp or retrieval time so users can assess how current the information is.

## Setup and Installation

### Prerequisites

- Android Studio compatible with the project's Android Gradle Plugin and SDK configuration
- Android SDK with Compile SDK 36 installed
- JDK 17 or the version required by the project
- Git
- An emulator or physical device running Android 8.0 (API 26) or later

### 1. Clone the Repository

Replace `your-username` with the repository owner's GitHub username:

```bash
git clone https://github.com/your-username/AirPulse-UtilityApp.git
cd AirPulse-UtilityApp
```

### 2. Open in Android Studio

1. Launch Android Studio and select **Open**.
2. Navigate to the cloned folder.
3. Select the project root directory.

### 3. Sync Gradle

Allow Android Studio to download the required dependencies and sync the project.

If necessary, select **File > Sync Project with Gradle Files**.

### 4. Run the Application

1. Start an Android Virtual Device or connect a physical device with USB debugging enabled.
2. Select the target device in Android Studio.
3. Click **Run**.
4. Wait for the application to install and launch.

## Android Configuration

| Configuration | Value |
| --- | --- |
| Application Type | Native Android |
| Package | `au.edu.jcu.assessment.utilityapp` |
| Language | Kotlin 2.0+ |
| Minimum SDK | 26 — Android 8.0 |
| Compile SDK | 36 |
| JDK | 17+ |
| UI Toolkit | Jetpack Compose |
| Design System | Material 3 |
| Architecture | MVVM |
| Network Client | Retrofit 2 |

## Permissions and Privacy

The application requires internet access to retrieve environmental readings:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

Requests send the selected city's coordinates to Open-Meteo. Selecting a predefined city does not require access to the device's GPS location.

## Error Handling and Limitations

- Loading indicators show when a request is in progress.
- Error messages explain unsuccessful requests and provide retry actions.
- StateFlow propagates state changes to the Compose UI.
- ViewModel retains screen state across supported configuration changes.
- Environmental readings depend on API availability and network connectivity.
- Data for a requested location may differ from nearby monitoring stations.
- Some measurements may be unavailable for a particular location or time.

## License and Assessment Information

| Item | Details |
| --- | --- |
| Course | CP3406 / CP5607 Mobile Computing |
| Institution | James Cook University (JCU) |
| Author | Madan Khatiwada |
| License | MIT License |

This project is licensed under the MIT License. Include a `LICENSE` file in the repository containing the full license text.

## Acknowledgements

- [Open-Meteo](https://open-meteo.com/) for environmental data.
- [Jetpack Compose](https://developer.android.com/compose) for the UI toolkit.
- [Material 3](https://m3.material.io/) for the design system.
- [Retrofit](https://square.github.io/retrofit/) for networking.
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) for asynchronous programming.
