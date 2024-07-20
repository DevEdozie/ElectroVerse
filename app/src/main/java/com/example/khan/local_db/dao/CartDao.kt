package com.example.khan.local_db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.khan.local_db.entity.CartItem

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(cartItem: CartItem)

    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    @Query("DELETE FROM cart WHERE cartItemId = :cartItemId")
    suspend fun removeFromCart(cartItemId: Int)

    @Query("SELECT * FROM cart")
    fun getCartItems(): LiveData<List<CartItem>>
}
