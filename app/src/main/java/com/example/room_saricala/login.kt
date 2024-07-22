package com.example.room_saricala

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.room_saricala.ViewModel.AppViewModel

import com.example.room_saricala.ui.theme.surface


@Composable
fun Login(navController: NavHostController, viewmodel: AppViewModel) {
    var username by remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    val currentUser by viewmodel.currentUser.observeAsState()
    LaunchedEffect(currentUser) {
        currentUser?.let {
            navController.navigate("Main")
        }
    }
    val icon = if (passwordVisibility)
        painterResource(id = R.drawable.show)
    else
        painterResource(id = R.drawable.hide)

    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ){
        Column(modifier = Modifier.size(350.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.thrift_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(300.dp),

            )
        }
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 45.dp, topEnd = 45.dp))
                .background(surface)
                .fillMaxSize(),

            horizontalAlignment = Alignment.CenterHorizontally,
            ) {
            Text(text = "Login", modifier = Modifier.padding(top = 32.dp), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = "Welcome back! Please enter your details", modifier = Modifier.padding(16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = username,
                onValueChange = {username = it},
                label = { Text("Username") },
                shape = RoundedCornerShape(16.dp),
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                placeholder = { Text(text = "Password") },
                label = { Text(text = "Password") },
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(
                            painter = icon,
                            contentDescription = "Visibility Icon"
                        )
                    }
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None
                else PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewmodel.getUser(username, password)
            },
                modifier = Modifier.width(280.dp),
            ) {
                Text(text = "Login", color = Color.White)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = "Don't have an account?")
                TextButton(onClick = { navController.navigate("register")}) {
                    Text(text = "Sign Up")
                }
            }
        }

    }
}

