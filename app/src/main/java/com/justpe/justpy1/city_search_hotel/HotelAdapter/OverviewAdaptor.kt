package com.justpe.justpy1.city_search_hotel.HotelAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.justpe.justpy1.city_search_hotel.HotelModel.DetailsModel
import com.justpe.justpy1.databinding.ItemOverviewBinding

class OverviewAdaptor( private var roomList: List<DetailsModel.HotelImageDetail>): RecyclerView.Adapter<OverviewAdaptor.ViewHolder>() {
    class ViewHolder(var binding:ItemOverviewBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=ItemOverviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val room = roomList[position]
        holder.binding.imgName.text = room.ImageDescription

        Glide.with(holder.itemView.context)
            .load(room.Url)
            .placeholder(com.justpe.justpy1.R.drawable.adult)
            .into(holder.binding.ivRoomImage)
    }

    override fun getItemCount(): Int=roomList.size

    fun updateList(newList: List<DetailsModel.HotelImageDetail>) {
        roomList = newList  // Replace the old list with the new one
        this.notifyDataSetChanged() // Refresh the RecyclerView
    }

}