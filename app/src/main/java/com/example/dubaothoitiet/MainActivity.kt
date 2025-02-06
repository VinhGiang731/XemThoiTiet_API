package com.example.dubaothoitiet

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dubaothoitiet.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherApi: WeatherApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherApi = retrofit.create(WeatherApi::class.java)

        binding.btnGet.setOnClickListener {
            val cityName = binding.edtCity.text.toString()
            if(cityName.isNotBlank()){
                fetchWeather(cityName, binding.txtResult)
            }else{
                Toast.makeText(this, "Enter your city!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchWeather(cityName: String, txtResult: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = weatherApi.getWeather(cityName, "98da58f3d62bfddbf17e036adc1662a6")
                if(response.isSuccessful){
                    val weatherResponse = response.body()
                    withContext(Dispatchers.Main){
                        weatherResponse?.let {
                            val resultText = """
                                City: $cityName
                                Temp: ${it.main.temp}°C
                                Humnidity: ${it.main.humidity}%
                                Descriptiom: ${it.weather[0].description}
                            """.trimIndent()
                            binding.txtResult.text = resultText
                        }
                    }
                }
                else{
                    withContext(Dispatchers.Main){
                        binding.txtResult.text = "Error: ${response.message()}"
                    }
                }
            } catch (e: Exception){
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    binding.txtResult.text = "Failed to fetch weather"
                }
            }
        }
    }
}