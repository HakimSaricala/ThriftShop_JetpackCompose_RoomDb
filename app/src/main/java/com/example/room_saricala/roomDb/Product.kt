package com.example.room_saricala.roomDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val Productid: Int,
    val name: String,
    val price: Double,
    val quantity: Int
)
