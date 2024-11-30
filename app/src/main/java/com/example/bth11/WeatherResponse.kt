package com.example.bth11

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind
)
data class Main(
    val temp: Float,
    val humidity: Int // Độ ẩm
)

data class Weather(
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double // Tốc độ gió
)