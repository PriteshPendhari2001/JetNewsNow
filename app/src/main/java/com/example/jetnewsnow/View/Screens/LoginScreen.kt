package com.example.jetnewsnow.View.Screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jetnewsnow.Model.Navigation.NavigationRoutes
import com.example.jetnewsnow.NewsViewModel.AuthState
import com.example.jetnewsnow.NewsViewModel.NewsViewModel
import com.example.jetnewsnow.R
import com.example.jetnewsnow.View.Components.validateEmail
import com.example.jetnewsnow.View.Components.validatePassword
import com.example.jetnewsnow.ui.theme.blackPrimary
import com.example.jetnewsnow.ui.theme.bluePrimary
import com.example.jetnewsnow.ui.theme.darkGray
import com.example.jetnewsnow.ui.theme.grayBlackText
import com.example.jetnewsnow.ui.theme.poppins
import com.example.jetnewsnow.ui.theme.poppinsBold
import com.example.jetnewsnow.ui.theme.purplePrimary

@Composable
fun LoginScreen(navController: NavController,newsViewModel: NewsViewModel){

    var email by remember{ mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val authState = newsViewModel.authState.collectAsState()
    val context = LocalContext.current


    LaunchedEffect(authState.value){
        when(authState.value){
            AuthState.Authenticated -> navController.navigate(NavigationRoutes.HomePage.route)
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()
            else -> Unit
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(15.dp)){

        Spacer(modifier = Modifier.height(40.dp))

        Row(modifier = Modifier.padding(start = 8.dp)){
            Text("Hello", fontFamily = poppinsBold, fontSize = 35.sp, color = blackPrimary)
        }

        Row(modifier = Modifier.padding(start = 8.dp)){
            Text("Again!", fontFamily = poppinsBold, fontSize = 35.sp, color = bluePrimary)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.padding(start = 8.dp)){
            Text("Welcome back you`ve \nbeen missed", fontSize = 18.sp, fontFamily = poppins, color = grayBlackText)
        }

        Column(modifier = Modifier.fillMaxSize()){

            Spacer(modifier = Modifier.height(40.dp))

            Text("Email",modifier = Modifier.padding(start = 10.dp), color = grayBlackText, fontFamily = poppins)
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = validateEmail(email)
                },
                isError = emailError != null,
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = purplePrimary,
                    unfocusedIndicatorColor = darkGray
                )
            )
            emailError?.let {
                Text(
                    it,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Password",modifier = Modifier.padding(start = 10.dp), color = grayBlackText, fontFamily = poppins)
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = validatePassword(password)
                },
                isError = passwordError != null,
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(
                            painter = painterResource(id = if (passwordVisible) R.drawable.visible else R.drawable.eye_off),
                            contentDescription = null
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = purplePrimary,
                    unfocusedIndicatorColor = darkGray
                )
            )
            passwordError?.let {
                Text(
                    it,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                )
            }


            TextButton(onClick = {}, modifier = Modifier.align(Alignment.End)){
                Text("Forgot the password ?", color = bluePrimary, fontFamily = poppins, fontWeight = SemiBold,
                    fontSize = 13.sp)
            }

            Button(
                onClick = {
                    if (emailError == null && passwordError == null){
                        newsViewModel.login(email,password)
                    }
                },
                enabled = authState.value != AuthState.Loading,
                modifier = Modifier.fillMaxWidth().padding(8.dp).height(45.dp),
                shape = RoundedCornerShape(8.dp)
            ){
                Text("Login", fontSize = 15.sp,fontFamily = poppins)
            }

            Row(modifier = Modifier.align(Alignment.End).padding(8.dp)){
                Text("don`t have an account?", fontFamily = poppins, color = grayBlackText)
                Text("Sign Up", fontFamily = poppins, color = bluePrimary, fontWeight = SemiBold,
                    modifier = Modifier.clickable {
                    navController.navigate(NavigationRoutes.signupScreen.route)
                })
            }
        }
    }
}

