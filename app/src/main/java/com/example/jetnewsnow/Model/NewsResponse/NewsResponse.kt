package com.example.jetnewsnow.Model.NewsResponse


data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int,
)