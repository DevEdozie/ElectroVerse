package com.example.khan.repository

import androidx.lifecycle.LiveData
import com.example.khan.local_db.dao.CartDao
import com.example.khan.local_db.dao.OrderDao
import com.example.khan.local_db.dao.WishListDao
import com.example.khan.local_db.entity.CartItem
import com.example.khan.local_db.entity.OrderItem
import com.example.khan.local_db.entity.WishListItem


class CacheRepository(
    // orderDao
    // WishlistDao
    private val cartDao: CartDao,
    private val wishListDao: WishListDao,
    private val orderDao: OrderDao
) {


    val cartItems: LiveData<List<CartItem>> = cartDao.getCartItems()
    val wishListItems: LiveData<List<WishListItem>> = wishListDao.getWishListItems()
    val orderItems: LiveData<List<OrderItem>> = orderDao.getAllOrderedItems()

    suspend fun addToCart(cartItem: CartItem) {
        cartDao.addToCart(cartItem)
    }

    suspend fun addToOrders(orderItem: OrderItem) {
        orderDao.addToOrders(orderItem)
    }

    suspend fun addToWishList(wishListItem: WishListItem) {
        wishListDao.addToWishList(wishListItem)
    }

    suspend fun updateCartItem(cartItem: CartItem) {
        cartDao.updateCartItem(cartItem)
    }

    suspend fun removeFromCart(cartItemId: Int) {
        cartDao.removeFromCart(cartItemId)
    }


}
