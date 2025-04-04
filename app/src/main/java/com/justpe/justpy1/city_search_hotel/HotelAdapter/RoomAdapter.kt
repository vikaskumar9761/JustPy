package com.justpe.justpy1.city_search_hotel.HotelAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.justpe.justpy1.R
import com.justpe.justpy1.databinding.ItemDetailsBinding
import com.justpe.justpy1.city_search_hotel.HotelModel.RoomDetailsResponse

class RoomAdapter(
    private var roomList: List<RoomDetailsResponse.AvailableRoom> = emptyList(),
    private val onRoomSelected: (String) -> Unit
) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    private var selectedPosition: Int = 0

    class RoomViewHolder(val binding: ItemDetailsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val binding = ItemDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val room = roomList[position]

        holder.binding.imgName.text = room.roomType
        holder.binding.tvRoomDetails1.text = "Meal: ${room.Meal ?: "No Name"}"
        holder.binding.tvRoomType.text = "Room Type: ${room.BedType ?: "N/A"}"
        holder.binding.tvTotalRoomPrice.text = "Hotel Discount: ${room.HotelDiscount ?: "N/A"}"
        holder.binding.tvRoomPrice.text = "Price: ${room.Price?: "N/A"} USD"
        holder.binding.tvBookingPolicy.text = "BookingPolicy: ${room.BookingPolicy ?: "N/A"}"
        holder.binding.tvCancellationPolicy.text = "CancellationPolicy: ${room.CancellationPolicy ?: "N/A"}"

        // ✅ Properly manage RadioButton selection state
        holder.binding.rbRoomOnly.setOnCheckedChangeListener(null) // reset old listener
        holder.binding.rbRoomOnly.isChecked = (position == selectedPosition)

        // ✅ Click on entire item
        holder.itemView.setOnClickListener {
            holder.binding.rbRoomOnly.performClick()
        }

        // ✅ When user checks the radio button
        holder.binding.rbRoomOnly.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val previousSelected = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)

                onRoomSelected((room.NP ?: "0").toString())
            }
        }

        // ✅ Load Image
        Glide.with(holder.itemView.context)
            .load(room.roomImage)
            .placeholder(R.drawable.ic_home_black_24dp)
            .into(holder.binding.ivRoomImage)
    }


    override fun getItemCount(): Int = roomList.size

    fun updateList1(newList1: List<RoomDetailsResponse.AvailableRoom>) {
        roomList = newList1
        selectedPosition = 0  // Select first by default

        // Trigger callback with first item
        if (roomList.isNotEmpty()) {
            onRoomSelected((roomList[0].NP ?: roomList[0].Price?: "0").toString())
        }

        notifyDataSetChanged()
    }
}
