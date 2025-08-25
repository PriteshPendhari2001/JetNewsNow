package com.example.jetnewsnow.Model.Navigation

import com.example.jetnewsnow.R


open class NavigationRoutes(val route : String){

    object splashScreen : NavigationRoutes("SplashScreen")
    object signupScreen : NavigationRoutes("SignupScreen")
    object loginScreen  : NavigationRoutes("LoginScreen")
    object HomePage : NavigationRoutes("HomePage")
    object DetailsNewsScreen : NavigationRoutes("DetailNews")
    object AccountDetails : NavigationRoutes("AccountDetails")
    object TermsAndConditons : NavigationRoutes("TermsAndConditions")
    object About : NavigationRoutes("About")


    object SearchNews : NavigationRoutes("SearchNews")
    object BookMarkScreen : NavigationRoutes("BookMarkScreen")
    object SettingScreen : NavigationRoutes("SettingScreen")
}

data class BottomNavItems(val routes : String,val icon : Int)

val bottomNavItems = listOf(

    BottomNavItems(
        routes = NavigationRoutes.HomePage.route,
        icon = R.drawable.home
    ),
    BottomNavItems(
        routes = NavigationRoutes.SearchNews.route,
        icon = R.drawable.bsearch
    ),
    BottomNavItems(
        routes = NavigationRoutes.BookMarkScreen.route,
        icon = R.drawable.bookmark
    ),
    BottomNavItems(
        routes = NavigationRoutes.SettingScreen.route,
        icon = R.drawable.setting
    ),

)