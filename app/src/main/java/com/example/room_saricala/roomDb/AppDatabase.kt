package com.example.room_saricala.roomDb

import androidx.room.Database

import androidx.room.RoomDatabase

@Database(entities = [User::class, Product::class, Cart::class, Receipt::class], version = 6, exportSchema = false)

abstract class AppDatabase: RoomDatabase() {
    abstract fun roomDao(): RoomDao

}
