package com.example.wheatherapp

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.wheatherapp.databinding.ActivityMainBinding
import org.json.JSONObject
import java.lang.Math.ceil


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor=Color.parseColor("#FF000000")
        }
        val lat=intent.getStringExtra("lat")
        val long=intent.getStringExtra("long")

        getJsonData(lat,long)

    }

    private fun getJsonData(lat: String?, long: String?) {
        val API_KEY="827f148b6f0396109c3c3c500766f0f2"
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${API_KEY}"


//
        val stringRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
                setValues(response)
            },
            {Toast.makeText(this,"Error",Toast.LENGTH_LONG).show() })

        queue.add(stringRequest)
    }

    private fun setValues(response: JSONObject?) {
        binding.city.text= response?.getString("name")
        val lat= response?.getJSONObject("coord")?.getString("lat")
        val long= response?.getJSONObject("coord")?.getString("lon")
        binding.cord.text="$lat , $long"

        binding.wheather.text=
            response?.getJSONArray("weather")?.getJSONObject(0)?.getString("main")

        if(binding.wheather.text.contentEquals("smoke",true))
        {
            binding.image.setImageResource(R.drawable.sun_cloudy)
            binding.back.setBackgroundResource(R.color.smoke)
        }
        else{
            if(binding.wheather.text.contentEquals("Rain",true)){
                binding.image.setImageResource(R.drawable.rainy)
                binding.back.setBackgroundResource(R.color.rain)
            }
            else{
                binding.image.setImageResource(R.drawable.sun)
                binding.back.setBackgroundResource(R.color.sun)

            }
        }

        var temp= response?.getJSONObject("main")?.getString("temp")
        temp=(temp!!.toFloat() -273.15).toInt().toString()
        binding.temp.text="${temp}°C"
        var mintemp= response?.getJSONObject("main")!!.getString("temp_min")
        mintemp=(((mintemp).toFloat()-273.15)).toInt().toString()
        binding.min.text="Min: "+mintemp+"°C"

        var maxtemp= response.getJSONObject("main").getString("temp_max")
        maxtemp=((ceil((maxtemp).toFloat()-273.15))).toInt().toString()
        binding.max.text="Max: "+maxtemp+"°C"

        binding.pressure.text=response.getJSONObject("main").getString("pressure")
        binding.humidity.text=response.getJSONObject("main").getString("humidity")

        binding.windSpeed.text=response.getJSONObject("wind").getString("speed")
        binding.degree.text="Degree : "+response.getJSONObject("wind").getString("deg")+"°"









    }
}