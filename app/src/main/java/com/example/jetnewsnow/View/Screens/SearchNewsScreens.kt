package com.example.jetnewsnow.View.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jetnewsnow.Const
import com.example.jetnewsnow.NewsViewModel.NewsViewModel
import com.example.jetnewsnow.R
import com.example.jetnewsnow.View.Components.BottomBar
import com.example.jetnewsnow.ui.theme.grayBlackText
import com.example.jetnewsnow.ui.theme.grayPrimary
import com.example.jetnewsnow.ui.theme.whitePrimary
@Composable
fun SearchNewsScreen(newsViewModel: NewsViewModel, navController: NavController) {

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val newsState by newsViewModel.newsState.collectAsState()
    val isLoading = newsState == null

    var searchNews by remember { mutableStateOf("") }

    Scaffold(bottomBar = {
        BottomBar(navController)
    }){ innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding)
        ) {

            // Search Bar
            OutlinedTextField(
                value = searchNews,
                onValueChange = { searchNews = it },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, shape = RoundedCornerShape(7.dp))
                    .background(Color.White, RoundedCornerShape(7.dp)),
                shape = RoundedCornerShape(7.dp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (searchNews.length >= 3) {
                            newsViewModel.fetchNews(searchNews.trim(), Const.Api_Key)
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    }
                ),
                leadingIcon = {
                    IconButton(onClick = {
                        if (searchNews.length >= 3) {
                            newsViewModel.fetchNews(searchNews.trim(), Const.Api_Key)
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            modifier = Modifier.size(20.dp),
                            contentDescription = "Search Icon"
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.filter),
                            modifier = Modifier.size(20.dp),
                            tint = Color.Black,
                            contentDescription = "Filter Icon"
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = grayBlackText,
                    unfocusedTextColor = grayBlackText,
                    focusedBorderColor = grayPrimary,
                    unfocusedBorderColor = whitePrimary,
                    focusedPlaceholderColor = whitePrimary,
                    cursorColor = grayBlackText,
                ),
                placeholder = {
                    Text("Search News", color = grayBlackText)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // News Content
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                newsState?.articles.isNullOrEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No news found.")
                    }
                }

                else -> {
                    LazyColumn {
                        items(newsState?.articles ?: emptyList()) { article ->
                            NewsCard(article = article, navController = navController,newsViewModel)
                        }
                    }
                }
            }
        }
    }
}


