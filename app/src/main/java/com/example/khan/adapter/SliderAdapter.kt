package com.example.khan.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.khan.R
import com.example.khan.databinding.ViewPagerItemBinding
import com.example.khan.model.Item

class SliderAdapter(private val items: List<Item>) :
    RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ViewPagerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {

//            Log.d("SLIDER ADAPTER", "${item.name}/n ${item.current_price[0].USD[0]}/n ")

            // Truncate the product title if it exceeds 15 characters
            val truncatedTitle = if (item.name.length > 15) {
                item.name.substring(0, 15) + "..."
            } else {
                item.name
            }
            binding.productTitle.text = truncatedTitle
//            binding.productPrice.text = "$${item.current_price[0].USD[0]}"
            val imageUrl = "https://api.timbu.cloud/images/${item.photos[0].url}"
            if (imageUrl.isNotEmpty()) {
                Glide.with(binding.productImg.context).load(imageUrl).into(binding.productImg)
            } else {
                binding.productImg.setImageResource(R.drawable.image_placeholder)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ViewPagerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
