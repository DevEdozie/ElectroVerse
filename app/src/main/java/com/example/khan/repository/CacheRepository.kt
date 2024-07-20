package com.example.khan.repository

import androidx.lifecycle.LiveData
import com.example.khan.local_db.dao.CartDao
import com.example.khan.local_db.entity.CartItem


class CacheRepository(
    // orderDao
    // WishlistDao
    private val cartDao: CartDao,
) {


    val cartItems: LiveData<List<CartItem>> = cartDao.getCartItems()

    suspend fun addToCart(cartItem: CartItem) {
        cartDao.addToCart(cartItem)
    }

    suspend fun updateCartItem(cartItem: CartItem) {
        cartDao.updateCartItem(cartItem)
    }

    suspend fun removeFromCart(cartItemId: Int) {
        cartDao.removeFromCart(cartItemId)
    }


}
