package com.example.jetnewsnow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetnewsnow.Model.Navigation.NewsNavigation
import com.example.jetnewsnow.NewsViewModel.NewsViewModel
import com.example.jetnewsnow.ui.theme.JetNewsNowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetNewsNowTheme {
                val  newsViewModel : NewsViewModel = viewModel()
                NewsNavigation(newsViewModel)
            }
        }
    }
}
