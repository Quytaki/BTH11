package com.example.bth11

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val sys: Sys
)
data class Main(
    val temp: Double,
    val temp_min: Double,
    val temp_max: Double,
    val humidity: Int
)

data class Weather(
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double // Tốc độ gió
)
data class Sys(
    val sunrise: Long,
    val sunset: Long
)