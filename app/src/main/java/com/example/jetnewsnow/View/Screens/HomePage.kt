package com.example.jetnewsnow.View.Screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jetnewsnow.Const
import com.example.jetnewsnow.Model.Navigation.NavigationRoutes
import com.example.jetnewsnow.Model.NewsResponse.Article
import com.example.jetnewsnow.NewsViewModel.AuthState
import com.example.jetnewsnow.NewsViewModel.NewsViewModel
import com.example.jetnewsnow.R
import com.example.jetnewsnow.View.Components.BottomBar
import com.example.jetnewsnow.View.Components.categoriesSelection
import com.example.jetnewsnow.ui.theme.bungee
import com.example.jetnewsnow.ui.theme.poppins
import com.example.jetnewsnow.ui.theme.poppinsBold
import com.example.jetnewsnow.ui.theme.whitePrimary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson


@Composable
fun HomePage(navController: NavController, newsViewModel: NewsViewModel) {

    val authState = newsViewModel.authState.collectAsState()
    val newsState by newsViewModel.newsState.collectAsState()
    val isLoading = newsState == null


    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate(NavigationRoutes.loginScreen.route)
            else -> Unit
        }
    }

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ){ innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(15.dp).padding(innerPadding)) {

            //Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // Empty box to balance the right side
                Box(modifier = Modifier.size(32.dp))

                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Icon(
                        painter = painterResource(R.drawable.newspaper),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(32.dp)
                    )
                    Text(
                        text = "NewsNow",
                        fontWeight = FontWeight.Medium,
                        fontFamily = bungee,
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .offset(y = 2.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(color = whitePrimary, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.notificationbell),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))


            //News Categories Selections Options
            categoriesSelection(newsViewModel, Const.Api_Key)

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

//this card work with both page home and searchNewsPage
@Composable
fun NewsCard(article: Article, navController: NavController,newsViewModel: NewsViewModel) {

    var isSaved by remember { mutableStateOf(false) }


    // Check if article is saved
    LaunchedEffect(article.url) {
        if (article.url != null) {
            newsViewModel.isArticleSaved(article.url) { saved ->
                isSaved = saved
            }
        }
    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val json = Uri.encode(Gson().toJson(article))
                navController.navigate("newsDetails/$json")
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Image
        article.urlToImage?.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Title
        article.title?.let {
            Text(
                text = it,
                fontFamily = poppinsBold,
                fontSize = 18.sp,
                lineHeight = 22.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Description
        article.description?.let {
            Text(
                text = it,
                fontFamily = poppins,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                color = Color.Gray,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Author / Source
        val newsData = mutableListOf<String>()

        article.author?.takeIf { it.isNotBlank() }?.let {
            newsData.add("By $it")
        }
        article.source?.name?.takeIf { it.isNotBlank() }?.let {
            newsData.add(it)
        }

        if (newsData.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                newsData.forEachIndexed { index, item ->
                    Text(
                        text = item,
                        fontFamily = poppins,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    if (index != newsData.lastIndex) {
                        Text(
                            text = " • ",
                            fontFamily = poppins,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }
        }




        // Save button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {
                    if (isSaved) {
                        // Find and remove the saved article
                        // This would require additional logic to get the saved article ID
                    } else {
                        newsViewModel.saveArticle(article)
                    }
                    isSaved = !isSaved
                }
            ) {
                Icon(
                    painter = painterResource(id = if (isSaved) R.drawable.savebookmark else R.drawable.bookmark),
                    contentDescription = null
                )
            }
        }


        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}