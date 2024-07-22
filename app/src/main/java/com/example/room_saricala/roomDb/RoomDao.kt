package com.example.room_saricala.roomDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction



@Dao
interface RoomDao {
    //User queries
    @Query("SELECT COUNT(userid) FROM users WHERE username = :username")
    suspend fun usernameExists(username: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): User

    //Product queries
    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<Product>

    @Query("SELECT * FROM products WHERE Productid = :productId LIMIT 1")
    suspend fun getProductById(productId: Int): Product

    @Query("UPDATE products SET quantity = :quantity WHERE Productid = :productId")
    suspend fun updateProductQuantity(productId: Int, quantity: Int)
    @Query("SELECT quantity FROM cart WHERE userId = :userId AND productId = :productId")
    suspend fun getCartProductQuantity(userId: Int, productId: Int): Int?

    suspend fun upsertCartProduct(cart: Cart) {
        val currentQuantity = getCartProductQuantity(cart.userId, cart.productId)
        if (currentQuantity == null) {
            // Product does not exist in the cart, insert new entry
            addProductToCart(cart)
        } else {
            // Product exists, update its quantity
            val newQuantity = currentQuantity + cart.quantity
            UpdateCartQuantity(cart.userId, cart.productId, newQuantity)
        }
    }
    @Query("UPDATE products SET quantity = quantity - :quantity WHERE Productid = :productId")
    suspend fun decrementProductQuantity(productId: Int, quantity: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addProductToCart(cart: Cart)

    @Query("UPDATE cart SET quantity = :quantity WHERE userId = :userId AND productId = :productId")
    suspend fun UpdateCartQuantity(userId: Int, productId: Int, quantity: Int)

    @Query("DELETE FROM cart WHERE userId = :userId AND productId IN (:productIds)")
    suspend fun deleteProductsFromCart(userId: Int, productIds: List<Int>)

    @Query("""
        SELECT p.Productid AS productId, p.name, p.price, c.quantity
        FROM cart c
        JOIN products p ON c.productId = p.Productid
        WHERE c.userId = :userId
    """)
    suspend fun getallProducts(userId: Int): List<CartProduct>

    //Receipt Queries
    // Function to create a receipt and update product quantities
    @Transaction
    suspend fun createReceiptAndClearCart(userId: Int, date: String, products: List<CartProduct>) {
        // Calculate total from products list
        val total = products.sumOf { it.price * it.quantity }

        // Create and insert the receipt
        val receipt = Receipt(0,userId = userId, total = total, date = date)
        insertReceipt(receipt)

        // Update product quantities and clear cart
        products.forEach { product ->
            deleteProductFromCart(userId, product.productId)
        }
    }
    // Insert a receipt into the database
    @Insert
    suspend fun insertReceipt(receipt: Receipt): Long
    // Get all receipts for a user
    @Query("SELECT * FROM receipts WHERE userId = :userId")
    suspend fun getReceiptsByUserId(userId: Int): List<Receipt>
    // Delete a product from the cart
    @Query("DELETE FROM cart WHERE userId = :userId AND productId = :productId")
    suspend fun deleteProductFromCart(userId: Int, productId: Int)

}