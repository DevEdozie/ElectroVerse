package com.example.khan.local_db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishListItem(
    @PrimaryKey(autoGenerate = true) val wishListItemId: Int,
    val productId: String,
    val productImageUrl: String,
    val productTitle: String,
    val productPrice: String
)


