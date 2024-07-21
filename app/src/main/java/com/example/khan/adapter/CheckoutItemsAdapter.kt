package com.example.khan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.khan.R
import com.example.khan.databinding.CheckoutItemBinding
import com.example.khan.local_db.entity.CartItem
import com.example.khan.viewmodel.MainActivityViewmodel


// Adapter class for displaying checkout items in a RecyclerView
class CheckoutItemsAdapter(
    private val viewModel: MainActivityViewmodel
) : RecyclerView.Adapter<CheckoutItemsAdapter.CheckoutItemsAdapterViewHolder>() {

    // ViewHolder class to hold and bind the product item view
    inner class CheckoutItemsAdapterViewHolder(val itemBinding: CheckoutItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    // DiffUtil callback to efficiently update the list of products
    private val differCallBack = object : DiffUtil.ItemCallback<CartItem>() {

        // Check if the IDs of the items are the same to determine if they represent the same item
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.cartItemId == newItem.cartItemId
        }

        // Check if the content of the items is the same to determine if they are identical
        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }

    // AsyncListDiffer to handle list changes asynchronously and efficiently
    val differ = AsyncListDiffer(this, differCallBack)

    // Inflates the item layout and creates a ViewHolder object
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CheckoutItemsAdapterViewHolder {
        val binding =
            CheckoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CheckoutItemsAdapterViewHolder(binding)
    }

    // Returns the size of the current list of products
    override fun getItemCount() = differ.currentList.size


    // Binds the data to the item view
    override fun onBindViewHolder(holder: CheckoutItemsAdapterViewHolder, position: Int) {
        val currentProduct = differ.currentList[position]


        holder.itemBinding.apply {
            // Truncate the product title if it exceeds 10 characters
            val currentTitle = currentProduct.productTitle
            val truncatedTitle = if (currentTitle.length > 15) {
                currentTitle.substring(0, 15) + "..."
            } else {
                currentTitle
            }
            // Set the product title
            productTitle.text = truncatedTitle
            // Set the product price
//            val currentPrice = currentProduct.current_price[0].USD[0]
//            productPrice.text = "$$currentPrice"
            productPrice.text = "$${currentProduct.productPrice}"

            // Check if photos list is not empty and load the first image
            val imageUrl = "https://api.timbu.cloud/images/${currentProduct.productImageUrl}"
            if (imageUrl.isNotEmpty()) {
                Glide.with(holder.itemBinding.root.context).load(imageUrl).into(productImage)
            } else {
                // Set a placeholder image or handle the case where there is no image
                productImage.setImageResource(R.drawable.image_placeholder)
            }
            // Set Count Quantity
            qtyCount.text = currentProduct.productQuantity.toString()


        }


    }
}