package com.example.jetnewsnow.View.Components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.jetnewsnow.Model.Navigation.bottomNavItems


@Composable
fun BottomBar(navController: NavController){

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(containerColor = Color.Transparent) {
        bottomNavItems.forEach { items ->
            NavigationBarItem(
                selected = currentRoute == items.routes,
                onClick = {if (currentRoute != items.routes){
                    navController.navigate(items.routes){
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                } },
                icon = { Icon(painter = painterResource(id = items.icon),null) },
            )
        }
    }
}
