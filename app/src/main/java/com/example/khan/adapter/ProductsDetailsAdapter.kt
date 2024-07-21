package com.example.khan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
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
class ProductsDetailsAdapter(
    private val viewModel: MainActivityViewmodel
) : RecyclerView.Adapter<ProductsDetailsAdapter.ProductsDetailsAdapterViewHolder>() {

    // ViewHolder class to hold and bind the product item view
    inner class ProductsDetailsAdapterViewHolder(val itemBinding: FeaturedShoesItemBinding) :
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

    //stuff
    private var categoryFilter: String? = null
    private var currentProductId: String? = null

    // Inflates the item layout and creates a ViewHolder object
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsDetailsAdapterViewHolder {
        val binding =
            FeaturedShoesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsDetailsAdapterViewHolder(binding)
    }

    // Returns the size of the current list of products
    override fun getItemCount() = differ.currentList.size

    // Binds the data to the item view
    override fun onBindViewHolder(holder: ProductsDetailsAdapterViewHolder, position: Int) {
        val currentProduct = differ.currentList[position]
        val price = 500

        holder.itemBinding.apply {
            // Set the product title
            productTitle.text = currentProduct.name
            // Set the product price
//            productPrice.text = "$${currentProduct.current_price[0].USD[0]}"
            productPrice.text = "$${price}"
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
            }
            //
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



    fun submitList(items: List<Item>) {
        val filteredItems = categoryFilter?.let { filter ->
            items.filter { item ->
                item.categories.any { category -> category.name == filter }
            }
        } ?: items
        differ.submitList(filteredItems)
    }

    fun updateCategory(categoryName: String?) {
        categoryFilter = categoryName
        submitList(differ.currentList)
    }

//    fun submitList(items: List<Item>, currentProductId: String?) {
//        val filteredItems = categoryFilter?.let { filter ->
//            items.filter { item ->
//                item.id != currentProductId && item.categories.any { category -> category.name == filter }
//            }
//        } ?: items.filter { it.id != currentProductId }
//        differ.submitList(filteredItems)
//    }
//
//    fun updateCategory(categoryName: String?, currentProductId: String?) {
//        categoryFilter = categoryName
//        this.currentProductId = currentProductId
//        // No need to submit list here, it will be handled in setUpRecyclerView
//    }

//    fun submitList(items: List<Item>, currentProductId: String?) {
//        val filteredItems = categoryFilter?.let { filter ->
//            items.filter { item ->
//                item.id != currentProductId && item.categories.any { category -> category.name == filter }
//            }
//        } ?: items.filter { it.id != currentProductId }
//        differ.submitList(filteredItems)
//    }
//
//    fun updateCategory(categoryName: String?, currentProductId: String?) {
//        categoryFilter = categoryName
//        this.currentProductId = currentProductId
//        submitList(differ.currentList, currentProductId)
//    }
}
