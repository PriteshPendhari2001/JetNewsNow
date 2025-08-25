package com.example.jetnewsnow.View.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jetnewsnow.Model.Navigation.NavigationRoutes
import com.example.jetnewsnow.NewsViewModel.NewsViewModel
import com.example.jetnewsnow.R
import com.example.jetnewsnow.View.Components.AccountDetails
import com.example.jetnewsnow.ui.theme.grayPrimary
import com.example.jetnewsnow.ui.theme.poppins
import com.example.jetnewsnow.ui.theme.poppinsBold

@Composable
fun SettingScreen(navController: NavController,newsViewModel: NewsViewModel) {

    Column(modifier = Modifier.fillMaxSize().padding(15.dp)) {

        Spacer(modifier = Modifier.height(10.dp))

        Text("Settings", fontSize = 20.sp, fontFamily = poppinsBold, fontWeight = FontWeight.Medium)

        Spacer(modifier = Modifier.height(15.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).clickable {
                navController.navigate(NavigationRoutes.AccountDetails.route)

            },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.person), contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    "Account",
                    fontFamily = poppins,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
            }

            Divider(thickness = 1.dp, color = grayPrimary)

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.bell), contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    "Push Notification",
                    fontFamily = poppins,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Divider(thickness = 1.dp, color = grayPrimary)

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).clickable {
                    navController.navigate(NavigationRoutes.TermsAndConditons.route)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.question), contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    "Terms & Conditions",
                    fontFamily = poppins,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Divider(thickness = 1.dp, color = grayPrimary)

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).clickable {
                    navController.navigate(NavigationRoutes.About.route)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.question), contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    "About",
                    fontFamily = poppins,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Divider(thickness = 1.dp, color = grayPrimary)


            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).clickable {
                    newsViewModel.logOut()
                    navController.navigate(NavigationRoutes.loginScreen.route){
                        popUpTo(NavigationRoutes.HomePage.route){ inclusive = true }
                    }
                },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.logout), contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    "Log Out",
                    fontFamily = poppins,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Divider(thickness = 1.dp, color = grayPrimary)

        }
    }
}