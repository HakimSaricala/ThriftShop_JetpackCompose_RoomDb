package com.example.room_saricala

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.room_saricala.ViewModel.AppViewModel
import com.example.room_saricala.ViewModel.Repository
import com.example.room_saricala.roomDb.AppDatabase
import com.example.room_saricala.ui.theme.Room_saricalaTheme
import com.example.room_saricala.ui.theme.background

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            name = "Saricala.db"

        ).build()
    }
    private val viewmodel by viewModels<AppViewModel>(
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AppViewModel(Repository(db)) as T
                }
            }
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Room_saricalaTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = background) {
                    MyApp(viewmodel)
                }
            }
        }
    }
}
@Composable
fun MyApp(viewmodel: AppViewModel) { Authnav(viewmodel)
}

