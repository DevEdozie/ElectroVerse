package com.example.khan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.example.khan.R
import com.example.khan.databinding.FeaturedShoesItemBinding
import com.example.khan.local_db.entity.WishListItem
import com.example.khan.viewmodel.MainActivityViewmodel


// Adapter class for displaying products in a RecyclerView
class WishListAdapter(
    private val viewModel: MainActivityViewmodel
) : RecyclerView.Adapter<WishListAdapter.WishListAdapterViewHolder>() {

    // ViewHolder class to hold and bind the product item view
    inner class WishListAdapterViewHolder(val itemBinding: FeaturedShoesItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    // DiffUtil callback to efficiently update the list of products
    private val differCallBack = object : DiffUtil.ItemCallback<WishListItem>() {

        // Check if the IDs of the items are the same to determine if they represent the same item
        override fun areItemsTheSame(oldItem: WishListItem, newItem: WishListItem): Boolean {
            return oldItem.wishListItemId == newItem.wishListItemId
        }

        // Check if the content of the items is the same to determine if they are identical
        override fun areContentsTheSame(oldItem: WishListItem, newItem: WishListItem): Boolean {
            return oldItem == newItem
        }
    }

    // AsyncListDiffer to handle list changes asynchronously and efficiently
    val differ = AsyncListDiffer(this, differCallBack)

    // Inflates the item layout and creates a ViewHolder object
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListAdapterViewHolder {
        val binding =
            FeaturedShoesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishListAdapterViewHolder(binding)
    }

    // Returns the size of the current list of products
    override fun getItemCount() = differ.currentList.size

    // Binds the data to the item view
    override fun onBindViewHolder(holder: WishListAdapterViewHolder, position: Int) {
        val currentProduct = differ.currentList[position]


        holder.itemBinding.apply {
            addToWishListBtn.visibility = View.GONE
            addToCart.visibility = View.GONE
            // Set the product title
            productTitle.text = currentProduct.productTitle
            // Set the product price
//            val currentPrice = currentProduct.current_price[0].USD[0]
//            productPrice.text = "$$currentPrice"
//            val oldPrice = currentPrice.toString().toDouble() + 10
//            productOldPrice.apply {
//                text = "$${oldPrice}"
//                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//            }
            // Check if photos list is not empty and load the first image
            val imageUrl = "https://api.timbu.cloud/images/${currentProduct.productImageUrl}"
            if (imageUrl.isNotEmpty()) {
                Glide.with(holder.itemBinding.root.context).load(imageUrl).into(productImage)
            } else {
                // Set a placeholder image or handle the case where there is no image
                productImage.setImageResource(R.drawable.image_placeholder)
            }
            // Set an OnClickListener to navigate to the detail fragment
            productImage.setOnClickListener {
                val productId = currentProduct.productId
                viewModel.fetchProduct(productId)
                it.findNavController()
                    .navigate(R.id.action_productsScreenFragment_to_productDetailFragment)
            }

        }
    }

}
