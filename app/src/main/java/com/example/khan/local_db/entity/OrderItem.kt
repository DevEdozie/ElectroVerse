package com.example.khan.local_db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "all_orders")
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    val orderItemId: Int,
    val productId: String,
    val productImageUrl: String,
    val productTitle: String,
    var productQuantity: Int,
    val productPrice: String

)

