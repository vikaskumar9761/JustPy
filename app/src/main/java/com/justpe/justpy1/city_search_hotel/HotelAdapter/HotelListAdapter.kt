package com.justpe.justpy1.city_search_hotel.HotelAdapter
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.justpe.justpy1.HotelDetailScreen.HotelDetails
import com.justpe.justpy1.R
import com.justpe.justpy1.city_search_hotel.HotelModel.hotelListModel
import com.justpe.justpy1.databinding.ItemHotelBinding
import java.util.ArrayList

// Adapter for displaying a list of hotels in a RecyclerView
class HotelListAdapter(
    private var hotelList: List<hotelListModel.HotelData>,
    private val context: Context // List of hotels to display
) : RecyclerView.Adapter<HotelListAdapter.HotelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val binding = ItemHotelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HotelViewHolder(binding,context)
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        val hotel = hotelList[position]
        holder.bind(hotel) // Call bind function to set data
    }

    override fun getItemCount(): Int = hotelList.size

    fun updateList(newList: List<hotelListModel.HotelData>) {
        hotelList = newList  // Replace the old list with the new one
        this.notifyDataSetChanged() // Refresh the RecyclerView
    }

    class HotelViewHolder(private val binding: ItemHotelBinding, private val context: Context)
        : RecyclerView.ViewHolder(binding.root) {


        fun bind(hotel: hotelListModel.HotelData) {
            binding.tvHotelName.text = hotel.hotelName
            binding.tvLocation.text = hotel.Location ?: "Location not available"
            binding.tvAddress.text = hotel.address
            binding.tvRating.text = hotel.rating
            binding.starRating.text = hotel.rating
            binding.tvPrice.text = hotel.TotalPrice

            // Change rating text color based on value
            val rating = hotel.rating.toDoubleOrNull() ?: 0.0
            val (colorRes, imageRes) = when {
                rating >= 6.0 -> Pair(R.color.green_600, R.drawable.full_rating_img)
                rating >= 4.0 -> Pair(R.color.green_600, R.drawable.full_rating_img)
                rating >= 2.0 -> Pair(R.color.yellow_500, R.drawable.rating_img)
                else -> Pair(R.color.red_600, android.R.drawable.star_big_on)
            }

            binding.tvRating.setTextColor(ContextCompat.getColor(binding.root.context, colorRes))
            binding.ivRatingStar.setImageResource(imageRes)

            // ✅ ImageSlider Setup
            val imageUrl = hotel.ImageThumbUrl?.takeIf { it.isNotBlank() }
                ?: "https://your-default-image-url.com/default.jpg" // ✅ Default Image

            val imageUrls = List(3) { imageUrl } // ✅ 3 बार same image add की गई

            val imageSlider: ImageSlider = binding.imageSlide
            val slideModels = imageUrls.map { SlideModel(it, ScaleTypes.FIT) }
            imageSlider.setImageList(slideModels)
            imageSlider.startSliding(3000)
            // Set click listener
            binding.root.setOnClickListener {
                val intent = Intent(context, HotelDetails::class.java).apply {
                    putExtra("hotelName", hotel.hotelName)
                    putExtra("location", hotel.Location)
                    putExtra("address", hotel.address)
                    putExtra("rating", hotel.rating)
                    putExtra("price", hotel.TotalPrice)
                    putExtra("EMTCommonID", hotel.EMTCommonID)
                    putStringArrayListExtra("imageUrls", ArrayList(imageUrls))                }
                context.startActivity(intent)
            }

        }
    }
}

