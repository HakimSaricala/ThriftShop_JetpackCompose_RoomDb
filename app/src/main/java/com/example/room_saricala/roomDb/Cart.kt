package com.example.room_saricala.roomDb

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "cart",
    foreignKeys = [
        ForeignKey(entity = User::class,
            parentColumns = ["userid"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Product::class,
            parentColumns = ["Productid"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class Cart(
    @PrimaryKey(autoGenerate = true) val cartId: Int = 0,
    val userId: Int,
    val productId: Int,
    val quantity: Int
)


data class CartProduct(
    val productId: Int,
    val name: String,
    val price: Double,
    val quantity: Int
)