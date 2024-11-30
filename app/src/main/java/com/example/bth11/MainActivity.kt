package com.example.bth11

import android.widget.ImageView
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var etCity: EditText
    private lateinit var btnSet: Button
    private lateinit var tvTemperature: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvDate: TextView
    private lateinit var ivWeatherIcon: ImageView
    private lateinit var tvHumidity: TextView
    private lateinit var tvWindSpeed: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etCity = findViewById(R.id.etCity)
        btnSet = findViewById(R.id.btnSet)
        tvTemperature = findViewById(R.id.tvTemperature)
        tvDescription = findViewById(R.id.tvDescription)
        tvDate = findViewById(R.id.tvDate)
        ivWeatherIcon = findViewById(R.id.ivWeatherIcon)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvWindSpeed = findViewById(R.id.tvWindSpeed)

        btnSet.setOnClickListener {
            val city = etCity.text.toString()
            if (city.isNotEmpty()) {
                fetchWeatherData(city)
            }
        }
    }

    private fun fetchWeatherData(city: String) {
        val apiKey = "a440157302cdc8a75626ec32c3838943"
        val call = RetrofitClient.weatherService.getWeather(city, apiKey)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val temp = it.main.temp
                        val description = it.weather[0].description.capitalize()
                        val icon = it.weather[0].icon
                        val humidity = it.main.humidity
                        val windSpeed = it.wind.speed
                        val date = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date())

                        // Cập nhật UI
                        tvTemperature.text = "${temp.toInt()}°"
                        tvDescription.text = description
                        tvDate.text = date
                        tvHumidity.text = "Humidity: $humidity%"
                        tvWindSpeed.text = "Wind: ${windSpeed} m/s"

                        // Lấy icon thời tiết
                        val weatherIcon = getWeatherIcon(description)

                        // Load weather icon using Glide
                        Glide.with(this@MainActivity)
                            .load("https://openweathermap.org/img/wn/${weatherIcon}.png")
                            .into(ivWeatherIcon)
                    }
                } else {
                    tvTemperature.text = "N/A"
                    tvDescription.text = "City not found"
                    tvDate.text = ""
                    tvHumidity.text = ""
                    tvWindSpeed.text = ""
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                tvTemperature.text = "Error"
                tvDescription.text = t.message
                tvDate.text = ""
                tvHumidity.text = ""
                tvWindSpeed.text = ""
            }
        })
    }

    // Hàm lấy icon thời tiết theo mô tả
    private fun getWeatherIcon(description: String): String {
        return when (description.toLowerCase(Locale.ROOT)) {
            "clear sky" -> "01d" // Icon cho trời nắng
            "few clouds" -> "02d" // Icon cho trời có vài đám mây
            "scattered clouds" -> "03d" // Icon cho mây rải rác
            "broken clouds" -> "04d" // Icon cho mây bị vỡ
            "shower rain" -> "09d" // Icon cho mưa rào
            "rain" -> "10d" // Icon cho mưa
            "thunderstorm" -> "11d" // Icon cho bão
            "snow" -> "13d" // Icon cho tuyết
            "mist" -> "50d" // Icon cho sương mù
            else -> "01d" // Mặc định là icon trời nắng nếu không có mô tả phù hợp
        }
    }
}
