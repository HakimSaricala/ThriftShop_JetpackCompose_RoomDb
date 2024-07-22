package com.example.room_saricala.roomDb

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "receipts")
data class Receipt(
    @PrimaryKey(autoGenerate = true) val receiptId: Int,
    val userId: Int,
    val total: Double,
    val date: String,
)
