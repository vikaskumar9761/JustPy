package com.justpe.justpy1
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.justpe.justpy1.databinding.HotelMainBinding
import com.justpe.justpy1.searchHotel.searchHotel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: HotelMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding=HotelMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val city = intent.getStringExtra("city")?:" Delhi"
        val country = intent.getStringExtra("country")?:"India"

        val cityTextView: TextView = findViewById(R.id.cityTextView)
        val countryTextView: TextView = findViewById(R.id.countryTextView)

        cityTextView.text = city
        countryTextView.text = country

        binding.selectCity.setOnClickListener {
            val intent= Intent(this,searchHotel::class.java)
            startActivity(intent)

        }
    }
}