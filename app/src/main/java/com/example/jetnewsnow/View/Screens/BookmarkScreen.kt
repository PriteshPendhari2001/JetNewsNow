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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.jetnewsnow.Model.Navigation.NavigationRoutes
import com.example.jetnewsnow.Model.NewsResponse.Article
import com.example.jetnewsnow.Model.NewsResponse.SaveArticle
import com.example.jetnewsnow.Model.NewsResponse.Source
import com.example.jetnewsnow.NewsViewModel.AuthState
import com.example.jetnewsnow.NewsViewModel.NewsViewModel
import com.example.jetnewsnow.R
import com.google.gson.Gson
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(navController: NavController, newsViewModel: NewsViewModel) {
    val savedArticles by newsViewModel.saveArticle.collectAsState()
    val authState by newsViewModel.authState.collectAsState()
    val isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (authState is AuthState.Authenticated) {
            isLoading.value = true
            newsViewModel.fetchSavedArticles()
            delay(1000) // Give time for data to load
            isLoading.value = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Articles") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (isLoading.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (authState is AuthState.Unauthenticated) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Please log in to view saved articles")
                        Button(
                            onClick = { navController.navigate(NavigationRoutes.loginScreen.route) },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Go to Login")
                        }
                    }
                }
            } else if (savedArticles.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Outlined.ThumbUp,
                            contentDescription = "No saved articles",
                            modifier = Modifier.size(64.dp),
                            tint = Color.LightGray
                        )
                        Text("No saved articles yet", modifier = Modifier.padding(top = 16.dp))
                    }
                }
            } else {
                LazyColumn {
                    items(savedArticles) { savedArticle ->
                        SavedArticleItem(
                            article = savedArticle,
                            onDelete = { newsViewModel.deleteSavedArticle(savedArticle.id) },
                            onClick = {
                                // Convert to Article object for navigation
                                val article = Article(
                                    source = Source(id = null, name = savedArticle.source),
                                    author = savedArticle.author,
                                    title = savedArticle.title,
                                    description = savedArticle.description,
                                    url = savedArticle.url,
                                    urlToImage = savedArticle.urlToImage,
                                    publishedAt = savedArticle.publishedAt,
                                    content = null
                                )
                                val json = Uri.encode(Gson().toJson(article))
                                navController.navigate("newsDetails/$json")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SavedArticleItem(article: SaveArticle, onDelete: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Left side → Image
            if (!article.urlToImage.isNullOrEmpty()) {
                AsyncImage(
                    model = article.urlToImage,
                    contentDescription = article.title,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Placeholder if no image
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        painter = painterResource(R.drawable.image),
                        contentDescription = "No image",
                        tint = Color.Gray,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Right side → Text content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = article.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (!article.description.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = article.description!!,
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = article.source?.ifEmpty { "Unknown Source" } ?: "",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}
