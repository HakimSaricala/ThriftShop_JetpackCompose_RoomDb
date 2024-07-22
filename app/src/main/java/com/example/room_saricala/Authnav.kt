package com.example.room_saricala

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.room_saricala.ViewModel.AppViewModel


@Composable
fun Authnav(viewmodel: AppViewModel) {
    val navController = rememberNavController()
    Surface {
        NavHost(navController = navController, startDestination = "Splash") {
            composable("login") {
                Login(navController, viewmodel)
            }
            composable("register") {
                Register(navController, viewmodel)
            }
            composable("Main") {
                MainScreen(navController, viewmodel)
            }
            composable("Splash") {
                SplashScreen(navController)
            }
        }
    }
}