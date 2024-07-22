package com.example.room_saricala

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.room_saricala.ViewModel.AppViewModel
import com.example.room_saricala.roomDb.CartProduct
import com.example.room_saricala.ui.theme.background
import com.example.room_saricala.ui.theme.primary
import com.example.room_saricala.ui.theme.surface
import com.example.room_saricala.ui.theme.tertiary

@Composable
fun CartScreen(navgationController: NavHostController, viewmodel: AppViewModel) {
    val cartProducts by viewmodel.cartProducts.observeAsState(initial = emptyList())
    var isEditMode by remember { mutableStateOf(false) }
    var editableCartProducts by remember { mutableStateOf(cartProducts) }
    // Initialize a list of booleans to track the checked state of each cart product
    var checkedStates by remember { mutableStateOf(List(cartProducts.size) { false }) }

    // Calculate the total price for checked items
    val totalPrice = editableCartProducts.zip(checkedStates).sumOf { (cartProduct, isChecked) ->
        if (isChecked) cartProduct.quantity * cartProduct.price else 0.0
    }
    // Update editableCartProducts whenever cartProducts changes
    LaunchedEffect(cartProducts) {
        editableCartProducts = cartProducts.toMutableList()
        checkedStates = List(cartProducts.size) { false }

    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Total: ₱$totalPrice", color = tertiary, fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp))
                TextButton(onClick = {
                    if (isEditMode) {
                        editableCartProducts.forEach { cartProduct ->
                            viewmodel.updateQuantity(cartProduct.productId, cartProduct.quantity)
                            Log.d("CartScreen", " ${cartProduct}")
                        }
                    }
                    isEditMode = !isEditMode
                }) {
                    if(isEditMode)
                        Text(text = "Save", color = tertiary, fontSize = 18.sp)
                    else
                        Text(text = "Edit", color = background, fontSize = 18.sp)
                }
            }
            LazyColumn {
                itemsIndexed(editableCartProducts) { index, cartProduct ->
                    CartCard(
                        cartProduct = cartProduct,
                        isEditMode = isEditMode,
                        onQuantityChange = { newQuantity ->
                            val updatedList = editableCartProducts.toMutableList()
                            val updatedProduct = updatedList[index].copy(quantity = newQuantity)
                            updatedList[index] = updatedProduct
                            editableCartProducts = updatedList
                        },
                        isChecked = checkedStates[index],
                        onCheckedChange = { newValue ->
                            checkedStates = checkedStates.toMutableList().also { it[index] = newValue }
                        }
                    )
                }
            }
        }
        if (!isEditMode && editableCartProducts.any { it.quantity <= 0 }) {
            val productsToRemove = editableCartProducts.filter { it.quantity <= 0 }.map { it.productId }
            viewmodel.deleteProductsFromCart(productsToRemove)
            editableCartProducts = editableCartProducts.filterNot { it.quantity <= 0 }.toMutableList()
        }
        FloatingActionButton(
            onClick = {
                val checkedProducts = editableCartProducts.filterIndexed { index, _ ->checkedStates[index] }
                viewmodel.createReceipt(checkedProducts)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .width(150.dp)
                .zIndex(1f),
        ) {
            Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.shopping),
                    contentDescription = "Checkout",
                    tint = tertiary,
                    modifier = Modifier.size(24.dp)
                )
                Text(text = "Checkout", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 8.dp), fontSize = 18.sp)
            }

        }

    }

}
@Composable
fun CartCard(
    cartProduct: CartProduct,
    isEditMode: Boolean,
    onQuantityChange: (Int) -> Unit,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    var quantity by remember { mutableStateOf(cartProduct.quantity) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = primary
        )
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cartProduct.name,
                    color = tertiary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Price: ₱${cartProduct.price}",
                    color = tertiary
                )
                Text(
                    text = "Quantity: ${cartProduct.quantity}",
                    color = tertiary,
                    fontSize = 14.sp
                )
            }
            if (isEditMode) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(0.5f),
                ) {
                    Button(onClick = {
                        if (quantity > 0) {
                            quantity--
                            onQuantityChange(quantity)
                        }
                    }) {
                        Text("-")
                    }
                    Text(text = "$quantity", color = tertiary)
                    Button(onClick = {
                        quantity++
                        onQuantityChange(quantity)
                    }) {
                        Text("+")
                    }
                }
            } else {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = onCheckedChange,
                )
            }

        }
    }
}
