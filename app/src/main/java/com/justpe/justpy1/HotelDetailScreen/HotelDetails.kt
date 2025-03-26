package com.justpe.justpy1.HotelDetailScreen

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.justpe.justpy1.databinding.HotelDetailsBinding

class HotelDetails : AppCompatActivity() {
    lateinit var binding: HotelDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding=HotelDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get data from Hotel List Screen
        val hotelName = intent.getStringExtra("hotelName") ?: "N/A"
        val location = intent.getStringExtra("location") ?: "N/A"
        val address = intent.getStringExtra("address") ?: "N/A"
        val rating = intent.getStringExtra("rating") ?: "N/A"
        val price = intent.getStringExtra("price") ?: "N/A"
       //set text from Hotel .xml file
        binding.tvHotelName.text=hotelName
        binding.tvLocation.text=location
        binding.tvRating.text=rating
        binding.tvAddress.text=address

        val imageUrls = intent.getStringArrayListExtra("imageUrls") ?: arrayListOf("https://your-default-image-url.com/default.jpg")

        val imageSlider: ImageSlider = binding.imageSlide
        val slideModels = imageUrls.map { SlideModel(it, ScaleTypes.FIT) }
        imageSlider.setImageList(slideModels)
        imageSlider.startSliding(3000)

    }
}