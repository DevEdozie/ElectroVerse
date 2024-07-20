package com.example.khan.local_db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val cartItemId: Int,
    val productId: String,
    val productImageUrl: String,
    val productTitle: String,
    val productQuantity: Int,
    val productPrice: String
)
