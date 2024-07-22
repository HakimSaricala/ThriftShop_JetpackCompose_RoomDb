package com.example.room_saricala

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.room_saricala.ViewModel.AppViewModel
import com.example.room_saricala.roomDb.Receipt

@Composable
fun Receipt(navgationController: NavHostController, viewmodel: AppViewModel) {
    val receipts = viewmodel.receipts.observeAsState(initial = listOf())
    LazyColumn {
        items(receipts.value) { receipt ->
            ReceiptItem(receipt)
        }
    }
}
@Composable
fun ReceiptItem(receipt: Receipt) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Receipt ID: ${receipt.receiptId}")
            Text(text = "Total Amount: ₱${receipt.total}")
            Text(text = "Date: ${receipt.date}")
        }
    }
}