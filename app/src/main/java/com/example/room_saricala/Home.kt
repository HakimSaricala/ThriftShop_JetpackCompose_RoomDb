package com.example.room_saricala

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.room_saricala.ViewModel.AppViewModel
import com.example.room_saricala.roomDb.Product
import com.example.room_saricala.ui.theme.background
import com.example.room_saricala.ui.theme.primary
import com.example.room_saricala.ui.theme.secondary
import com.example.room_saricala.ui.theme.surface
import com.example.room_saricala.ui.theme.tertiary


@Composable
fun Home(navgationController: NavHostController, viewmodel: AppViewModel) {
    val products = viewmodel.products.observeAsState(initial = emptyList())
    val currentUser = viewmodel.currentUser.value
    val scrollState = rememberScrollState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(16.dp)
                .verticalScroll(scrollState)

        ) {
            products.value.forEach { product ->
                FruitCard(product, onAddToCartClicked = { selectedProduct, selectedQuantity ->
                    currentUser?.userid?.let { userId ->
                        viewmodel.addProductToCart(userId, selectedProduct.Productid, selectedQuantity)
                    }
                })
            }
        }
    }
}

@Composable
fun FruitCard(product: Product, onAddToCartClicked: (Product, Int) -> Unit) {
    var quantity by remember { mutableStateOf(1) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = background),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = secondary,
                    fontSize = 20.sp
                )
                Text(
                    text = "Price: $${product.price}",
                    color = Color.White
                )
                Text(
                    text = "Quantity: ${product.quantity}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
            Column(modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(0.7f),
                ) {
                    Button(onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier,
                    ) {
                        Text("-")
                    }
                    Text(text = "$quantity", color = Color.White)
                    Button(onClick = { quantity++ }) {
                        Text("+")
                    }
                }
                Row {
                    Button(onClick = { onAddToCartClicked(product, quantity)},
                        modifier = Modifier.fillMaxWidth(0.7f),
                        ) {
                        Image(
                            painterResource(id = R.drawable.cart),
                            contentDescription ="Cart button icon",
                            modifier = Modifier.size(20.dp))

                        Text(text = "Add to cart",Modifier.padding(start = 10.dp))
                    }
                }

            }
        }
    }
}


