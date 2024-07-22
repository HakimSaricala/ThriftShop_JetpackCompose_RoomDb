package com.example.room_saricala.roomDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val userid: Int,
    val username: String,
    val password: String
)
