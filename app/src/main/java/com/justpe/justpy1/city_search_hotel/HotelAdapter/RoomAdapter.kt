package com.justpe.justpy1.city_search_hotel.HotelAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.justpe.justpy1.R
import com.justpe.justpy1.databinding.ItemDetailsBinding
import com.justpe.justpy1.city_search_hotel.HotelModel.DetailsModel
import com.justpe.justpy1.city_search_hotel.HotelModel.RoomDetailsResponse

class RoomAdapter(
    private var roomList: List<DetailsModel.RoomImage> = emptyList(),
    private var roomList1: List<RoomDetailsResponse.AvailableRoom> = emptyList()
) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    class RoomViewHolder(val binding: ItemDetailsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val binding = ItemDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        if (position < roomList.size) {
            // 🔹 Room Image Data Bind
            val room = roomList[position]
           // holder.binding.imgName.text = (room.EngineType ?: "N/A").toString()
//            Glide.with(holder.itemView.context)
//                .load(room.Url)
//                .placeholder(com.justpe.justpy1.R.drawable.adult)
//                .into(holder.binding.ivRoomImage)
        }

        if (position < roomList1.size) {
            // 🔹 Hotel Name & Price Bind
            val room1 = roomList1[position]
            holder.binding.imgName.setText("${room1.roomType}")
            holder.binding.tvRoomDetails1.text = room1.Meal ?: "No Name"
            holder.binding.tvRoomPrice1.text = "Price: ${room1.Price ?: "N/A"} USD"
            Glide.with(holder.itemView.context)
                .load(room1.roomImage)
                .placeholder(R.drawable.ic_home_black_24dp)
                .into(holder.binding.ivRoomImage)
        }
    }

    override fun getItemCount(): Int {
        return maxOf(roomList.size, roomList1.size)  // 🔹 Maximum list size return karega
    }

    // 🔹 Update Room Image List
    fun updateList(newList: List<DetailsModel.RoomImage>) {
        roomList = newList
        notifyDataSetChanged()
    }

    // 🔹 Update Hotel List
    fun updateList1(newList1: List<RoomDetailsResponse.AvailableRoom>) {
        roomList1 = newList1
        notifyDataSetChanged()
    }
}
