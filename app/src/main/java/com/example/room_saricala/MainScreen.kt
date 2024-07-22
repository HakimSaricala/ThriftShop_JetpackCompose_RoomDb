package com.example.room_saricala

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.room_saricala.ViewModel.AppViewModel
import com.example.room_saricala.ui.theme.background
import com.example.room_saricala.ui.theme.primary
import com.example.room_saricala.ui.theme.surface
import com.example.room_saricala.ui.theme.tertiary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewmodel: AppViewModel, ) {
    val navgationController = rememberNavController()
    val currentUser by viewmodel.currentUser.observeAsState()
    LaunchedEffect(key1 = currentUser) {
        if (currentUser == null) {
            navController.navigate("login") {
                popUpTo("login") { inclusive = true }
            }
        }
    }
    Surface(
        color = background,
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),

                    title = {
                        Text("")

                    },

                    navigationIcon = {
                        val image: Painter = painterResource(id = R.drawable.thrift_logo)
                        Row(modifier = Modifier.padding(10.dp)) {
                            Image(
                                painter = image,
                                contentDescription = "Localized description",
                                Modifier.size(60.dp)

                                )

                        }

                    },
                )
            },
            bottomBar = { Buttombar(navgationController) }
        ) {innerPadding ->
            NavHost(navController = navgationController, startDestination = "Home",
                modifier = Modifier.padding(innerPadding)){
                composable("Home") {
                    Home(navgationController,viewmodel)
                }
                composable("Cart") {
                    CartScreen(navgationController,viewmodel)
                }
                composable("Chekout") {
                    Receipt(navgationController, viewmodel)
                }
                composable("Profile") {
                    Profile(navgationController,viewmodel)
                }

            }

        }

    }
}

@Composable
fun Buttombar(navgationController: NavController) {
    val home = painterResource(id = R.drawable.home)
    val cart = painterResource(id = R.drawable.cart)
    val checkout = painterResource(id = R.drawable.receipt)
    val profile = painterResource(id = R.drawable.profile)

    //get the current route
    val navBackStackEntry = navgationController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val selected = when {
        currentRoute?.startsWith("Home") == true -> home

        currentRoute?.startsWith("Cart") == true -> cart

        currentRoute?.startsWith("Chekout") == true -> checkout

        currentRoute?.startsWith("Profile") == true -> profile

        else -> home
    }
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)),
        containerColor = surface,
        contentColor = primary,
        tonalElevation = 10.dp

    ) {
        IconButton(
            onClick = {
                navgationController.navigate("Home") {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = home,
                contentDescription = null,
                Modifier.size(26.dp),
                tint = if (selected == home) tertiary else Color.White
            )
        }
        IconButton(
            onClick = {
                navgationController.navigate("Cart")

                },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = cart,
                contentDescription = null,
                Modifier.size(26.dp),
                tint = if (selected == cart) tertiary else Color.White
            )
        }
        IconButton(
            onClick = {
                navgationController.navigate("Chekout") {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = checkout,
                contentDescription = null,
                Modifier.size(26.dp),
                tint = if (selected == checkout) tertiary else Color.White
            )
        }
        IconButton(
            onClick = {
                navgationController.navigate("Profile") {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = profile,
                contentDescription = null,
                Modifier.size(26.dp),
                tint = if (selected == profile) tertiary else Color.White
            )
        }
    }
}