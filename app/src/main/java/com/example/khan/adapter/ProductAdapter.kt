package com.example.khan.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.khan.R
import com.example.khan.databinding.ProductsItemLayoutBinding
import com.example.khan.databinding.SpecialOfferItemBinding
import com.example.khan.local_db.entity.CartItem
import com.example.khan.local_db.entity.WishListItem
import com.example.khan.model.Item
//import com.example.khan.view.ProductsScreenFragmentDirections
import com.example.khan.viewmodel.MainActivityViewmodel

// Adapter class for displaying products in a RecyclerView
class ProductAdapter(
    private val viewModel: MainActivityViewmodel
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // ViewHolder class to hold and bind the product item view
    inner class ProductViewHolder(val itemBinding: SpecialOfferItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    // DiffUtil callback to efficiently update the list of products
    private val differCallBack = object : DiffUtil.ItemCallback<Item>() {

        // Check if the IDs of the items are the same to determine if they represent the same item
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        // Check if the content of the items is the same to determine if they are identical
        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    // AsyncListDiffer to handle list changes asynchronously and efficiently
    val differ = AsyncListDiffer(this, differCallBack)

    // Inflates the item layout and creates a ViewHolder object
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =
            SpecialOfferItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    // Returns the size of the current list of products
    override fun getItemCount() = differ.currentList.size

    // Binds the data to the item view
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentProduct = differ.currentList[position]
        val price = 500


        holder.itemBinding.apply {
            // Set the product title
            productTitle.text = currentProduct.name
            // Set the product price
//            val currentPrice = currentProduct.current_price[0].USD[0]
//            productPrice.text = "$$currentPrice"
            productPrice.text = "$$price"
//            val oldPrice = currentPrice.toString().toDouble() + 10
            val oldPrice = price + 10
            productOldPrice.apply {
                text = "$${oldPrice}"
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            // Check if photos list is not empty and load the first image
            val imageUrl = "https://api.timbu.cloud/images/${currentProduct.photos[0].url}"
            if (imageUrl.isNotEmpty()) {
                Glide.with(holder.itemBinding.root.context).load(imageUrl).into(productImage)
            } else {
                // Set a placeholder image or handle the case where there is no image
                productImage.setImageResource(R.drawable.image_placeholder)
            }
            // Set an OnClickListener to navigate to the detail fragment
            productImage.setOnClickListener {
                val productId = currentProduct.id
                viewModel.fetchProduct(productId)
                it.findNavController()
                    .navigate(R.id.action_productsScreenFragment_to_productDetailFragment)
            }

            addToCart.setOnClickListener {
                viewModel.addToCart(
                    CartItem(
                        cartItemId = 0,
                        productId = currentProduct.id,
                        productImageUrl = currentProduct.photos[0].url,
                        productTitle = currentProduct.name,
                        productQuantity = 1,
                        productPrice = "$price"
                    )
                )

            }

            // Add to wishlist
            addToWishListBtn.setOnClickListener {
                viewModel.addToWishList(
                    WishListItem(
                        wishListItemId = 0,
                        productId = currentProduct.id,
                        productImageUrl = currentProduct.photos[0].url,
                        productPrice = "$price",
                        productTitle = currentProduct.name
                    )
                )

            }
        }


    }

    // Method to filter and submit the list of items
    fun submitList(items: List<Item>) {
        val specialOffersFilter = "special offers"
        val filteredItems = items.filter { item ->
            item.categories.any { category -> category.name == specialOffersFilter }
        }
        differ.submitList(filteredItems)
    }
}
