package com.example.sky_mood

import MyDataItem
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sky_mood.R.id.getWeatherData
import com.example.sky_mood.ui.theme.Sky_MoodTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity:ComponentActivity() {


    private lateinit var weatherRecyclerView: RecyclerView
    private lateinit var cityEditText: EditText
    private lateinit var getWeatherImageButton: ImageButton
    private lateinit var weatherAdapter: WeatherAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activty_main)

        cityEditText=findViewById(R.id.cityEditText)
        getWeatherImageButton=findViewById(getWeatherData)
        weatherRecyclerView = findViewById(R.id.weatherRecyclerView)
        weatherRecyclerView.layoutManager = LinearLayoutManager(this)

         getWeatherImageButton.setOnClickListener {
             val cityName = cityEditText.text.toString()
             if(cityName.isNotEmpty())
             {
                 fetchWeatherData(cityName)
             }
             else{
                 //optionally ,show an error message or prompt to enter a city name
         }

         }
    }
   
      private fun fetchWeatherData(city: String) {
        val retrofit= Retrofit.Builder()
            .baseUrl("https://freetestapi.com/api/v1/weathers")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
         val api= retrofit.create(WeatherApi::class.java)
        val call=api.getData(city,"your_api_key")


        call.enqueue(object : Callback<List<MyDataItem>> {
            override fun onResponse(call: Call<List<MyDataItem>>, response: Response<List<MyDataItem>>) {
                if (response.isSuccessful && response.body() != null){
                     weatherAdapter = WeatherAdapter(response.body()!!)
                    weatherRecyclerView.adapter = weatherAdapter
                }
                else{
                    Log.d("MainActivity","Response unsuccessful: ${response.message()}")


                }
            }

            override fun onFailure(call: Call<List<MyDataItem>>, t: Throwable) {
                Log.d("onFailure","onFailure: ${t.message}")
            }


        })

    }


}




    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        Sky_MoodTheme {
            Greeting("Android")
        }
    }
