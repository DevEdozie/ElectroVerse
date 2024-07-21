package com.example.khan.local_db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.khan.local_db.entity.CartItem
import com.example.khan.local_db.entity.OrderItem


@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToOrders(orderItem: OrderItem)

//    @Update
//    suspend fun updateCartItem(cartItem: CartItem)
//
//    @Query("DELETE FROM cart WHERE cartItemId = :cartItemId")
//    suspend fun removeFromCart(cartItemId: Int)

    @Query("SELECT * FROM all_orders")
    fun getAllOrderedItems(): LiveData<List<OrderItem>>

}