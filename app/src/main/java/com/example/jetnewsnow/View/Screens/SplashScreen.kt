package com.example.jetnewsnow.View.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jetnewsnow.Model.Navigation.NavigationRoutes
import com.example.jetnewsnow.NewsViewModel.NewsViewModel
import com.example.jetnewsnow.R


@Composable
fun SplashScreen(viewModel: NewsViewModel, navcontroller: NavController){


    val isLoading by viewModel.splashState.collectAsState()

    LaunchedEffect(isLoading){
        if (!isLoading){
            navcontroller.navigate(NavigationRoutes.signupScreen.route){
                popUpTo(NavigationRoutes.splashScreen.route){
                    inclusive = true
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(color = Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource((R.drawable.newspaper)),
            contentDescription = null,
            modifier = Modifier.size(85.dp)
        )
    }
}

