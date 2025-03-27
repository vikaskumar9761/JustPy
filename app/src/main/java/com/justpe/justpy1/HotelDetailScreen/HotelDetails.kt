package com.justpe.justpy1.HotelDetailScreen

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.justpe.justpy1.HotelDetailScreen.FragmentScreen.DetailsFragment
import com.justpe.justpy1.HotelDetailScreen.FragmentScreen.OverviewFragment
import com.justpe.justpy1.HotelDetailScreen.FragmentScreen.RoomFragment
import com.justpe.justpy1.R
import com.justpe.justpy1.databinding.HotelDetailsBinding

class HotelDetails : AppCompatActivity() {
    lateinit var binding: HotelDetailsBinding
    private var selectedButton: Button? = null // Keeps track of the previously selected button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = HotelDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from the Hotel List screen (passed via Intent)
        val hotelName = intent.getStringExtra("hotelName") ?: "N/A"
        val location = intent.getStringExtra("location") ?: "N/A"
        val address = intent.getStringExtra("address") ?: "N/A"
        val rating = intent.getStringExtra("rating") ?: "N/A"
        val price = intent.getStringExtra("price") ?: "N/A"

        // Set text values in the UI from received data
        binding.tvHotelName.text = hotelName
        binding.tvLocation.text = location
        binding.tvRating.text = rating
        binding.tvAddress.text = address

        // Set images in the Image Slider
        val imageUrls = intent.getStringArrayListExtra("imageUrls") ?: arrayListOf("https://your-default-image-url.com/default.jpg")
        val imageSlider: ImageSlider = binding.imageSlide
        val slideModels = imageUrls.map { SlideModel(it, ScaleTypes.FIT) }
        imageSlider.setImageList(slideModels)
        imageSlider.startSliding(3000)

        // Load the default fragment (RoomFragment)
        ReplaceFragment(RoomFragment())
        updateButton(binding.btRoom) // Highlight the "Room" button as selected by default

        // Button click listeners to switch fragments
        binding.btRoom.setOnClickListener {
            ReplaceFragment(RoomFragment())
            updateButton(binding.btRoom)
        }
        binding.btOverView.setOnClickListener {
            ReplaceFragment(OverviewFragment())
            updateButton(binding.btOverView)
        }
        binding.btDetails.setOnClickListener {
            ReplaceFragment(DetailsFragment())
            updateButton(binding.btDetails)
        }
    }

    // Function to replace the current fragment with a new fragment
    private fun ReplaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    // Function to update the button colors when a new button is selected
    private fun updateButton(newSelectedButton: Button) {
        selectedButton?.setBackgroundColor(ContextCompat.getColor(this, R.color.white_A700)) // Reset previous button color
        newSelectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_500)) // Highlight the new button
        selectedButton = newSelectedButton
    }
}
