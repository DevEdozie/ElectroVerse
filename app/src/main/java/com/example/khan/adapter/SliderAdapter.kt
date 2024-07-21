package com.example.khan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.khan.R
import com.example.khan.databinding.ViewPagerItemBinding
import com.example.khan.local_db.entity.CartItem
import com.example.khan.model.Item
import com.example.khan.viewmodel.MainActivityViewmodel

class SliderAdapter(
    private val items: List<Item>,
    private val viewModel: MainActivityViewmodel // Add private to make it accessible inside ViewHolder
) : RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: ViewPagerItemBinding,
        private val viewModel: MainActivityViewmodel // Add the viewModel to the ViewHolder constructor
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {

            val price = 500

            // Truncate the product title if it exceeds 15 characters
            val truncatedTitle = if (item.name.length > 15) {
                item.name.substring(0, 15) + "..."
            } else {
                item.name
            }
            binding.productTitle.text = truncatedTitle
//            binding.productPrice.text = "$${item.current_price[0].USD[0]}"
            binding.productPrice.text = "$${price}"
            val imageUrl = "https://api.timbu.cloud/images/${item.photos[0].url}"
            if (imageUrl.isNotEmpty()) {
                Glide.with(binding.productImg.context).load(imageUrl).into(binding.productImg)
            } else {
                binding.productImg.setImageResource(R.drawable.image_placeholder)
            }

            binding.addToCart.setOnClickListener {
                viewModel.addToCart( // Use viewModel here
                    CartItem(
                        cartItemId = 0,
                        productId = item.id, // Use item.id
                        productImageUrl = item.photos[0].url, // Use item.photos[0].url
                        productTitle = item.name, // Use item.name
                        productQuantity = 1,
                        productPrice = "$price" // This should be the actual price from item
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ViewPagerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, viewModel) // Pass viewModel to the ViewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
