package com.example.jetnewsnow.Model.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetnewsnow.Model.NewsResponse.Article
import com.example.jetnewsnow.NewsViewModel.NewsViewModel
import com.example.jetnewsnow.View.Components.AboutScreen
import com.example.jetnewsnow.View.Components.AccountDetails
import com.example.jetnewsnow.View.Components.TermsAndConditionsScreen
import com.example.jetnewsnow.View.Screens.DetailNewsScreen
import com.example.jetnewsnow.View.Screens.HomePage
import com.example.jetnewsnow.View.Screens.LoginScreen
import com.example.jetnewsnow.View.Screens.BookmarkScreen
import com.example.jetnewsnow.View.Screens.SearchNewsScreen
import com.example.jetnewsnow.View.Screens.SettingScreen
import com.example.jetnewsnow.View.Screens.SignupScreen
import com.example.jetnewsnow.View.Screens.SplashScreen
import com.google.gson.Gson

@Composable
fun NewsNavigation(newsViewModel: NewsViewModel){

    val navcontroller = rememberNavController()

    NavHost(navcontroller, startDestination = NavigationRoutes.splashScreen.route){

        composable(NavigationRoutes.splashScreen.route){
            SplashScreen(newsViewModel,navcontroller)
        }

        composable(NavigationRoutes.signupScreen.route){
            SignupScreen(navcontroller,newsViewModel)
        }

        composable(NavigationRoutes.loginScreen.route){
            LoginScreen(navcontroller,newsViewModel)
        }

        composable(NavigationRoutes.HomePage.route){
            HomePage(navcontroller,newsViewModel)
        }

        composable(
            "newsDetails/{articleJson}",
            arguments = listOf(navArgument("articleJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("articleJson")
            val article = Gson().fromJson(json, Article::class.java)
            DetailNewsScreen(article = article,navcontroller)
        }

        composable(NavigationRoutes.SearchNews.route){
            SearchNewsScreen(newsViewModel,navcontroller)
        }

        composable(NavigationRoutes.BookMarkScreen.route){
            BookmarkScreen(navcontroller,newsViewModel)
        }

        composable(NavigationRoutes.SettingScreen.route){
            SettingScreen(navcontroller,newsViewModel)
        }

        composable(NavigationRoutes.AccountDetails.route){
            AccountDetails(navcontroller)
        }

        composable(NavigationRoutes.TermsAndConditons.route){
            TermsAndConditionsScreen(navcontroller)
        }
        composable(NavigationRoutes.About.route){
            AboutScreen(navcontroller)
        }
    }
}