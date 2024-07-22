package com.example.room_saricala.ViewModel

import com.example.room_saricala.roomDb.AppDatabase
import com.example.room_saricala.roomDb.Cart
import com.example.room_saricala.roomDb.CartProduct
import com.example.room_saricala.roomDb.Product
import com.example.room_saricala.roomDb.Receipt
import com.example.room_saricala.roomDb.User

class Repository(private val db: AppDatabase) {

    suspend fun usernameExists(username: String): Int{
        return db.roomDao().usernameExists(username)
    }
    suspend fun addUser(user: User){
        db.roomDao().addUser(user)
    }

    suspend fun getUser(username: String, password: String): User {
        return db.roomDao().getUser(username, password)
    }

    suspend fun getAllProducts(): List<Product> {
        return db.roomDao().getAllProducts()
    }
    suspend fun getProductById(productId: Int): Product {
        return db.roomDao().getProductById(productId)
    }

    suspend fun getCartProductQuantity(userId: Int, productId: Int): Int? {
        return db.roomDao().getCartProductQuantity(userId, productId)
    }
    suspend fun updateProductQuantity(productId: Int, quantity: Int) {
        db.roomDao().updateProductQuantity(productId, quantity)
    }

    suspend fun getAllProductsInCart(userId: Int): List<CartProduct> {
        return db.roomDao().getallProducts(userId)
    }
    suspend fun upsertCartProduct(cart: Cart) {
        return db.roomDao().upsertCartProduct(cart)
    }

    suspend fun updateCartQuantity(userId: Int, productId: Int, quantity: Int) {
        db.roomDao().UpdateCartQuantity(userId, productId, quantity)
    }

    suspend fun deleteProductsFromCart(userId: Int, productIds: List<Int>) {
        db.roomDao().deleteProductsFromCart(userId, productIds)
    }
    suspend fun decrementProductQuantity(productId: Int, quantity: Int) {
        db.roomDao().decrementProductQuantity(productId, quantity)
    }

    suspend fun createReceiptAndClearCart(userId: Int, date: String, products: List<CartProduct>) {
        db.roomDao().createReceiptAndClearCart(userId, date, products)
    }
    suspend fun getReceipts(userId: Int): List<Receipt> {
        return db.roomDao().getReceiptsByUserId(userId)
    }

}