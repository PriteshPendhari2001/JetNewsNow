package com.example.jetnewsnow.Model.NewsResponse

import com.google.firebase.Timestamp
import com.google.firebase.database.Exclude


data class SaveArticle(
    @Exclude var id: String = "",
    var userId: String = "",
    var title: String = "",
    var description: String? = "",
    var url: String = "",
    var urlToImage: String? = "",
    var publishedAt: String? = "",
    var source: String? = "",
    var author: String? = "",
    var savedAt: Timestamp = Timestamp.now()
) {
    // Empty constructor for Firestore
    constructor() : this("", "", "", "", "", "", "", "", "", Timestamp.now())
}