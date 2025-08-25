package com.example.jetnewsnow.NewsViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetnewsnow.Model.Api.NewsApi
import com.example.jetnewsnow.Model.NewsResponse.Article
import com.example.jetnewsnow.Model.NewsResponse.NewsResponse
import com.example.jetnewsnow.Model.NewsResponse.SaveArticle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val db = FirebaseFirestore.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    private val _newsState = MutableStateFlow<NewsResponse?>(null)
    val newsState : StateFlow<NewsResponse?> = _newsState

    private val newApi = NewsApi.create()

    val categories = listOf("All", "Technology", "Business", "Entertainment", "Health", "Science", "Sports")

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _saveArticle = MutableStateFlow<List<SaveArticle>>(emptyList())
    val saveArticle : StateFlow<List<SaveArticle>> = _saveArticle


    val _splashState = MutableStateFlow(true)
    val splashState : StateFlow<Boolean> = _splashState


    init {

        viewModelScope.launch {
            delay(2000)
            _splashState.value = false
        }

        CheckAuthState()
        onAuthStateChanged()
    }


    fun CheckAuthState(){
        if (auth.currentUser == null){
            _authState.value = AuthState.Unauthenticated
            _saveArticle.value = emptyList()
        }else{
            _authState.value = AuthState.Authenticated
            fetchSavedArticles()
        }
    }


    fun onAuthStateChanged() {
        auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                _authState.value = AuthState.Authenticated
                fetchSavedArticles() // Load saved articles when auth state changes
            } else {
                _authState.value = AuthState.Unauthenticated
                _saveArticle.value = emptyList()
            }
        }
    }


    fun login(email : String,password : String){

        if (email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email and Password can`t be empty")
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message ?: "something went wrong")
                }
            }
    }


    fun signup(email: String,password: String){

        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message ?: "something went wrong")                }
            }
    }

    //this function give data when user search for any news
    fun fetchNews(query: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = newApi.searchForNews(query, apiKey)
                _newsState.value = response

            } catch (e: Exception) {
                Log.e("NewsViewModel", "News fetch failed: ${e.message}", e)
            }
        }
    }

    fun onCategorySelected(category: String, apiKey: String) {
        _selectedCategory.value = category

        val mappedCategory = when (category) {
            "All" -> null
            else -> category.lowercase()
        }

        fetchCategoryNews(mappedCategory, apiKey)
    }

    private fun fetchCategoryNews(category: String?, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = if (category == null) {
                    newApi.getTopHeadlines("us", category,apiKey)
                } else {
                    newApi.getTopHeadlines("us", category, apiKey)
                }
                _newsState.value = response
            } catch (e: Exception) {
                Log.e("NewsViewModel", "Error: ${e.message}", e)
            }
        }
    }

    fun logOut(){
        FirebaseAuth.getInstance().signOut() // This clears Firebase's saved session
        _authState.value = AuthState.Unauthenticated

    }


    //FirebaseFireStore Staring Here

    // Check if article is saved
    fun isArticleSaved(articleUrl: String, callback: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: run {
            callback(false)
            return
        }

        db.collection("saved_articles")
            .whereEqualTo("userId", userId)  // No space
            .whereEqualTo("url", articleUrl)
            .get()
            .addOnSuccessListener { documents ->
                callback(!documents.isEmpty)
            }
            .addOnFailureListener {
                callback(false)
            }
    }


    fun saveArticle(article: Article) {
        val userId = auth.currentUser?.uid ?: run {
            Log.e("NewsViewModel", "User not authenticated when trying to save article")
            return
        }

        // Check if article already exists to avoid duplicates
        db.collection("saved_articles")
            .whereEqualTo("userId", userId)  // No space
            .whereEqualTo("url", article.url ?: "")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Article doesn't exist, save it
                    val savedArticle = SaveArticle(
                        userId = userId,
                        title = article.title ?: "",
                        description = article.description,
                        url = article.url ?: "",
                        urlToImage = article.urlToImage,
                        publishedAt = article.publishedAt,
                        source = article.source?.name,
                        author = article.author
                    )

                    db.collection("saved_articles")
                        .add(savedArticle)
                        .addOnSuccessListener {
                            Log.d("NewsViewModel", "Article saved successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.e("NewsViewModel", "Error saving article", e)
                        }
                } else {
                    Log.d("NewsViewModel", "Article already saved")
                }
            }
            .addOnFailureListener { e ->
                Log.e("NewsViewModel", "Error checking for existing article", e)
            }
    }

    // Fetch user's saved articles
    fun fetchSavedArticles() {
        val userId = auth.currentUser?.uid ?: run {
            Log.e("NewsViewModel", "No user ID available")
            return
        }

        // Make sure field names match exactly what's in Firestore
        db.collection("saved_articles")
            .whereEqualTo("userId", userId)  // No space after "userId"
            .orderBy("savedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w("NewsViewModel", "Listen failed.", error)

                    // Check if it's an index error
                    if (error.code == FirebaseFirestoreException.Code.FAILED_PRECONDITION) {
                        // Try without ordering as fallback
                        //fetchSavedArticlesWithoutOrdering(userId)
                    }
                    return@addSnapshotListener
                }

                val articles = mutableListOf<SaveArticle>()
                for (doc in value!!) {
                    // Use toObject with the SavedArticle class
                    val article = doc.toObject(SaveArticle::class.java).copy(id = doc.id)
                    articles.add(article)
                }
                _saveArticle.value = articles
            }
    }

    // Delete saved article
    fun deleteSavedArticle(articleId: String) {
        db.collection("saved_articles")
            .document(articleId)
            .delete()
            .addOnSuccessListener {
                fetchSavedArticles() // Refresh the list
            }
            .addOnFailureListener { e ->
                Log.e("NewsViewModel", "Error deleting article", e)
            }
    }
}


//State Holder Classes
sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}

