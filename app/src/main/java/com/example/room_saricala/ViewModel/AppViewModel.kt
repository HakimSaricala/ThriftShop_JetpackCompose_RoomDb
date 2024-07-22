package com.example.room_saricala.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.room_saricala.roomDb.Cart
import com.example.room_saricala.roomDb.CartProduct
import com.example.room_saricala.roomDb.Product
import com.example.room_saricala.roomDb.Receipt
import com.example.room_saricala.roomDb.User
import kotlinx.coroutines.launch
import java.time.LocalDate


data class CurrentUser(val userid: Int, val username: String)
class AppViewModel(private val repository: Repository): ViewModel() {

    private val _currentUser = MutableLiveData<CurrentUser?>()
    val currentUser: MutableLiveData<CurrentUser?> = _currentUser

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _cartProducts = MutableLiveData<List<CartProduct>>()
    val cartProducts: LiveData<List<CartProduct>> = _cartProducts

    private val _receipts = MutableLiveData<List<Receipt>>()
    val receipts: LiveData<List<Receipt>> = _receipts


    fun usernameExists(username: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val exists = repository.usernameExists(username) > 0
            onResult(exists)
        }
    }
    fun addUser(username: String, password: String) {
        viewModelScope.launch {
            try {
                val newUser = User(0, username, password)
                repository.addUser(newUser)
            }catch (e: Exception) {
            }
        }
    }
    fun getUser(username: String, password: String) {
        viewModelScope.launch {
            try {
                val user = repository.getUser(username, password)
                _currentUser.value = CurrentUser(user.userid, user.username)
                loadProducts()
                loadCartProducts()
                loadReceipts()

            }catch (e: Exception){

            }
        }
    }

    fun loadReceipts() {
        viewModelScope.launch {
            try {
                // Step 3: Retrieve all receipts from the repository
                val allReceipts = repository.getReceipts(currentUser.value?.userid ?: 0)

                // Step 4: Update the LiveData with the list of receipts
                _receipts.postValue(allReceipts)
            } catch (e: Exception) {
                Log.e("AppViewModel", "Error loading receipts", e)
            }
        }
    }
    fun loadProducts() {
        viewModelScope.launch {
            try {
                val productsList = repository.getAllProducts()
                _products.postValue(productsList)
                Log.d("AppViewModel", "Products loaded")
            } catch (e: Exception) {
                Log.e("AppViewModel", "Error loading products", e)
            }
        }

    }

    fun createReceipt(  products: List<CartProduct>) {
        viewModelScope.launch {
            try {
                val date = LocalDate.now().toString()
                currentUser.value?.let { repository.createReceiptAndClearCart(it.userid, date, products) }
                loadCartProducts()
                loadReceipts()
                Log.d("AppViewModel", "Receipt created and cart cleared")
            } catch (e: Exception) {
                Log.e("AppViewModel", "Error creating receipt", e)
            }
        }
    }
    fun updateQuantity(productId: Int, newQuantity: Int) {
        viewModelScope.launch {
            currentUser.value?.let { user ->
                val currentCartQuantity = repository.getCartProductQuantity(user.userid, productId) ?: 0
                val quantityDifference = newQuantity - currentCartQuantity

                if (quantityDifference != 0) {
                    // Update cart quantity
                    repository.updateCartQuantity(user.userid, productId, newQuantity)

                    // Retrieve the current product quantity from the inventory
                    val product = repository.getProductById(productId)
                    val newProductQuantity = product.quantity - quantityDifference

                    // Update the product quantity in the inventory
                    repository.updateProductQuantity(productId, newProductQuantity)

                    // Refresh the LiveData for cart products and inventory products
                    loadCartProducts()
                    loadProducts()
                }
            }
        }
    }
    fun deleteProductsFromCart(productIds: List<Int>) {
        viewModelScope.launch {
            try {
                currentUser.value?.let { repository.deleteProductsFromCart(it.userid, productIds) }

                loadCartProducts()
            } catch (e: Exception) {
                Log.e("AppViewModel", "Error deleting products from cart", e)
            }
        }
    }

    fun addProductToCart(userId: Int, productId: Int, quantityToAdd: Int) {
        viewModelScope.launch {
            try {
                // Step 1: Retrieve the product by its ID
                val product = repository.getProductById(productId)

                // Step 2: Check if the product's current quantity is sufficient
                if (product.quantity >= quantityToAdd) {
                    // Step 3: Calculate the new quantity
                    val newQuantity = product.quantity - quantityToAdd

                    // Step 4: Update the product's quantity in the database
                    repository.updateProductQuantity(productId, newQuantity)

                    // Step 5: Update the local _products LiveData to reflect the change in the UI
                    _products.value = _products.value?.map {
                        if (it.Productid == productId) it.copy(quantity = newQuantity) else it
                    }

                    // Step 6: Proceed with adding the product to the cart
                    val cart = Cart(userId = userId, productId = productId, quantity = quantityToAdd)
                    repository.upsertCartProduct(cart)
                    loadCartProducts()

                    Log.d("AppViewModel", "Product added or updated in cart and quantity updated")
                } else {
                    Log.d("AppViewModel", "Insufficient product quantity for productId: $productId")
                }
            } catch (e: Exception) {
                Log.e("AppViewModel", "Error handling product in cart", e)
            }
        }
    }

    private fun loadCartProducts() {
        val currentUserId = _currentUser.value?.userid
        if (currentUserId != null) {
            viewModelScope.launch {
                try {
                    val productsInCart = repository.getAllProductsInCart(currentUserId)
                    _cartProducts.postValue(productsInCart)
                    Log.d("AppViewModel", "Cart products loaded")
                } catch (e: Exception) {
                    Log.e("AppViewModel", "Error loading cart products", e)
                }
            }
        } else {
            Log.d("AppViewModel", "No current user found for loading cart products")
        }
    }
    fun logout() {
        // Clear the current user LiveData or any other necessary cleanup
        _currentUser.value = null
        // Add any additional cleanup logic here, e.g., clearing shared preferences, etc.
    }

}