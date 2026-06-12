# 🌫️ RealTimeAirQualityMonitor

A real-time Air Quality Index tracker built with **Jetpack Compose** and **Clean Architecture**.

![AQI App](https://github.com/user-attachments/assets/da6b3f97-0ad2-4f6d-8e2e-6f88462257c8)

---

## 📱 Screenshots

<p float="left">
  <img src="https://github.com/user-attachments/assets/da6b3f97-0ad2-4f6d-8e2e-6f88462257c8" width="200"/>
  <img src="https://github.com/user-attachments/assets/fa00d99a-1f4f-470b-bc70-4e5439a57396" width="200"/>
  <img src="https://github.com/user-attachments/assets/3280736e-9209-462a-9267-94594d28f208" width="200"/>
  <img src="https://github.com/user-attachments/assets/d2eb5168-44b4-4ef9-8a5d-6d257381b8ec" width="200"/>
</p>

---

## ✨ Features

- 📍 Real-time AQI fetch by current location
- 🔍 Search AQI by city name
- 🎨 Color-coded AQI indicator with smooth animations
- 🧪 Pollutants breakdown — PM2.5, PM10, NO₂, O₃, CO, SO₂
- 📅 5-day AQI forecast
- 💡 Health tips based on AQI level
- 📡 No internet connection handling
- ❌ City not found error handling

---

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | Clean Architecture, MVVM |
| DI | Hilt |
| Networking | Retrofit, OkHttp, Gson |
| Async | Coroutines, StateFlow |
| Location | FusedLocationProvider |
| API | WAQI (World Air Quality Index) |

---

## 🏗️ Architecture
This app follows **Clean Architecture** with 3 layers:
presentation/

├── ui/

│   └── HomeScreen.kt

└── viewmodel/

├── AqiViewModel.kt

└── AqiUiState.kt
domain/

├── model/

│   └── AqiData.kt

├── repository/

│   └── AqiRepository.kt

└── usecase/

├── GetAqiUseCase.kt

└── GetAqiByLocationUseCase.kt
data/

├── remote/

│   ├── AqiApiService.kt

│   ├── AqiDto.kt

│   └── LocationHelper.kt

└── repository/

└── AqiRepositoryImpl.kt

---

## 🚀 Setup

1. Clone the repo
```bash
git clone https://github.com/kapilj23/AQIApp.git
```

2. Get free API token from [WAQI](https://aqicn.org/data-platform/token/)

3. Add token in `AqiRepositoryImpl.kt`
```kotlin
token = "YOUR_WAQI_TOKEN_HERE"
```

4. Build and run!

---

## 📦 API

This app uses the [WAQI API](https://waqi.info/) — World Air Quality Index.

- Free tier available
- 1000+ cities worldwide
- Real-time data + forecast

---

## 👨‍💻 Author

**Kapil Joshi** — Android Developer

[![GitHub](https://img.shields.io/badge/GitHub-kapilj23-black?logo=github)](https://github.com/kapilj23)
