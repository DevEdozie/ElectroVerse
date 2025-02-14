package com.example.khan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.khan.R
import com.example.khan.databinding.FeaturedShoesItemBinding
import com.example.khan.local_db.entity.CartItem
import com.example.khan.local_db.entity.WishListItem
import com.example.khan.model.Item
import com.example.khan.viewmodel.MainActivityViewmodel


// Adapter class for displaying products in a RecyclerView
class FeaturedProductsAdapter(
    private val viewModel: MainActivityViewmodel
) : RecyclerView.Adapter<FeaturedProductsAdapter.FeaturedProductsAdapterViewHolder>() {

    // ViewHolder class to hold and bind the product item view
    inner class FeaturedProductsAdapterViewHolder(val itemBinding: FeaturedShoesItemBinding) :
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
    var imageUrl: String = ""

    // Inflates the item layout and creates a ViewHolder object
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeaturedProductsAdapterViewHolder {
        val binding =
            FeaturedShoesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeaturedProductsAdapterViewHolder(binding)
    }

    // Returns the size of the current list of products
    override fun getItemCount() = differ.currentList.size

    // Binds the data to the item view
    override fun onBindViewHolder(holder: FeaturedProductsAdapterViewHolder, position: Int) {
        val currentProduct = differ.currentList[position]
        val price = 500

        holder.itemBinding.apply {
            // Set the product title
            productTitle.text = currentProduct.name
            // Set the product price
//            productPrice.text = "$${currentProduct.current_price[0].USD[0]}"
            productPrice.text = "$${price}"
            // Check if photos list is not empty and load the first image
            imageUrl = "https://api.timbu.cloud/images/${currentProduct.photos[0].url}"
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
        val featuredProductsFilter = "featured products"
        val filteredItems = items.filter { item ->
            item.categories.any { category -> category.name == featuredProductsFilter }
        }
        differ.submitList(filteredItems)
    }
}
