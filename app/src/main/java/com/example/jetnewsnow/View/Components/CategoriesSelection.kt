package com.example.jetnewsnow.View.Components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jetnewsnow.Const
import com.example.jetnewsnow.NewsViewModel.NewsViewModel
import com.example.jetnewsnow.ui.theme.blackPrimary
import com.example.jetnewsnow.ui.theme.grayPrimary

@Composable
fun categoriesSelection(
    newsViewModel: NewsViewModel,
    apiKey: String
) {
    val selectedCategory by newsViewModel.selectedCategory.collectAsState()
    val categories = newsViewModel.categories

    LazyRow(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        items(categories) { category ->
            val isSelected = selectedCategory == category
            val underlineWidth by animateDpAsState(
                targetValue = if (isSelected) 24.dp else 0.dp,
                label = ""
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) blackPrimary else grayPrimary,
                label = ""
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clickable {
                        newsViewModel.onCategorySelected(category, apiKey)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = category,
                    color = textColor,
                    fontWeight = FontWeight.Medium
                )
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .width(underlineWidth)
                        .background(Color.Black)
                )
            }
        }
    }
}
