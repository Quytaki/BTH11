package com.example.bth11

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    // Khai báo các thành phần giao diện
    private lateinit var etCity: EditText
    private lateinit var btnSet: ImageView
    private lateinit var tvHumidity: TextView
    private lateinit var tvMinTemperature: TextView
    private lateinit var tvMaxTemperature: TextView
    private lateinit var tvSunrise: TextView
    private lateinit var tvSunset: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ánh xạ các thành phần giao diện
        etCity = findViewById(R.id.etCity)
        btnSet = findViewById(R.id.searchIcon)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvMinTemperature = findViewById(R.id.tvMinTemperature)
        tvMaxTemperature = findViewById(R.id.tvMaxTemperature)
        tvSunrise = findViewById(R.id.tvSunrise)
        tvSunset = findViewById(R.id.tvSunset)

        // Sự kiện khi nhấn nút tìm kiếm
        btnSet.setOnClickListener {
            val city = etCity.text.toString().trim()
            if (city.isEmpty()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
            } else {
                fetchWeatherData(city)
            }
        }
    }

    private fun fetchWeatherData(city: String) {
        val apiKey = "a440157302cdc8a75626ec32c3838943" // API Key
        val call = RetrofitClient.weatherService.getWeather(city, apiKey)

        // Hiển thị trạng thái đang tải
        resetWeatherInfo("Loading...")

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { weather ->
                        updateWeatherInfo(weather)
                    }
                } else {
                    resetWeatherInfo("City not found")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                resetWeatherInfo("Error: ${t.message}")
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateWeatherInfo(weather: WeatherResponse) {
        val humidity = weather.main.humidity
        val minTemp = weather.main.temp_min
        val maxTemp = weather.main.temp_max
        val sunrise = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(weather.sys.sunrise * 1000))
        val sunset = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(weather.sys.sunset * 1000))

        // Cập nhật thông tin thời tiết
        tvHumidity.text = "Humidity: $humidity%"
        tvMinTemperature.text = "Min Temperature: ${minTemp.toInt()}°"
        tvMaxTemperature.text = "Max Temperature: ${maxTemp.toInt()}°"
        tvSunrise.text = "Sunrise: $sunrise"
        tvSunset.text = "Sunset: $sunset"
    }

    private fun resetWeatherInfo(message: String) {
        tvHumidity.text = "Humidity: --%"
        tvMinTemperature.text = "Min Temperature: --°"
        tvMaxTemperature.text = "Max Temperature: --°"
        tvSunrise.text = "Sunrise: --"
        tvSunset.text = "Sunset: --"
    }
}
