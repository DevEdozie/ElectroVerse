package com.example.khan.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khan.local_db.database.AppDatabase
import com.example.khan.local_db.entity.CartItem
import com.example.khan.local_db.entity.OrderItem
import com.example.khan.local_db.entity.WishListItem
import com.example.khan.model.BaseResponse
import com.example.khan.model.Item
import com.example.khan.model.ProductsResponse
import com.example.khan.repository.CacheRepository
import com.example.khan.repository.TimbuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewmodel(application: Application) : AndroidViewModel(application) {


    // Lazy initialization of the repository
    val timbuRepo by lazy {
        TimbuRepository()
    }

    // LiveData for products response
    private val _timbuResult: MutableLiveData<BaseResponse<ProductsResponse>> = MutableLiveData()
    val timbuResult: LiveData<BaseResponse<ProductsResponse>> = _timbuResult

    // LiveData for single product response
    private val _timbuProduct: MutableLiveData<BaseResponse<Item>> = MutableLiveData()
    val timbuProduct: LiveData<BaseResponse<Item>> = _timbuProduct

    // LiveData for list of items
    private val _items: MutableLiveData<List<Item>> = MutableLiveData()
    val items: LiveData<List<Item>> = _items

    // LiveData for total price
    private val _totalPrice: MutableLiveData<Int> = MutableLiveData()
    val totalPrice: LiveData<Int> = _totalPrice

    // LiveData for delivery fee
    private val _deliveryFee: MutableLiveData<Int> = MutableLiveData()
    val deliveryFee: LiveData<Int> = _deliveryFee

    // LiveData for discount
    private val _discount: MutableLiveData<Int> = MutableLiveData()
    val discount: LiveData<Int> = _discount

    // LiveData for final total
    private val _finalTotal: MutableLiveData<Int> = MutableLiveData()
    val finalTotal: LiveData<Int> = _finalTotal

    private val repository: CacheRepository

    init {
        val cartDao = AppDatabase.getDatabase(application).cartDao()
        val wishListDao = AppDatabase.getDatabase(application).wishListDao()
        val orderDao = AppDatabase.getDatabase(application).orderDao()
        repository = CacheRepository(cartDao, wishListDao, orderDao)
        fetchProducts()
        calculatePriceSummary() // Calculate price summary on initialization
    }


    // Function to fetch products
    fun fetchProducts() {
        // Set initial state to loading
        _timbuResult.postValue(BaseResponse.Loading())

        // Launch coroutine to perform network request
        viewModelScope.launch {
            val response = timbuRepo.getProducts()
            if (response is BaseResponse.Success) {
                // Post success response and update items list
                _timbuResult.postValue(response)
                _items.postValue(response.data?.items)
            } else if (response is BaseResponse.Error) {
                // Post error response and clear items list
                _timbuResult.postValue(response)
                _items.postValue(emptyList())
            }
        }
    }

    // Function to fetch a single product by its ID
    fun fetchProduct(productId: String) {
        // Set initial state to loading
        _timbuProduct.postValue(BaseResponse.Loading())

        // Launch coroutine to perform network request
        viewModelScope.launch {
            val response = timbuRepo.getProduct(productId)
            if (response is BaseResponse.Success) {
                // Post success response
                _timbuProduct.postValue(response)
            } else if (response is BaseResponse.Error) {
                // Post error response
                _timbuProduct.postValue(response)
            }
        }
    }

    // Function to add a product to the wish list
    fun addToWishList(wishListItem: WishListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val wishListItems = getAllWishListItems()
            val productName = wishListItem.productTitle
            val isInWishList =
                wishListItems.value?.any { product -> product.productTitle == productName } ?: false
            if (isInWishList) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        getApplication(),
                        "Item is already in wish list",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                repository.addToWishList(wishListItem)
                withContext(Dispatchers.Main) {
                    Toast.makeText(getApplication(), "Item added to wish list", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


    // Function to get all wishlist items
    fun getAllWishListItems() = repository.wishListItems


    // Function to add a product to the cart
    fun addToCart(item: CartItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val cartItems = getAllCartItems()
            val productName = item.productTitle
            val isInCart =
                cartItems.value?.any { product -> product.productTitle == productName } ?: false
            if (isInCart) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        getApplication(),
                        "Item is already in the cart",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                repository.addToCart(item)
                withContext(Dispatchers.Main) {
                    Toast.makeText(getApplication(), "Item added to cart", Toast.LENGTH_SHORT)
                        .show()
                }
                calculatePriceSummary() // Recalculate price summary
            }
        }
    }


    // Function to update a product in the cart
    fun updateCartItem(item: CartItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCartItem(item)
            calculatePriceSummary() // Recalculate price summary
        }
    }

    // Function to remove a product from the cart
    fun removeFromCart(cartItemId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFromCart(cartItemId)
            calculatePriceSummary() // Recalculate price summary
        }
    }

    // Function to get all cart items
    fun getAllCartItems() = repository.cartItems

    // Function to add cart items to orders and clear the cart
    fun checkout() {
        viewModelScope.launch(Dispatchers.IO) {
            val cartItems = repository.cartItems.value ?: emptyList()
            cartItems.forEach { cartItem ->
                val orderItem = OrderItem(
                    orderItemId = 0, // Auto-generate
                    productId = cartItem.productId,
                    productImageUrl = cartItem.productImageUrl,
                    productTitle = cartItem.productTitle,
                    productQuantity = cartItem.productQuantity,
                    productPrice = cartItem.productPrice
                )
                repository.addToOrders(orderItem)
                //

                repository.removeFromCart(cartItem.cartItemId)
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(getApplication(), "Order placed successfully", Toast.LENGTH_SHORT)
                    .show()
            }
            calculatePriceSummary() // Recalculate price summary after checkout
        }
    }

    // Function to get all ordered items
    fun getAllOrderedItems() = repository.orderItems

    // Function to calculate total price, delivery fee, discount, and final total
    private fun calculatePriceSummary() {
        viewModelScope.launch(Dispatchers.IO) {
            val cartItems = repository.cartItems.value ?: emptyList()
            var totalPrice = 0
            val deliveryFee = 150
            val discount = 100

            cartItems.forEach { item ->
                totalPrice += item.productPrice.toInt() * item.productQuantity
                // Example logic for delivery fee and discount
//                deliveryFee += 5.0 // Fixed delivery fee per item
//                discount += if (item.productQuantity > 1) 10.0 else 0.0 // Discount for multiple items
            }

            val finalTotal = totalPrice + deliveryFee - discount

            withContext(Dispatchers.Main) {
                _totalPrice.value = totalPrice
                _deliveryFee.value = deliveryFee
                _discount.value = discount
                _finalTotal.value = finalTotal
            }
        }
    }

}
